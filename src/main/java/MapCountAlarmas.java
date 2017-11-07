import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class MapCountAlarmas extends RichMapFunction<SensorPojo, SensorPojo>  {
    private ValueState<Integer> count;



    @Override
    public void open(Configuration config) {
        ValueStateDescriptor<Integer> descriptor =
                new ValueStateDescriptor<>(
                        "count", // the state name
                        TypeInformation.of(new TypeHint<Integer>() {}), // type information
                        0); // default value of the state, if nothing was set
        count = getRuntimeContext().getState(descriptor);
    }


    @Override
    public SensorPojo map(SensorPojo sensorPojo) throws Exception {
        System.out.println("Hello, I am in map function of MapCountAlarmas");

        Integer state = count.value();
        state++;
        count.update(state);


        return sensorPojo;
    }
}
