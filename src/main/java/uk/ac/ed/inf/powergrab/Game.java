package uk.ac.ed.inf.powergrab;

import java.io.File;

public class Game {
    public  static  final int ALL_STEP = 250 ;
    protected  GameParam  gameParam;
    protected  Drone      drone;


    public Game(GameParam gameParam){
        this.gameParam = gameParam;
    }


    public void init(){

        //initialise the map
        GameMap gameMap = new GameMap(gameParam);

        // using the arguments provided to create the Drone
        drone = DroneFactory.createDrone(gameParam,gameMap);

        if(null == drone){
            System.out.println("wrong parameters");
        }

    }
   
    public void play(){
        while (!isGameOver()){
            this.drone.flying();
        }
        String path = System.getProperty("user.dir");
        String logFilePath = String.format("%s%s%s-%s.txt",path,File.separator,gameParam.getDroneType(),gameParam.ddmmyyyy("-"));
        String mapFilePath = String.format("%s%s%s-%s.geojson",path,File.separator,gameParam.getDroneType(),gameParam.ddmmyyyy("-"));
        //export the files
        this.drone.exportLog(logFilePath);
        this.drone.getGameMap().exportMap(mapFilePath,this.drone.flyPath);
        double rate = CalculateUtils.DoubleDivide(this.drone.coin,this.drone.gameMap.getAllCoin()) * 100;// calculate the final score
        
        //print the game result and the final score
        System.out.println(String.format("GameOver! %s, %s drone, step:%d, coins:%f, MaxCoin:%f Collection_Rate:%f%%",
             gameParam.yyyymmdd("-"),gameParam.getDroneType(),drone.getStep(),drone.getCoin(),this.drone.gameMap.getAllCoin(),rate
        ));
    }


    // set the conditions to determine when the game is over 
    public boolean isGameOver(){
        if(drone.getStep() == Game.ALL_STEP || 0 == drone.getPower()){
            return true;
        }
        return false;
    }

}
