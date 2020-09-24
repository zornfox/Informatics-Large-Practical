package uk.ac.ed.inf.powergrab;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import java.util.*;
import java.util.stream.Collectors;



public class StatelessDrone extends Drone{

    public StatelessDrone(Position position,GameMap gameMap){
        super(position,gameMap);
    }

    @Override
    protected  Direction nextDirection(){
        List<Direction> directions = new ArrayList<>();
        Map<Direction,Double> directionFeatures = nextDirections();
        // avoid negative station
        directionFeatures.entrySet().removeIf(v -> v.getValue() < 0);
        Map<Direction,Double> directionFeatureMap = directionFeatures.entrySet().stream().filter(v-> v.getValue() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        // if there is a station with positive coin
        if(directionFeatureMap.size() > 0) {
            List<Map.Entry<Direction, Double>> orderMap = new ArrayList<Map.Entry<Direction, Double>>(directionFeatureMap.entrySet());
            Collections.sort(orderMap, new Comparator<Map.Entry<Direction, Double>>() {
                @Override
                public int compare(Map.Entry<Direction, Double> o1, Map.Entry<Direction, Double> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            return orderMap.get(0).getKey();
        }else{
        	//otherwise, executing random moving
            directions  = new ArrayList<>(directionFeatures.keySet());
            int random = (int)(0 + Math.random()*(directions.size()));
            return directions.get(random);
        }
    }

}
