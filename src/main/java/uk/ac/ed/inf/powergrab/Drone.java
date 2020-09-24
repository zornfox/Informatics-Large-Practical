package uk.ac.ed.inf.powergrab;


import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Drone {
    public  static  final double STEP_FUEL= 1.25; // assign the energy will be used for every step
    public  static  final double COLLECT_DISTANCE = 0.00025;
    protected double power;
    protected double coin;
    protected int step;
    protected Position currentLocation;
    protected List<Point> flyPath;
    protected String flyLog;
    protected GameMap gameMap;

    public Drone(){
        init();
    }

    public Drone(Position position,GameMap gameMap){
        init(position,gameMap);
    }

    // initialise the drone when start the game 
    public void init(){   
        this.power = 250.0;
        this.coin = 0.0;
        this.step = 0;
        this.flyPath = new ArrayList<>();
        this.flyLog = "";
    }

    public void init(Position position,GameMap gameMap){
        this.currentLocation = position;
        this.gameMap = gameMap;
        init();
    }

    // Flying 
    public void flying(){

        Position old_position = this.getCurrentLocation();

        // get direction
        Direction nextDirection = nextDirection();

        // each movement
        move(nextDirection);

        // transferring 
        collection();

        // log recorded after each movement 
        this.flyLog += String.format("%s,%s,%s,%s,%s,%s,%s\n",old_position.latitude,old_position.longitude,
                nextDirection,this.currentLocation.latitude,this.currentLocation.longitude,
                this.getCoin(),this.getPower());
    }

    // choose the next direction 
    protected abstract Direction nextDirection();

    // finish one movement 
    protected void move(Direction nextDirection){
        this.currentLocation = this.currentLocation.nextPosition(nextDirection);
        this.step += 1;
        this.power = CalculateUtils.DoubleSubtract(this.power,Drone.STEP_FUEL);
        // after each movement check the power left 
        if(this.power < 0){
            this.power = 0;
        }
        flyPath.add(Point.fromLngLat(this.currentLocation.longitude,this.currentLocation.latitude));
    }

    // during collection, automatically transfer and update station
    protected void collection(){
       Feature feature =  collectionFeature();
       if(null == feature){
           return;
       }
       calculate(feature);
        this.gameMap.updateFeature(feature);
    }

    //find the closest station, using distance to determine 
    protected Feature collectionFeature(){
        Feature feature = null;
        double  min_distance = Double.MAX_VALUE;

        for (Feature f : this.gameMap.getFeatures()){
            Point featureLocation = (Point) f.geometry();
            if(featureLocation != null ){
                double distance = GameMap.getDistance(this.currentLocation, featureLocation);
                double icons = f.getProperty("coins").getAsDouble();
                if (distance <= Drone.COLLECT_DISTANCE && distance < min_distance && 0 != icons) {
                    min_distance = distance;
                    feature = f;
                }
            }
        }
        return  feature;
    }

    // calculate the energyLeft/CoinLeft of the station and Coins/Energy holding by the drone now
    protected void calculate( Feature f ){
        double  coins = f.getProperty("coins").getAsDouble();
        double  power = f.getProperty("power").getAsDouble();
        this.coin = CalculateUtils.DoubleAdd(this.coin,coins);
        this.power = CalculateUtils.DoubleAdd(this.power,power);
    
        f.removeProperty("coins");
        f.removeProperty("power");

        // update the station properties when transferring cannot completely finished
        if(this.coin < 0){
            f.addNumberProperty("coins",this.coin);
            this.coin = 0;
        }else{
            f.addNumberProperty("coins",0);
        }

        if(this.power < 0){
            f.addNumberProperty("power",this.power);
            this.power = 0;
        }else{
            f.addNumberProperty("power",0);
        }
    }

    // get the next effective direction collection
    protected Map<Direction,Double> nextDirections(){
        // find the next position
        Map<Direction,Double> directionFeatures = new HashMap<>();
        // get all possible direction
        for (Direction direction: Direction.values()){
            // next possible positions
            Position nextFuturePosition = this.currentLocation.nextPosition(direction);
            // coin default
            Double coins = 0.0;
            // check if inside the range of map
            if(nextFuturePosition.inPlayArea()){
                for (Feature feature : this.gameMap.getFeatures()){
                    // get the location of the feature
                    Point featurePoint = (Point) feature.geometry();
                    // if the location is not null
                    if(featurePoint != null ){
                        // get the distance between next possible position and the location of the feature
                        double distance = GameMap.getDistance(nextFuturePosition, featurePoint);
                        // if the distance inside the collecting range, applying collecting 
                        if (distance <= Drone.COLLECT_DISTANCE){
                            coins = feature.getProperty("coins").getAsDouble();
                        }
                    }
                }
                directionFeatures.put(direction,coins);
            }
        }
        return  directionFeatures;
    }

    //For exporting the files 
    public void exportLog(String path){
        try{
            Files.write(Paths.get(path), flyLog.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getCoin() {
        return coin;
    }

    public void setCoin(double coin) {
        this.coin = coin;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Position getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Position currentLocation) {
        this.currentLocation = currentLocation;
    }
}
