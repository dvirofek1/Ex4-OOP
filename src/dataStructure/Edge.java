package dataStructure;

import java.io.Serializable;

import algorithms.Graph_Algo;

public class Edge implements edge_data,Serializable
{
	private final int src,dest;
	private int tag;
	private double weight;

	public Edge(int src,int dest,double weight)
	{
		if(weight<0)
		{
			throw new ArithmeticException("Weight cannot be negative or zero");
		}
		this.src = src;
		this.dest = dest;
		this.tag = 0;
		this.weight = weight;
	}
	public Edge()
	{
		this.src = -1;
		this.dest = -1;
		this.tag = 0;
		this.weight = Graph_Algo.INFI;
	}
	@Override
	public int getSrc() {
		return src;
	}

	@Override
	public int getDest() {
		return dest;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	//return string from the form (src,desr,weight,tag)
	public String getInfo() {
		return new String("("+src+","+dest+","+weight+","+tag+")");
	}

	@Override
	public void setInfo(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public void setTag(int t) {
		this.tag = t;
		
	}
	@Override
	public String toString()
	{
		return ("src: "+src+" dest: "+dest+" weight: "+weight+ " tag "+tag);
	}
	public Edge copy()
	{
		Edge copy =  new Edge(src,dest,weight);
		copy.setTag(tag);
		return copy;
	}
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Edge)
		{
			System.out.println("check equals");
			edge_data e = (Edge)o;
			return (e.getSrc()==src&&e.getDest()==dest);
		}
		return false;
	}
	@Override
	public int hashCode()
	{
		int a = src;
        int b = dest;
        int c = a+b;
        int hashCode = (a-b)+Long.valueOf((a * 31 + b) * 31 + c).hashCode();
        return hashCode;
	}
}
