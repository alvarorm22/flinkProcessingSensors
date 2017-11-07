public class DataMessageMail {
    String name;
    String surname;
    Double lati;
    Double longi;
    String zone;
    String street;
    String city;
    String country;
    String mail;
    String telcode;
    String telephone;
    String geohash;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Double getLati() {
        return lati;
    }

    public void setLati(Double lati) {
        this.lati = lati;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelcode() {
        return telcode;
    }

    public void setTelcode(String telcode) {
        this.telcode = telcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public DataMessageMail(String name, String surname, Double lati,
                           Double longi, String zone, String street,
                           String city, String country, String mail,
                           String telcode, String telephone, String geohash) {
        this.name = name;
        this.surname = surname;
        this.lati = lati;
        this.longi = longi;
        this.zone = zone;
        this.street = street;
        this.city = city;
        this.country = country;
        this.mail = mail;
        this.telcode = telcode;
        this.telephone = telephone;
        this.geohash = geohash;
    }

    public DataMessageMail(){}

    public String toString(String tipoAlarma, int id, String tipo){
        String message = null;

        if (tipo.equals("alarma")) {
            message = "Se ha activado la alarma de " + tipoAlarma + " del sensor " + id +
                    "\nLos datos del cliente son los siguentes:" +
                    "\nNombre:\t \t \t \t \t" + this.getName() +
                    "\nApellidos:\t \t \t \t \t" + this.getSurname() +
                    "\nDirección:\t \t \t \t \t" + this.getStreet() +
                    " (" + this.getCity() + " - " + this.getCountry() + ")" +
                    "\nTeléfono:\t \t \t \t \t" + "(" + this.getTelcode()
                    + ") " + this.getTelephone() +
                    "\nGeolocalización:\t \t \t \t \tLatitud - " + this.getLati()
                    + " Longitud - " + this.getLongi() + " Zona - " + this.getZone() +
                    "\nEmail: \t \t \t \t \t" + this.getMail() +
                    "\nDashboard:\t \t \t \t \t" + this.getZone();
        }else if (tipo.equals("ok")){
            message = "Se ha recuperado el sensor " + id +
                    "\nLos datos del cliente son los siguentes:" +
                    "\nNombre:\t \t \t \t \t" + this.getName() +
                    "\nApellidos:\t \t \t \t \t" + this.getSurname() +
                    "\nDirección:\t \t \t \t \t" + this.getStreet() +
                    " (" + this.getCity() + " - " + this.getCountry() + ")" +
                    "\nTeléfono:\t \t \t \t \t" + "(" + this.getTelcode()
                    + ") " + this.getTelephone() +
                    "\nGeolocalización:\t \t \t \t \tLatitud - " + this.getLati()
                    + " Longitud - " + this.getLongi() + " Zona - " + this.getZone() +
                    "\nEmail: \t \t \t \t \t" + this.getMail() +
                    "\nDashboard:\t \t \t \t \t" + this.getZone();
        }

        return message;
    }
}








