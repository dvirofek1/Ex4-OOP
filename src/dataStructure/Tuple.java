package dataStructure;

import java.io.Serializable;

public class Tuple implements Serializable{

 
	private final int k1,k2;

    public Tuple(int k1,int k2) {
        this.k1=k1;
        this.k2=k2;
    }
    public int getK1()
    {
    	return k1;
    }
    public int getK2()
    {
    	return k2;
    }
    @Override
    public boolean equals(Object t)
    {
    	if(t instanceof Tuple)
    	{
    		Tuple t1 = (Tuple)t;
    		return (k1==t1.getK1()&&k2==t1.getK2());
    	}
    	return false;
    }
   @Override
    public int hashCode() {
        int a = k1;
        int b = k2;
        int c = a+b;
        int hashCode = (a-b)+Long.valueOf((a * 31 + b) * 31 + c).hashCode();
        return hashCode;
    }
}