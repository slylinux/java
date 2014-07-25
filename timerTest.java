import java.io.IOException;
import java.util.Timer;

public class timerTest
{
    static class task extends java.util.TimerTask
    {
	public void run()
	{
	    System.out.println("timer");
	}
    }

    public static void main(String[] args)
    {
	Timer timer=new Timer();
	timer.schedule(new task(),1000,2000);
	while(true)
	    {
		try{
		    int key=System.in.read();	
		    if(key=='c')
			{
			    timer.cancel();
			    break;
			}
		}
		catch(IOException e)
		    {
			e.printStackTrace();
		    }
	    }
    }
}