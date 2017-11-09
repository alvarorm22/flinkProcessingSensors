import org.apache.flink.api.common.functions.FilterFunction;


public class FiltroCount implements FilterFunction<SensorPojo> {


    @Override
    public boolean filter(SensorPojo sensorPojo) throws Exception {

        if(sensorPojo.getMovimiento().equals("true")) sensorPojo.setTipo("movimiento");
        else if (sensorPojo.getHumo().equals("true")) sensorPojo.setTipo("humo");
        else if ((Integer.parseInt(sensorPojo.getTemperatura())) >= (Integer.parseInt(sensorPojo.getTempMaxima()))) {
            sensorPojo.setTipo("temperatura");
        }

        if (sensorPojo.getTipo().equals(null)){
            sensorPojo.setTipo("empty");
        }

        System.out.println("---------------------------------------Recibido +1 Tipo: "+sensorPojo.getTipo());


        return true;
    }
}
