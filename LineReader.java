import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LineReader {

    public static boolean isRightOrder(int[] numbers){
	for (int i=1;i<numbers.length;i++){
	    if(numbers[i]>numbers[i-1])
		return false;
	}
	return true;
    }
    public static void flip(int[] numbers,int index){
	System.out.println("flip "+index);
	int len=numbers.length;
	int[] tmp=new int[len-index];
	for(int i=0;i<tmp.length;i++){
	    tmp[i]=numbers[len-i-1];
	}
	for(int i=0;i<len-index;i++){
	    numbers[index+i]=tmp[i];
	}
    }
    public static int rightIndex(int[] numbers,int n)
    {
	int[] tmp=new int[numbers.length];
	for(int i=0;i<numbers.length;i++){
	    tmp[i]=numbers[i];
	}
	for(int i=0;i<tmp.length;i++){
	    for(int j=1;j<tmp.length-i;j++){
		if(tmp[j-1]<tmp[j]){
		    int t=tmp[j-1];
		    tmp[j-1]=tmp[j];
		    tmp[j]=t;
		}
	    }
	}
	for(int i=0;i<tmp.length;i++){
	    if(tmp[i]==n){
		return i;
	    }
	}
	return 0;
    }
    public static int findLargest(int numbers[]){
	int num=numbers[0];
	for(int i=1;i<numbers.length;i++){
	    if(numbers[i]>num)
		num=numbers[i];
	}
	return num;
	
    }
    public static int findFlipPosition(int numbers[]){
	for(int i=0;i<numbers.length;i++){
	    int largest=findLargest(numbers);	    
	}
    }
    public static void main(String[] args){
	String line;
	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	System.out.println("please input array of numbers:");
	try{
	    line=br.readLine();
	    br.close();
	    String[] stringList=line.split(" ");
	    int[] numbers=new int[stringList.length];
	    for(int i=0;i<stringList.length;i++){
		numbers[i]=Integer.parseInt(stringList[i],10);
	    }	
	    System.out.println("largest="+findLargest(numbers));
	    while(!isRightOrder(numbers)){
		
	    }
	    // for(int i=0;i<numbers.length;i++){
	    // 	System.out.println(numbers[i]);
	    // }

	}
	catch(Exception e){
	    System.out.println(e);
	}
    }
}