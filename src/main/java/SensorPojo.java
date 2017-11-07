import org.apache.flink.api.common.state.ValueState;

import java.io.Serializable;

public class SensorPojo implements Serializable{
    private String id;
    private String movimiento;
    private String humo;
    private String temperatura;
    private String tempMaxima;
    private String jsonString;
    private String tipo;


    public SensorPojo(){}
    public SensorPojo(String id, String movimiento, String humo,
                      String temperatura, String tempMaxima){
        this.id=id;
        this.movimiento=movimiento;
        this.humo=humo;
        this.temperatura=temperatura;
        this.tempMaxima=tempMaxima;

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",");
        sb.append(movimiento).append(",");
        sb.append(humo).append(",");
        sb.append(temperatura).append(",");
        sb.append(tempMaxima).append(",");


        return sb.toString();
    }

    public static SensorPojo fromString(String line) {

        String[] tokens = line.split(",");
        if (tokens.length != 5) {
            throw new RuntimeException("Invalid record: " + line);
        }

        SensorPojo sens = new SensorPojo();

        try {
            sens.setId(tokens[0]);
            sens.setMovimiento(tokens[1]);
            sens.setHumo(tokens[2]);
            sens.setTemperatura(tokens[3]);
            sens.setTempMaxima(tokens[4]);


        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid record: " + line, nfe);
        }

        return sens;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getHumo() {
        return humo;
    }

    public void setHumo(String humo) {
        this.humo = humo;
    }

    public String getTempMaxima() {
        return tempMaxima;
    }

    public void setTempMaxima(String tempMaxima) {
        this.tempMaxima = tempMaxima;
    }

    public String getJsonString() { return jsonString; }

    public void setJsonString(String jsonString) { this.jsonString = jsonString; }

    public String getTipo(){ return this.tipo; }

    public void setTipo (String tip){ this.tipo = tip; }

}
