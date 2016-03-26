import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class AndroidTelnet {
	//start lat/long point, specify change in lat/long and number of times to change
	//loop NUMBER_OF_POINTS-1, with array of size NUMBER_OF_POINTS-1, specifying delay b/w each point
	//loop structure: update location, delay, set next point
	

    static final float START_LONGITUDE = (float) -82.343535, START_LATITUDE = (float) 29.644779;
    
    static final float DELTA_LONGITUDE = 0.000005f, DELTA_LADITUDE = 0.000000f;

    // Delays that emulate change in speed
    
    //// Full speed definition
    static final int FULL_SPEED_DELAY = 50; // ms,  should be determined by Usain Bolt    
    
    //// Full acceleration (0 -> full speed)
    static int[] fullAcceleration = new int[] {800,600,500,450,400,350,325,300,275,250,225,200,175,150,125,100,75,50};

    //// Full deceleration (full speed -> 0)
    static int[] fullDeceleration = new int[] {50,75,100,125,150,175,200,225,250,275,300,325,350,400,450,500,600,800};

	//// How long is the run?
	static final int NUMBER_OF_POINTS = fullAcceleration.length + 500 + fullDeceleration.length;
    
    public static void main(String[] args) {
    	
    	//set goal to 250 seconds, resample every 250 ms
    	//calculate value for total distance
    	//specify goal time 
    	
    	
    	//TO TEST -- 
    	//have current distance variable, 
    	//loop: set prev point var, wait 250 ms, calculate distance covered
    	//b/w curr and prev point, add to current distance, calculate what distance covered should be
    	//after x seconds, get percentage by dividing distance covered/total and should be/total,
    	//real distance covered percentage and should be percentage, multiple that percentage by 
    	//normal freq, resample at that percentage, repeat
    	
    	Socket socket = null;
        try {
            socket = new Socket("localhost", 5554); // usually 5554

            PrintStream out = new PrintStream(socket.getOutputStream());
            float longitude = START_LONGITUDE, latitude = START_LATITUDE;
            String str;

            for (int i = 0; i < NUMBER_OF_POINTS-1; i++) {
                str = "geo fix " + longitude + " " + latitude + "\n";
                out.print(str);
                System.out.print(str);

                if(i < fullAcceleration.length){
					// 0 -> 100% speed
					Thread.sleep(fullAcceleration[i]); //convert to seconds
				}else if(i >= NUMBER_OF_POINTS-fullDeceleration.length){
					// 100 -> 0% speed
					Thread.sleep(fullDeceleration[i-(NUMBER_OF_POINTS-fullDeceleration.length)]); //convert to seconds
				}else{
					// 100% speed
					Thread.sleep(FULL_SPEED_DELAY); //convert to seconds
				}


                longitude += DELTA_LONGITUDE;
                latitude += DELTA_LADITUDE;
            }
        } catch (UnknownHostException e) {
            System.exit(-1);
        } catch (IOException e) {
            System.exit(-1);
        } catch (InterruptedException e) {
            System.exit(-1);
        }
        if(socket != null) {
        	try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
