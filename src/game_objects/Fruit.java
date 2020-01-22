package game_objects;


import dataStructure.edge_data;
import gameClient.game_obj;
import utils.Point3D;

/**
 * This class represent a fruit

 * @author ofekroz
 *
 */


public class Fruit implements game_obj
{
	static int cur_id =0;
	int id;
	Point3D pos;
	double value;
	int type;
	edge_data edge;
	final int VALUE=18;//the index of value in json
	public final static int LOW_TO_HIGH=1;
	public final static int HIGH_TO_LOW=-1;
	boolean belong_robot;
	

	public Fruit(Point3D pos, double value, int type, edge_data edge) {
		super();
		this.id = cur_id;
		cur_id++;
		this.pos = pos;
		this.value = value;
		this.type = type;
		belong_robot = false;
		this.edge = edge;
	}
	
	public Fruit()
	{
		
	}
	
	/**
	 * getters and setters
	 * @return
	 */

	public boolean isBelong_robot() {
		return belong_robot;
	}

	public void setBelong_robot(boolean belong_robot) {
		this.belong_robot = belong_robot;
	}

	public Point3D getPos() {
		return pos;
	}

	public double getValue() {
		return value;
	}

	public int getType() {
		return type;
	}

	public edge_data getEdge() {
		return edge;
	}
	/**
	 * init from string to fruit object
	 */
	@Override
	public void initFromString(String s) {
		double value=0;
		int type=0;
		double x=0, y=0;
		Point3D pos=null;
		String current = "";
		boolean end = false;
		int mone= 0;
		for(int i =VALUE; i<s.length()&&!end;i++)
		{
			if(s.charAt(i)!=',')
				current+=s.charAt(i);
			else
			{
				switch(mone) {
				case 0:
					value = Double.valueOf(current);
					i+=7; //the space between value to type
					mone++;
					break;
				case 1:
					type = Integer.valueOf(current);
					i+=7; //the space between type to x_pos
					mone++;
					break;
				case 2:
					x = Double.valueOf(current);
					mone++;
					break;
				default:
					y = Double.valueOf(current);
					 end = true;
					break;}
				current="";
			}
		}
		pos= new Point3D(x,y);
		this.pos=pos;
		this.value=value;
		this.type=type;
		
	}

	public void setEdge(edge_data edge2) {
		this.edge=edge2;
		
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getSrcByType()
	{
		if(type == -1)
			return edge.getSrc();
		return edge.getDest();
	}
	public int getDestByType()
	{
		if(type == -1)
			return edge.getDest();
		return edge.getSrc();
	}
	
	public void setPos(Point3D p)
	{
		this.pos=p;
	}
	
	@Override
	public int hashCode()
	{
		return id;
	}
	@Override public boolean equals(Object o)
	{
		if(o instanceof Fruit)
			return ((Fruit)o).id == id;
		return false;
	}
	

}
