package game_objects;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;

/**
 * This class represent a robot, robot contains:
 * destination- the destination vertex to move
 * src- the current vertex
 * id- the id of the robot
 * speed- speed of robot
 * pos- the position of the robot
 * @author ofekroz
 *
 */

public class Robot
{
	/** getters and setters
	 * 
	 * @return
	 */
	
	
	public int getDest() {
		return dest;
	}
	public int getSrc() {
		return src;
	}
	public int getId() {
		return id;
	}
	public double getSpeed() {
		return speed;
	}
	public Point3D getPos() {
		return pos;
	}
	public void setDest(int dest) {
		this.dest = dest;
	}
	public void setSrc(int src) {
		this.src = src;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public void setPos(Point3D pos) {
		this.pos = pos;
	}

	public Robot()
	{
		dest = -1;
		src = -1;
		id=0;
		speed=0;
		pos = Point3D.ORIGIN;
		value=0;
	}
	public Robot(int dest, int src, int id, double speed, Point3D pos,double value) {
		super();
		this.dest = dest;
		this.src = src;
		this.id = id;
		this.speed = speed;
		this.pos = pos;
		this.value=value;
		this.belong_fruits=false;
	}
	public boolean isBelong_fruits() {
		return belong_fruits;
	}
	public void setBelong_fruits(boolean belong_fruits) {
		this.belong_fruits = belong_fruits;
	}
	int dest;
	int src;
	int id;
	double speed;
	Point3D pos;
	double value;
	boolean belong_fruits;
	
	public void initRobot(String s) throws JSONException {
		JSONObject robot1 = new JSONObject(s);
		JSONObject robot = robot1.getJSONObject("Robot");

		// read the robot

		int id = (Integer) robot.get("id");
		String pos = (String) robot.get("pos");
		String[] cordinates = pos.split(",");
		Point3D p = new Point3D(Double.valueOf(cordinates[0]), Double.valueOf(cordinates[1]), 0);
		double value = (Double) robot.get("value");
		double speed = (Double) robot.get("speed");
		int dest = (Integer) robot.get("dest");
		int src = (Integer) robot.get("src");
		this.id = id;
		this.pos = p;
		this.value=value;
		this.speed=speed;
		this.dest=dest;
		this.src=src;

	}
	
	
}
