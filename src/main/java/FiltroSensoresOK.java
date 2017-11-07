import org.apache.flink.api.common.functions.FilterFunction;


public class FiltroSensoresOK implements FilterFunction<SensorPojo> {
    @Override
    public boolean filter(SensorPojo sensorPojo) throws Exception {

        String id = sensorPojo.getId();
        String movimiento = sensorPojo.getMovimiento();
        String humo = sensorPojo.getHumo();
        String temperatura = sensorPojo.getTemperatura();
        String tempMaxima = sensorPojo.getTempMaxima();

        System.out.println( "||||--Mensaje recibido--> " +
                "\tSensor ID: " + id +
                "\tMovimiento: " + movimiento +
                "\tHumo: " + humo +
                "\tTemperatura:" + temperatura +
                "\tT.Maxima: " + tempMaxima);

        boolean bool = false;
        if ((sensorPojo.getMovimiento().equals("false")) && (sensorPojo.getHumo().equals("false")) &&
                ((Double.parseDouble(sensorPojo.getTemperatura())) < (Double.parseDouble(sensorPojo.getTempMaxima())))){
            bool=true;
        }

        if(bool) System.out.println("El sensor ha recuperado su estado correcto... Se procede a llamar a AlarmasOkSink para " +
                "actualizar dashboard y enviar mail");

        return bool;
    }
}