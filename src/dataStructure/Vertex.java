package dataStructure;


import java.io.Serializable;


import utils.Point3D;
import algorithms.*;
public class Vertex implements node_data,Serializable
{

	private int tag;
	private  int key ;
	private Point3D location;
	private double weight;
	private String father;
	private static int counter =0;
	
	
	public Vertex(int key,Point3D location)
	{
		this.tag= 0;
		this.key=key;
		this.location = location;
		this.weight = Graph_Algo.INFI;
		this.father = "null";

	}
	public Vertex()
	{
		this.tag= 0;
		this.key=counter;
		counter++;
		this.location = null;
		this.weight = Graph_Algo.INFI;
		this.father = "null";
	}
	
	
	@Override
	public int getKey() {
		return key;
	}

	@Override
	public Point3D getLocation() {
		return location;
	}

	@Override
	public void setLocation(Point3D p) {
		this.location = new Point3D(p);
		
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public void setWeight(double w) {
		weight  =w;
	}

	@Override
	public String getInfo() {
		return father;
	}

	@Override
	//get info in this form   (key,weight,tag,(location))
	public void setInfo(String s) {
		father =s;
	}

	@Override
	public int getTag() {
		return tag;
	}

	@Override
	public void setTag(int t) {
		tag = t;
		
	}
	@Override
	public String toString()
	{
		return "Vertex: "+key;
	}
	public Vertex copy()
	{
		if(location!=null)
		{
			Vertex v= new Vertex(this.key,location.copy());
			v.setWeight(weight);
			v.setTag(tag);
			v.setInfo(father);
			return v;
			
		}
		else
			{
				Vertex v= new Vertex(this.key,null);
				v.setWeight(weight);
				v.setTag(tag);
				v.setInfo(father);
				return v;
			}
	}
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof node_data)
		{
			if(o instanceof Vertex)
			{
				Vertex v= (Vertex)o;
				return (v.getKey()==key);
			}
		}
		return false;
	}
}
