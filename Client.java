import java.io.*;
import java.net.*;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

class Employee implements Serializable
{
    private int employeeNumber;
    private String employeeName;

    public Employee(int employeeNumber,String employeeName)
    {
	this.employeeNumber=employeeNumber;
	this.employeeName=employeeName;
    }

    public void setEmployeeNumber(int employeeNumber)
    {
	this.employeeNumber=employeeNumber;
    }

    public void setEmployeeName(String employeeName)
    {	
	this.employeeName=employeeName;
    }
    public String getEmployeeName()
    {
	return this.employeeName;
    }
    public int getEmployeeNumber()
    {
	return this.employeeNumber;
    }

}				 

public class Client
{

    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Employee empFromClient=new Employee(20,"Mr.Right");
    Employee empFromServer=new Employee(21,"Mr.Left");
    public Client()
    {
	try
	    {
		socket=new Socket("192.168.3.35",48968);
		in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out=new PrintWriter(socket.getOutputStream(),true);
		BufferedReader line=new BufferedReader(new InputStreamReader(System.in));

		ObjectOutputStream clientOutputStream=new 
		    ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream clientInputStream=new 
		    ObjectInputStream(socket.getInputStream());

		System.out.println("please enter a message:");
		out.println(line.readLine());
		//clientOutputStream.writeObject(empFromClient);
		//empFromServer=(Employee)clientInputStream.readObject();
		//System.out.println("server echo "+empFromServer.getEmployeeName()+" Number:"
		//		   +empFromServer.getEmployeeNumber());
		System.out.println("server echo "+in.readLine());
		line.close();
		out.close();
		in.close();
		socket.close();
	    }
	catch(IOException e)
	    {
		System.out.println(e);
	    }
	// catch(ClassNotFoundException e)
	//     {
	// 	System.out.println(e);
	//     }

    }

    public static void main(String[] args)
    {
	new Client();
    }
}