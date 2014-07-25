
public class HelloWorldApp
{
    public native void writeFile(String data);
    public native String readFile();

    public static void main(String args[])
    {
	System.out.println("hello world,this is a Java test App");
    }
}