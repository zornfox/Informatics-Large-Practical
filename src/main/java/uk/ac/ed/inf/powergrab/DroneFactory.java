
package uk.ac.ed.inf.powergrab;

//generate corresponding object according to subclasses based on parameters from GameParam
public class DroneFactory {
	public static Drone createDrone(GameParam gameParam,GameMap gameMap){
		Drone drone=null;
		switch (gameParam.getDroneType()) {
			case "stateless":
				drone = new StatelessDrone(gameParam.getPosition(),gameMap);
				break;
			case "stateful":
				drone = new StatefulDrone(gameParam.getPosition(),gameMap);
				break;
			default:
		}
		return drone;
	}
}
