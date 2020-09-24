package uk.ac.ed.inf.powergrab;


import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;

import java.util.*;
import java.util.stream.Collectors;

public class StatefulDrone extends Drone {

    // record the station it has already visited
    private List<String> collectedFeatures;
    private Feature nextFeature;

    public StatefulDrone(Position position,GameMap gameMap){
        super(position,gameMap);
        this.collectedFeatures = new ArrayList<>();
    }

    @Override
    protected  Direction nextDirection(){
        // if the next feature are null, keep searching
        if(null == nextFeature){
            nextFeature = nextFeature();
        }

        //get the next direction by Feature
        Direction direction =  getNextDirectionByFeature(nextFeature);

        //get the next direction
        return  direction;
    }


    // collecting and transferring
    @Override
    protected void collection(){

        Feature feature =   collectionFeature();

        // check if the feature is null
        if(null == feature){
            return;
        }

        String  featureId  = feature.getProperty("id").toString();

        // check if the station has been visited
        if(null != nextFeature && featureId.equals(nextFeature.getProperty("id").toString())){
            nextFeature = null;
        }

        if(!collectedFeatures.contains(featureId)){
            // record the feature after we finished
            this.collectedFeatures.add(featureId);
        }

        calculate(feature);
        this.gameMap.updateFeature(feature);

    }


    //get the next direction by Feature
    private Direction getNextDirectionByFeature(Feature nextFeature){
        // if the positive station has been visited, randomly choose direction to move until game is over
        if(null == nextFeature){
            return getRandomDirection();
        }

        // get the position of the next feature
        Point nextPoint =  (Point) nextFeature.geometry();

       // Direction direction = getDirectionByPoint(nextPoint);
        Direction nextDirection = null;
        Double minDistance = Double.MAX_VALUE;
        Map<Direction,Double> directionFeatures = nextDirections();
        directionFeatures.entrySet().removeIf(v -> v.getValue() < 0);
        List<Direction> directions  = new ArrayList<>(directionFeatures.keySet());
        for (Direction direction: directions){
            Position futurePosition = this.currentLocation.nextPosition(direction);
            if(futurePosition.inPlayArea()) {
                double distance = GameMap.getDistance(futurePosition, nextPoint);
                if (distance < minDistance) {
                    minDistance = distance;
                    nextDirection =  direction;
                }
            }
        }

        return nextDirection;

    }

    // get random direction
    private Direction getRandomDirection(){
        Map<Direction,Double> directionFeatures = nextDirections();
        directionFeatures.entrySet().removeIf(v -> v.getValue() < 0);
        List<Direction> directions  = new ArrayList<>(directionFeatures.keySet());
        int random = (int)(0+Math.random()*(directions.size()));
        return directions.get(random);
    }


    // get next feature
    private Feature nextFeature(){

        Map<Feature,Double> featureDistance = new HashMap<>();
        // scan the map, get the all positive station which is not being visited
        for (Feature feature : this.gameMap.getFeatures()){
            if(!collectedFeatures.contains(feature.getProperty("id"))){
                // get the location of the feature
                Point featurePoint = (Point) feature.geometry();
                if(featurePoint != null &&  feature.getProperty("coins").getAsDouble() > 0){
                	// get the distance between next position and feature
                    double distance = GameMap.getDistance(this.currentLocation, featurePoint);
                    featureDistance.put(feature, distance);

                }
            }
        }

        if(0 == featureDistance.size()){
            return null;
        }

        // get the most 5 closest stations
        List<Feature> nearFeatures = featureDistance.entrySet().stream().sorted((Map.Entry<Feature, Double> e1, Map.Entry<Feature, Double> e2) ->  e1.getValue().compareTo(e2.getValue()))
                .map(entry -> entry.getKey()).collect(Collectors.toList());

        // maximum get 5 stations
        if(nearFeatures != null && nearFeatures.size() > 5 ){
            nearFeatures = nearFeatures.subList(0,5);
        }

        Map<Feature,Double> featureWeights = new HashMap<>();

        //calculate the EarningYield of each station
        nearFeatures.forEach(feature ->{
            double coins = feature.getProperty("coins").getAsDouble();
            double direciton = featureDistance.get(feature);
            double EarningYield = getWeight(direciton,coins);
            featureWeights.put(feature,EarningYield);
        });

        Feature nextFeature =  featureWeights.entrySet().stream().max((Map.Entry<Feature, Double> e1, Map.Entry<Feature, Double> e2) ->  e2.getValue().compareTo(e1.getValue())).map(entry -> entry.getKey()).get();
        return nextFeature;
    }


    private double getWeight(double direction,double coins){
        double stepNum = CalculateUtils.DoubleDivide(direction,Position.R);
        double stepCost = CalculateUtils.DoubleDivide(stepNum, (Game.ALL_STEP - this.step));
        double coinRatio = CalculateUtils.DoubleDivide(coins,GameMap.FEATURE_MAX_POWER);
        double EarningYield = CalculateUtils.DoubleSubtract(coinRatio,stepCost * 35) ;
        return EarningYield;

    }


}
