package uk.ac.ed.inf.powergrab;

public class Position {
    public double latitude;
    public double longitude;
    public static final double R = 0.0003;// set the fixed value of each movement of the drone 
    
    public Position(double latitude, double longitude) {
    	this.latitude = latitude;
    	this.longitude = longitude;
    }
    
    public Position nextPosition(Direction direction) {
    	Position p = new Position(latitude, longitude); //Initialise the position before moving
    	
    	switch(direction) {
    	case N:
    		p.latitude = latitude + R;
    		break;
    	case NNE:
    		p.latitude = latitude + R * Math.sin(Math.toRadians(67.5));
    		p.longitude = longitude + R * Math.cos(Math.toRadians(67.5));
    		break;
    	case NE:
    		p.latitude = latitude + R * Math.sin(Math.toRadians(45));
    		p.longitude = longitude + R * Math.cos(Math.toRadians(45));
    		break;
    	case ENE:
    		p.latitude = latitude + R * Math.sin(Math.toRadians(22.5));
    		p.longitude = longitude + R * Math.cos(Math.toRadians(22.5));
    	    break;
    	
    	    
    	case E:
    		p.longitude = longitude + R;
    	    break;
    	case ESE:
    		p.latitude = latitude - R * Math.sin(Math.toRadians(22.5));
    		p.longitude = longitude + R * Math.cos(Math.toRadians(22.5));
    		break;
    	case SE:
    		p.latitude = latitude - R * Math.sin(Math.toRadians(45));
    		p.longitude = longitude + R * Math.cos(Math.toRadians(45));
    		break;
    	case SSE:
    		p.latitude = latitude - R * Math.sin(Math.toRadians(67.5));
    		p.longitude = longitude + R * Math.cos(Math.toRadians(67.5));
    		break;
    	
    	
    	case S:
    		p.latitude = latitude - R;
    		break;
    	case SSW:
    		p.latitude = latitude - R * Math.sin(Math.toRadians(67.5));
    		p.longitude = longitude - R * Math.cos(Math.toRadians(67.5));
    		break;
    	case SW:
    		p.latitude = latitude - R * Math.sin(Math.toRadians(45));
    		p.longitude = longitude - R * Math.cos(Math.toRadians(45));
    		break;
    	case WSW:
    		p.latitude = latitude - R * Math.sin(Math.toRadians(22.5));
    		p.longitude = longitude - R * Math.cos(Math.toRadians(22.5));
    		break;
    	
    	
    	case W:
    		p.longitude = longitude - R;
    		break;
    	case WNW:
    		p.latitude = latitude + R * Math.sin(Math.toRadians(22.5));
    		p.longitude = longitude - R * Math.cos(Math.toRadians(22.5));
    		break;
    	case NW:
    		p.latitude = latitude + R * Math.sin(Math.toRadians(45));
    		p.longitude = longitude - R *Math.cos(Math.toRadians(45));
    		break;
    	case NNW:
    		p.latitude = latitude + R * Math.sin(Math.toRadians(67.5));
    		p.longitude = longitude - R * Math.cos(Math.toRadians(67.5));
    		break;
    	default:
    		break;
    	}

    	return p;    
    }
    
    public boolean inPlayArea() {  
    	return isValidLatitude() && isValidLongitude();
    }

    public boolean isValidLatitude() {
    	return 55.942617 < this.latitude && this.latitude < 55.946233;
    }
    public boolean isValidLongitude() {
    	return -3.192473 < this.longitude && this.longitude < -3.184319;
    }



}
