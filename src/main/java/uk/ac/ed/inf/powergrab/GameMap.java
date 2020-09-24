package uk.ac.ed.inf.powergrab;

import com.mapbox.geojson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;



public class GameMap {
    public final static String BASE_URL_TPL = "http://homepages.inf.ed.ac.uk/stg/powergrab/%s/powergrabmap.geojson";
    public final static double FEATURE_MAX_POWER = 125;
    private String downloadUrl;
    private List<Feature> features;
    private double allCoin;

    //initialise the map: download Map and save all the features
    public GameMap(GameParam gameParam){
        this.downloadUrl = String.format(BASE_URL_TPL,gameParam.yyyymmdd("/"));
        String mapJsonStr =  downLoadMap();
        FeatureCollection fc = FeatureCollection.fromJson(mapJsonStr);
        features = fc.features();
        // calculate all the positive coins from a map
        features.forEach(feature -> {
           double coins = feature.getProperty("coins").getAsDouble();
           if(0 < coins){
               allCoin = CalculateUtils.DoubleAdd(this.allCoin,coins);
           }
        });
    }
  
    //update the coinsLeft and powerLeft in the features 
    public void updateFeature(Feature feature){
        String id = feature.getProperty("id").toString();
        for (Feature f: this.features) {
            if (id.equals(f.getProperty("id"))) {
                f.removeProperty("coins");
                f.removeProperty("power");
                f.addNumberProperty("coins",feature.getProperty("coins").getAsDouble());
                f.addNumberProperty("power",feature.getProperty("power").getAsDouble());
            }
        }
    }

    //downloading the map
    public String downLoadMap(){
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(this.downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            String line;
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return result.toString();
    }

    //write a copy of the map and record the fly path
    public void exportMap(String path,List<Point> flyPath){
        Geometry geometryFlightPath = LineString.fromLngLats(flyPath);
        Feature flightPathFeature = Feature.fromGeometry(geometryFlightPath);
        this.features.add(flightPathFeature);
        FeatureCollection mapInfo = FeatureCollection.fromFeatures(this.features);
        try{
            Files.write(Paths.get(path), mapInfo.toJson().toString().getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //calculate the distance between two positions  
    public static double getDistance(Position a, Position b){
        double x = a.longitude - b.longitude;
        double y = a.latitude - b.latitude;
        return Math.sqrt(Math.abs(Math.pow(x, 2) + Math.pow(y, 2)));
    }

    //calculate the distance between current position to station position
    public static double getDistance(Position a, Point b){
        double x =  CalculateUtils.DoubleSubtract(a.longitude,b.longitude());
        double y =  CalculateUtils.DoubleSubtract(a.latitude,b.latitude());
        return Math.sqrt(Math.abs(Math.pow(x, 2) + Math.pow(y, 2)));
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public double getAllCoin() {
        return allCoin;
    }

    public void setAllCoin(double allCoin) {
        this.allCoin = allCoin;
    }
}
