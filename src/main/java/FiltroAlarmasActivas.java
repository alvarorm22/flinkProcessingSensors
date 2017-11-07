import jnr.ffi.annotations.In;
import org.apache.flink.api.common.functions.FilterFunction;


public class FiltroAlarmasActivas implements FilterFunction<SensorPojo> {
    @Override
    public boolean filter(SensorPojo sensorPojo) throws Exception {

        String id = sensorPojo.getId();
        String movimiento = sensorPojo.getMovimiento();
        String humo = sensorPojo.getHumo();
        String temperatura = sensorPojo.getTemperatura();
        String tempMaxima = sensorPojo.getTempMaxima();

        System.out.println( "||--Mensaje recibido--> " +
                "\tSensor ID: " + id +
                "\tMovimiento: " + movimiento +
                "\tHumo: " + humo +
                "\tTemperatura:" + temperatura +
                "\tT.Maxima: " + tempMaxima);

        boolean bool = false;
        if (sensorPojo.getMovimiento().equals("true")){
            bool = true;
            sensorPojo.setTipo("movimiento");
        } else if (sensorPojo.getHumo().equals("true")){
            bool = true;
            sensorPojo.setTipo("humo");
        } else if ((Integer.parseInt(temperatura)) >= (Integer.parseInt(tempMaxima))){
            bool = true;
            sensorPojo.setTipo("temperatura");
        }

        if(!bool) System.out.println("El sensor " + id + " es correcto... se descarta el mensaje...");

        return bool;
    }
}
