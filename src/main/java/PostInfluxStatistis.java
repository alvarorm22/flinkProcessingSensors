import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class PostInfluxStatistis implements org.apache.flink.streaming.api.functions.windowing.WindowFunction<SensorPojo, Object, org.apache.flink.api.java.tuple.Tuple, org.apache.flink.streaming.api.windowing.windows.TimeWindow> {
    @Override
    public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<SensorPojo> iterable, Collector<Object> collector) throws Exception {

    }
}
