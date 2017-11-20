import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import javax.mail.MessagingException;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AlarmasOkSink implements SinkFunction<SensorPojo>, Serializable {

    static CassandraConnector cc = new CassandraConnector("ip-172-31-1-114", 9042);

    public void invoke(SensorPojo sensorPojo) throws Exception {

        System.out.println("I'm in Alarmas OK Sink");
        DataPostInfluxDB dataPostInfluxDB = new DataPostInfluxDB();
        DataMessageMail dataMessageMail = new DataMessageMail();

        getFromCassandra(sensorPojo.getId(),sensorPojo.getTipo(),dataPostInfluxDB, dataMessageMail);
        PostToInfluxDB(sensorPojo,dataPostInfluxDB);

        try {
            sendMail(dataMessageMail, sensorPojo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }

    public void PostToInfluxDB(SensorPojo sensorPojo,DataPostInfluxDB dataPostInfluxDB) throws Exception {

        String url = "http://ip-172-31-29-33:8086/write?db="+dataPostInfluxDB.getZone();
        System.out.println("The URL in InfluxDB is: "+url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        String urlParameters = "test,host=server"+ sensorPojo.getId() + ",geohash="+
                dataPostInfluxDB.getGeohash() +" value=0";

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


    public void getFromCassandra(String idString, String tipoAlarma,
                                 DataPostInfluxDB dataPostInfluxDB, DataMessageMail dataMessageMail) throws MessagingException {

        int id = Integer.parseInt(idString);
        String consulta = "select * from world.datosclientes where id = " + id;

        System.out.println("Searching in cassandra...");
        ResultSet data = cc.getSession().execute(consulta);
        Row datos = data.one();

        dataMessageMail.setName(datos.getString("name"));
        dataMessageMail.setSurname(datos.getString("surname"));
        dataMessageMail.setLati(datos.getDouble("latitude"));
        dataMessageMail.setLongi(datos.getDouble("longitude"));
        dataMessageMail.setZone(datos.getString("zone"));
        dataMessageMail.setStreet(datos.getString("streetaddress"));
        dataMessageMail.setCity(datos.getString("city"));
        dataMessageMail.setCountry(datos.getString("country"));
        dataMessageMail.setMail(datos.getString("emailaddress"));
        dataMessageMail.setTelcode(datos.getString("telephonecountrycode"));
        dataMessageMail.setTelephone(datos.getString("telephonenumber"));
        dataMessageMail.setGeohash(datos.getString("geohash"));

        String message = dataMessageMail.toString(tipoAlarma,id, "ok");
        System.out.println(message);

        dataPostInfluxDB.setGeohash(dataMessageMail.getGeohash());
        dataPostInfluxDB.setZone(dataMessageMail.getZone());

    }

    public static void sendMail(DataMessageMail message, SensorPojo sensorPojo) throws MessagingException {

        System.out.println("Se envia mail...");
        String title = "["+sensorPojo.getId()+"] - SENSOR OK";
        GoogleMail.Send("monitoring.secure.alert", "*****", "monitoring.secure.alert@gmail.com",
                title, message.toString(sensorPojo.getTipo(),Integer.parseInt(sensorPojo.getId()), "ok"));

    }

}
