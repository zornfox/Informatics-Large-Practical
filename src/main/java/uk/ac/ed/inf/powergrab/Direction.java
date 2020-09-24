package uk.ac.ed.inf.powergrab;
//define all 16 directions that drones can choose
	/* 
	 *                        N
	 *                 NNW         NNE
	 *             NW                   NE
	 *        WNW                           ENE
	 *     W                                     E
	 *        WSW                           ESE  
	 *             SW                   SE
	 *                 SSW         SSE 
	 *                        S
	 */
public enum Direction {
  
	 N,       
	 NNE,
	 NE, 
     ENE, 
     
	 E, 
	 ESE, 
	 SE, 
     SSE,
     
     S, 
	 SSW,
	 SW, 
     WSW,
     
	 W,
	 WNW,
	 NW,
     NNW, 
	
}
