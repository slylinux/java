package cn.com.hello;

public class world {
	
	 static{   
	       
		 try{
		   
			 String jp=System.getProperty("java.library.path"); 
			 System.out.println(jp );
	         System.loadLibrary("register"); 
	     } 
		 catch (Throwable t){	    	 
	    	 t.getStackTrace();
	    	 System.out.println("jni-exp:"+t.toString());
	     }  
		 }
	//此处定义的是native方法  
	public native  boolean product_register();  
	  
	    public static void main (String args[]) {         
	         world test = new world();  
	        boolean t=test.product_register(); 
	        System.out.println(t);
	     }  
	    
}
