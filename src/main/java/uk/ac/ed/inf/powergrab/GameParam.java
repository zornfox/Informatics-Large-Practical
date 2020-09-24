package uk.ac.ed.inf.powergrab;


//Encapsulation

public class GameParam {

    private String day;
    private String month;
    private String year;
    private double latitude;
    private double longitude;
    private long seed;
    private String droneType;

    public Position getPosition(){
        return  new Position(this.latitude,this.longitude);
    }
    // define the date format of URL address
    public String yyyymmdd(String separator){
        return String.format("%s%s%s%s%s",year,separator,month,separator,day);
    }
    // define the date format of the exporting files' name
    public String ddmmyyyy(String separator){

        return String.format("%s%s%s%s%s",day,separator,month,separator,year);
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public String getDroneType() {
        return droneType;
    }

    public void setDroneType(String droneType) {
        this.droneType = droneType;
    }
}
