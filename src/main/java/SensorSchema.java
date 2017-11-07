import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;

/**
 * Implements a SerializationSchema and DeserializationSchema for TaxiRide for Kafka data sources and sinks.
 */
public class SensorSchema implements DeserializationSchema<SensorPojo>, SerializationSchema<SensorPojo> {

    @Override
    public byte[] serialize(SensorPojo element) {
        return element.toString().getBytes();
    }

    @Override
    public SensorPojo deserialize(byte[] message) {
        return SensorPojo.fromString(new String(message));
    }

    @Override
    public boolean isEndOfStream(SensorPojo nextElement) {
        return false;
    }

    @Override
    public TypeInformation<SensorPojo> getProducedType() {
        return TypeExtractor.getForClass(SensorPojo.class);
    }
}
