package uk.ac.ed.inf.powergrab;


public class App 
{
    public static void main( String[] args )
    {
        //  15 09 2019 55.944425 -3.188396  5678 stateless as an example 
        if(args.length < 7 ) {
            System.out.println("missing parameters");
        }else{
            // receive arguments to start the game
            GameParam gameParam = new GameParam();
            try {
                gameParam.setDay(args[0]);
                gameParam.setMonth(args[1]);
                gameParam.setYear(args[2]);
                gameParam.setLatitude(Double.parseDouble(args[3]));
                gameParam.setLongitude(Double.parseDouble(args[4]));
                gameParam.setSeed( Long.parseLong(args[5]));
                gameParam.setDroneType(args[6]);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("wrong parameters");
            }

            try{
                // Create the new game 
                Game game = new Game(gameParam);
                // initialise the game 
                game.init();
                // begin 
                game.play();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Interruption! game went fault");
            }
        }
    }
}
