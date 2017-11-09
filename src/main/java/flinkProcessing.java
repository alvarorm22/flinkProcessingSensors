import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.io.Serializable;
import java.util.Properties;


public class flinkProcessing implements Serializable{

    public static void main(String[] args) throws Exception {

        System.out.println("Flink is listening...");

        // create execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "ip-172-31-6-74:9092,ip-172-31-40-250:9092");
        //properties.setProperty("group.id", "flink_consumer");


        //Consumer del topic 'ENTRY', donde se mandan todos los mensajes que generan los sensores.
        DataStream<SensorPojo> stream = env
                .addSource(new FlinkKafkaConsumer010<>("ENTRY", new SensorSchema(), properties));

        /*Consumer del topic 'OK', donde se mandan los mensajes de los sensores que recuperan
          su estado correcto después de haber saltado su alarma*/
        DataStream<SensorPojo> streamOk = env
                .addSource(new FlinkKafkaConsumer010<>("OK", new SensorSchema(), properties));

        //Producer que escribe en el topic 'COUNT' los sensores que se han activado
        FlinkKafkaProducer010<SensorPojo> myProducer = new FlinkKafkaProducer010<SensorPojo>("COUNT",
                new SensorSchema(), properties);

        /*Consumer del topic 'COUNT' donde se mandan los sensores que se han activado para posteriormente obtenerlos
          con flink y llevar la cuenta*/
        DataStream<SensorPojo> CountStream = env
                .addSource(new FlinkKafkaConsumer010<>("COUNT", new SensorSchema(), properties));

    //prueba git


        // ----------Topología stream---------
        /*
        1- (Filter) Obtengo los mensajes del Stream ya de tipo SensorPojo y hago un filtro para quedarme
           solo con las que llegan con algún campo activo (movimiento, humo o temperatura), el
           resto los descarto.
        2- (keyBy) Hago el KeyBy para paralelizar y particionar los datos por la clave "id". Se asegura
           que todos los mensajes con la misma clave son procesados por el mismo operador.
        3- (map) Se realiza un map para obtener y analizar el estado de cada sensor. Cada sensor tiene
           un estado <Tuple2<Int,Int>> de humo y temperatura y se registra la cuenta de los mensajes
           que llegan con ese campo activo. La lógica es esperar a la llegada de tres mensajes
           con el campo humo o temperatura activos para generar la alarma.
           Devuelve el sensor en caso de que haya que generar la alarma o null si la cuenta no ha
           llegado todavía a 3.
        4- (Filter) Filtro los mensajes que llegan del map. Se retornan los sensores para activar la alarma
           y se descartan los null.
        5- (Sink) Se reciben los sensores activos, se lanza una petición a cassandra para obtener los
           datos del cliente con la alarma activa, se hace un POST a la bbdd Influx para actualizar
           el mapa en Grafana y se envía un mail a un buzón de correo notificando la alarma.
         */
        SingleOutputStreamOperator MainStream =  stream.filter(new FiltroAlarmasActivas()).
                keyBy("id").map(new MapCountState()).filter(new FiltroGenerarAlarma());
        MainStream.addSink(new AlarmasActivasSink());

        //Escribo los sensores con alarma activa en el topic 'count' a través del productor para procesarlos posteriormente
        MainStream.addSink(myProducer);


        /*Obtengo los sensores del topic 'count', analizo cual es el tipo de la alarma, particiono los datos por su tipo
          y aplico una ventana de un minuto. Almaceno la cuenta de los sensores activos y envío la cuenta a InfluxDB
          para visualizar las estadísticas de los últimos minutos en Grafana según el tipo de alarma generada*/
        CountStream.filter(new FiltroCount()).keyBy("tipo").
                timeWindow(Time.seconds(60)).apply(new PostInfluxStatistics()).print();


        // ----------Topología streamOK---------
        /*
        1- (Filter) Comprueba que todos los campos del sensor están correctos y devuelve true
           o false si se cumple o no esa condición.
        2- (keyBy) Hago el KeyBy para paralelizar y particionar los datos por la clave "id". Se asegura
           que todos los mensajes con la misma clave son procesados por el mismo operador.
        3- (Sink) Se reciben los sensores que han recuperado su estado correcto, se lanza una petición
           a cassandra para obtener los datos del cliente, se hace un POST a la bbdd Influx para
           actualizar el mapa en Grafana y que el sensor vuelva a la normalidad y se envía un mail
           al buzón de correo notificando la recuperación del sensor.
        */
        streamOk.filter(new FiltroSensoresOK()).keyBy("id").addSink(new AlarmasOkSink());



        env.execute();


    }

}











        /*stream.filter(new FiltroAlarmasActivas()).keyBy("id")
                .map(new MapCountState()).filter(new FiltroGenerarAlarma())
                .addSink(new AlarmasActivasSink());*/