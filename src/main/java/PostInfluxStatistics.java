import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class PostInfluxStatistics<IN> extends Window implements WindowFunction<IN, Object, Tuple, TimeWindow> {
    @Override
    public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<IN> iterable, Collector<Object> collector) throws Exception {

        int count = 0;
        Iterator<IN> itr = iterable.iterator();

        SensorPojo pojoit;
        String tipo = null;

        while (itr.hasNext()){
            count++;
            pojoit = (SensorPojo) itr.next();
            tipo = pojoit.getTipo();
        }

        if(tipo!=null && tipo!="empty"){
            System.out.println("------------------Time window---------------The count of " + tipo +
                    " in this window is " + count);
        } else System.out.println("------------------This window is empty, any alarm has been activated");


        String url = "http://ip-172-31-29-33:8086/write?db=prueba";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        String urlParameters = "count,tipo=" + tipo + ",window=60 value=" + count;


        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

    }

    @Override
    public long maxTimestamp() {
        return 0;
    }
}
