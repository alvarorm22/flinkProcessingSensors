import org.apache.flink.api.common.functions.FilterFunction;

public class FiltroGenerarAlarma implements FilterFunction<SensorPojo> {
    @Override
    public boolean filter(SensorPojo sensorPojo) throws Exception {
        boolean bool = false;

        if(sensorPojo != null){
            bool = true;
            System.out.println("Se llama a sink para el sensor " + sensorPojo.getId());
        }

        return bool;
    }
}
