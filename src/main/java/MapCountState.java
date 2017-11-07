import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import sun.management.Sensor;


public class MapCountState  extends RichMapFunction<SensorPojo, SensorPojo> {

    private ValueState <Tuple2<Long,Long>> count;

    SensorPojo sensorPojoReturn = new SensorPojo();

    @Override
    public SensorPojo map(SensorPojo sensorPojo) throws Exception {

        System.out.println("I'm in Map function with sensor " + sensorPojo.getId());

        // access the state value
        System.out.println("Recupero el estado del sensor recibido");
        //Estado -> Tupla <HUMO,TEMPERATURA>
        Tuple2<Long,Long> state = count.value();

        if(sensorPojo.getTipo().equals("movimiento")){
            sensorPojoReturn = sensorPojo;
            System.out.println("Es de tipo movimiento --> retorno sensor");
        }else if (sensorPojo.getTipo().equals("humo")){
            System.out.println("Es de tipo humo --> incremento cuenta");
            state.f0++;
            System.out.println("La cuenta de humo es: "+state.f0);
            if((state.f0) == Long.valueOf(3)) {
                System.out.println("Es de tipo humo --> cuenta = 3, retorno sensor y " +
                        "reseteo cuenta");
                sensorPojoReturn = sensorPojo;
                state.f0= Long.valueOf(0);
            }else {
                sensorPojoReturn = null;
                System.out.println("Es de tipo humo --> cuenta < 3, retorno null");
            }
        }else if (sensorPojo.getTipo().equals("temperatura")){
            System.out.println("Es de tipo temperatura --> incremento cuenta");
            state.f1++;
            System.out.println("La cuenta de temperatura es: "+state.f1);
            if((state.f1) == Long.valueOf(3)) {
                System.out.println("Es de tipo temperatura --> cuenta = 3, retorno sensor " +
                        "y reseteo cuenta");
                sensorPojoReturn = sensorPojo;
                state.f1=Long.valueOf(0);
            }else{
                sensorPojoReturn = null;
                System.out.println("Es de tipo temperatura --> cuenta < 3, retorno null");
            }
        }

        // update the state
        System.out.println("FIN MAP -> Actualizo estado");
        count.update(state);

        return sensorPojoReturn;

    }



    @Override
    public void open(Configuration config) {
        ValueStateDescriptor<Tuple2<Long, Long>> descriptor =
                new ValueStateDescriptor<>(
                        "count", // the state name
                        TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() {}), // type information
                        Tuple2.of(0L, 0L)); // default value of the state, if nothing was set
        count = getRuntimeContext().getState(descriptor);
    }
}