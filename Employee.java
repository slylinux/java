import java.io.*;
import java.net.*;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Employee implements Serializable
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
}				 
