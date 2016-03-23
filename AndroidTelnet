import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class AndroidTelnet {
	//start lat/long point, specify change in lat/long and number of times to change
	//loop NUMBER_OF_POINTS-1, with array of size NUMBER_OF_POINTS-1, specifying delay b/w each point
	//loop structure: update location, delay, set next point
	
    static final int PAUSE = 250; // ms
    static final float START_LONGITUDE = (float) -82.343535, START_LATITUDE = (float) 29.644779;
    static final int NUMBER_OF_POINTS = 1000;
    static final float DELTA_LONGITUDE = 0.000005f, DELTA_LADITUDE = 0.000005f;
    static float[] delayArr = new float[] {2,2,2,2,2,2,2,2,2};
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

                Thread.sleep(PAUSE); //convert to seconds

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
