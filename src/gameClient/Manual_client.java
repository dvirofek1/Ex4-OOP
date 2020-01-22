package gameClient;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Vertex;
import dataStructure.edge_data;
import dataStructure.node_data;
import game_objects.Fruit;
import game_objects.GameState;
import game_objects.Robot;
import utils.MyGameGUI;
import utils.Point3D;
import utils.Range;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.*;

public class Manual_client implements Runnable {
	game_service game;
	int game_id;
	LinkedList<Fruit> fruits;
	DGraph graph;
	LinkedList<Robot> robots;
	LinkedList<edge_data> edges;
	final double EPSILON = 0.0000000001;
	HashMap<edge_data, Double> score_by_edge;
	double score;
	HashMap<edge_data, LinkedList<Fruit>> fruit_by_edge;
	MyGameGUI win;
	boolean authomatic;
	graph_algorithms gAlgo;
	boolean finishInit = false;
	KML_Logger kml;
	int robotNum;
	final int CASES = 24;
	int[] mod = new int[CASES];
	int[] allow_moves=new int[CASES];

	
	

	/**
	 * constructor for client
	 * 
	 * @param game_id
	 * @param authomatic
	 */

	public Manual_client(int game_id, boolean authomatic) {
		Game_Server.login(209373208);
		game = Game_Server.getServer(game_id);
		init_Mod_allow_moves();
		this.authomatic = authomatic;
		
		this.game_id = game_id;
		graph = new DGraph();
		robots = new LinkedList<>();
		edges = new LinkedList<>();
		gAlgo = new Graph_Algo();
		score_by_edge = new HashMap<edge_data, Double>();
		fruit_by_edge = new HashMap<>();
		kml = new KML_Logger(""+game_id);
		init();

	}
	// game.startGame()
	// game is running
	/**
	 * This method init mod array and allow_moves array for ex4
	 */
	private void init_Mod_allow_moves() {
		allow_moves[0] = 290;
		mod[0] = 30;
		allow_moves[1] = 580;
		mod[1] = 9;
		allow_moves[2] = Integer.MAX_VALUE;
		mod[2] = 2;
		allow_moves[3] = 580;
		mod[3] = 20;
		allow_moves[4] = Integer.MAX_VALUE;
		mod[4] = 2;
		allow_moves[5] = 500;
		mod[5] = 10;
		for(int i= 6; i<9;i++)
		{
			allow_moves[i] = Integer.MAX_VALUE;
			mod[i] = 1;
		}
		allow_moves[9] = 580;
		mod[9] = 10;
		allow_moves[10] = Integer.MAX_VALUE;
		mod[10] = 2;
		allow_moves[11] = 580;
		mod[11] = 10;
		allow_moves[12] = Integer.MAX_VALUE;
		mod[12] = 2;
		allow_moves[13] = 580;
		mod[13] = 10;
		allow_moves[14] = Integer.MAX_VALUE;
		mod[14] = 2;
		allow_moves[15] = Integer.MAX_VALUE;
		mod[15] = 2;
		allow_moves[16] = 290;
		mod[16] = 10;
		allow_moves[17] = Integer.MAX_VALUE;
		mod[17] = 2;
		allow_moves[18] = Integer.MAX_VALUE;
		mod[18] = 2;
		allow_moves[19] = 580;
		mod[19] = 20;
		allow_moves[20] = 290;
		mod[20] = 20;
		allow_moves[21] = Integer.MAX_VALUE;
		mod[21] = 2;
		allow_moves[22] = Integer.MAX_VALUE;
		mod[22] = 2;
		allow_moves[23] =1140;
		mod[23] = 15;
		
	}
	

	
	
	public void setGraphicWin(MyGameGUI w) {
		this.win = w;
	}

	/**
	 * init the fruits,robots,dgraph,and run the gui
	 * 
	 */

	public void init() {
		score = 0;

		// init fruits
		fruits = (LinkedList<Fruit>) update_fruits();

		// init dgraph

		try {
			initGraph(game.getGraph());
		} catch (JSONException e) {
			System.out.println("Eror with getting information from json");
		}
		gAlgo.init(graph);
		// init fruite's father
		findFruitSource(fruits);

		// init the number of robots and locate them

		try {
			robotNum = getNumOfRobots(game.toString());
			for (int i = 0; i < robotNum; i++) {
				robots.add(new Robot());
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// calculate when put the robots and locate them

		for (Robot rob : robots) {
			setSrcToRobot(rob);
			game.addRobot(rob.getSrc());
		}

		// init the robots
		List<String> r = game.getRobots();
		robots.clear();
		for (String s1 : r) {
			try {
				initRobot(s1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// draw gui
		MyGameGUI win = new MyGameGUI(graph, this);
		finishInit = true;

	}

	/**
	 * read the number of robots from JSON
	 * 
	 * @param s
	 * @return
	 * @throws JSONException
	 */

	private int getNumOfRobots(String s) throws JSONException {
		JSONObject robot1 = new JSONObject(s);
		JSONObject robot = robot1.getJSONObject("GameServer");

		int num = (Integer) robot.get("robots");
		return num;

	}

	/**
	 * set for all the fruit there match edge
	 * 
	 * @param fruits
	 */

	private void findFruitSource(List<Fruit> fruits) {
		fruit_by_edge = new HashMap<>();
		for (Fruit fruit : fruits) {
			for (edge_data edge : edges) {
				Point3D src = graph.getNode(edge.getSrc()).getLocation();
				Point3D dest = graph.getNode(edge.getDest()).getLocation();
				Point3D fruit_loc = fruit.getPos();
				double distance = Math.abs(distance(dest, src));

				// The whole distance between src->fruit->dest
				double whole_distance = Math.abs(distance(src, fruit_loc) + Math.abs(distance(fruit_loc, dest)));
				if (Math.abs(whole_distance) - distance <= EPSILON) {
					if (fruit.getType() == Fruit.HIGH_TO_LOW && edge.getSrc() > edge.getDest()
							|| fruit.getType() == Fruit.LOW_TO_HIGH && edge.getSrc() < edge.getDest())
						fruit.setEdge(edge);
				}
			}
		}

	}

	/**
	 * 
	 * @return the time that left
	 */

	public double get_timer() {
		return game.timeToEnd() / 1000;
	}

	/**
	 * 
	 * @return the current score
	 */

	public double get_score() {
		return score;
	}

	/**
	 * update the fruit list by reading JSON.
	 * 
	 * @return
	 */

	public List<Fruit> update_fruits() {
		fruit_by_edge = new HashMap<>();
		fruits = new LinkedList<>();
		for (String s : game.getFruits()) {
			Fruit f = new Fruit(null, 0, 0, null);
			f.setBelong_robot(false);
			f.initFromString(s);
			if(game_id==9 && game.timeToEnd()<8 && f.getValue()==14)
			{
				System.out.println("ok");
			}
			else
			{
				f.setBelong_robot(false);
				fruits.add(f);
			}
		}
		findFruitSource(fruits);
		update_score_by_edge();
		// findFruitSource(fruits);
		return fruits;
	}

	/**
	 * update the score by edges, insert the information in hash map
	 * 
	 */

	private void update_score_by_edge() {
		for (Fruit f : fruits) {
			edge_data f_edge = f.getEdge();
			LinkedList<Fruit> temp;
			temp = fruit_by_edge.get(f_edge);
			if (temp == null) {
				temp = new LinkedList<>();
				fruit_by_edge.put(f_edge, temp);
			}
			temp.add(f);
			double after_add = f.getValue();
			if (score_by_edge.get(f_edge) != null)
				after_add += score_by_edge.get(f_edge);
			score_by_edge.put(f_edge, after_add);
		}

	}

	/**
	 * init Dgraph from JSON
	 * 
	 * @param s
	 * @throws JSONException
	 */

	public void initGraph(String s) throws JSONException {
		JSONObject edge = new JSONObject();
		JSONObject node = new JSONObject();

		JSONObject jsonObject = new JSONObject(s);

		// read the nodes
		JSONArray jsonArray = jsonObject.getJSONArray("Nodes");
		for (int j = 0; j < jsonArray.length(); j++) {
			node = jsonArray.getJSONObject(j);
			int id = (Integer) node.get("id");
			String pos = (String) node.get("pos");
			String[] cordinates = pos.split(",");
			Point3D p = new Point3D(Double.valueOf(cordinates[0]), Double.valueOf(cordinates[1]), 0);
			graph.addNode(new Vertex(id, p));
			kml.addNodePlaceMark(p.toString());
		}

		// read the edges.
		JSONArray jsonArray1 = jsonObject.getJSONArray("Edges");
		for (int j = 0; j < jsonArray1.length(); j++) {
			edge = jsonArray1.getJSONObject(j);
			int src = (Integer) (edge.get("src"));
			double w = (Double) (edge.get("w"));
			int dest = (Integer) (edge.get("dest"));
			graph.connect(src, dest, w);
			edge_data e = new Edge(src, dest, w);
			edges.add(e);
		}

	}

	public KML_Logger getKML() {
		return kml;
	}

	/**
	 * Init robots from JSON
	 * 
	 * @param s
	 * @throws JSONException
	 */

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
		Robot cur = new Robot(dest, src, id, speed, p, value);
		synchronized (robots) {
			robots.add(cur);
		}

	}

	/**
	 * return the distance between two Point3D
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */

	private double distance(Point3D p1, Point3D p2) {
		double x1 = p1.x();
		double y1 = p1.y();
		double x2 = p2.x();
		double y2 = p2.y();
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	/**
	 * set the first position to robot
	 * 
	 * @param r
	 */

	private void setSrcToRobot(Robot r) {
		// Init the collection, need to be changed every step...
		LinkedList<Fruit> fruitsAlgo = null;
		synchronized (fruits) {
			fruitsAlgo = (LinkedList<Fruit>) update_fruits();
		}
		score_by_edge = new HashMap<>();
		fruit_by_edge = new HashMap<>();

		findFruitSource(fruitsAlgo);
		fruitsAlgo.sort(new FruitComperator());
		for (Robot rob : robots) {
			if (fruitsAlgo.size() == 0)
				rob.setDest(-1);
			else {
				// Find the max score on edges
				edge_data max_e = null;
				double max_score = 0;

				for (Fruit f : fruitsAlgo) {
					edge_data f_edge = f.getEdge();
					LinkedList<Fruit> temp;
					temp = fruit_by_edge.get(f_edge);
					if (temp == null) {
						temp = new LinkedList<>();
						fruit_by_edge.put(f_edge, temp);
					}
					temp.add(f);
					double after_add = f.getValue();
					if (score_by_edge.get(f_edge) != null)
						after_add += score_by_edge.get(f_edge);
					score_by_edge.put(f_edge, after_add);
					if (max_score < after_add) {
						max_score = after_add;
						max_e = f_edge;

					}
				}
				//System.out.println(max_e.getSrc());

				rob.setSrc(max_e.getSrc());
				Iterator<Fruit> f = fruitsAlgo.iterator();
				while (f.hasNext()) {
					Fruit fru = f.next();
					if (fru.getEdge() == max_e)
						f.remove();
				}

			}

		}

	}

	/**
	 * the algorithm for moving robots
	 * calculate by a formula dependence on distance, and value.
	 */

	private void setDestToAllRobots() {
		LinkedList<Fruit> fruitsAlgo = new LinkedList<>();
			for (Fruit f : fruits) {
				fruitsAlgo.add(f);
			}
		
		fruitsAlgo.sort(new FruitComperator());


		for (Robot robot : robots) {
			boolean found = false; 
			int j_op_b = 0;
			Fruit f_op_b = null;
			double plan_b_max_val = 0;
			double t_precent = 0.8;
			double t_score = 0.2;
			double sum_value = 0;
			final double CONSTANT = 60;
			if(game_id==3 ||game_id==9)
			{
				t_score = 0.1;
				t_precent = 0.9;
			}
			List<node_data> optionB = null;
			int i = 0;
			while (!found) {
				if (i == fruitsAlgo.size())
					break;
				Fruit f = fruitsAlgo.get(i);
				if (!f.isBelong_robot()) {
					LinkedList<Integer> vertices = new LinkedList<>();
					vertices.add(robot.getSrc()); // root->1 before fruit->fruit
					vertices.add(f.getSrcByType());
					vertices.add(f.getDestByType());
					
					LinkedList<node_data> steps = (LinkedList<node_data>) gAlgo.shortestPath(vertices.get(0),
							vertices.get(1));

					int j = 0; // remove the root from steps
					while (robot.getSrc() == steps.get(j).getKey()) {
						j++;
						if (j == steps.size())
							break;
					}
					steps.add(graph.getNode(vertices.get(2)));
					if (steps.size() > 1) {
						double time = calcTime(steps, robot.getSpeed());
						sum_value = (CONSTANT / time) * t_precent + (t_score * f.getValue());
						if (plan_b_max_val < sum_value) {
							plan_b_max_val = sum_value;
							optionB = steps;
							j_op_b = j; //index for starting the path 
							f_op_b = f;
						}
					}
				}
				i++;

			}
			if (!found) {
				if (optionB != null) {
					node_data ver = optionB.get(j_op_b);
					robot.setDest(ver.getKey());
					f_op_b.setBelong_robot(true);
					game.chooseNextEdge(robot.getId(), robot.getDest());

					found = true;
				} else {
					robot.setDest(-1);
					game.chooseNextEdge(robot.getId(), robot.getDest());
				}
			}
		}

	}
	/**
	 * return robot list
	 * 
	 * @return
	 */

	public LinkedList<Robot> getRobots() {
		return robots;
	}

	/**
	 * return robot by id
	 * 
	 * @param id
	 * @return
	 */

	public Robot getRobot(int id) {
		for (Robot r : robots) {
			if (r.getId() == id)
				return r;
		}
		return null;
	}

	/**
	 * update robot list by reading json
	 */

	public void update_robot_list() {
		synchronized (robots) {
			robots.clear();
			List<String> r = game.getRobots();
			for (String s1 : r) {
				try {
					initRobot(s1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * update the fruit list
	 */

	public void update_fruits_list() {
		synchronized (fruits) {
			update_fruits();
		}

	}

	public game_service get_game() {
		return game;
	}

	/**
	 * move robot by id of robot and dest point
	 * 
	 * @param id
	 * @param dest
	 */

	public void moveRobot(int id, int dest) {
		Robot r = getRobot(id);
		if (r == null)
			throw new RuntimeException("Robot with id: " + id + " doesn't exist");
		if (graph.getEdge(r.getSrc(), dest) == null)
			throw new RuntimeException("There is no edge between src: " + r.getSrc() + " dest: " + dest);
		game.chooseNextEdge(id, dest);
	}

	public LinkedList<Fruit> getFruits() {
		return fruits;
	}

	/**
	 * The main thread of the game
	 */

	@Override
	public void run() {
		int count = 0;
		int countMove = 0;
		int sleep = 10; //3->5

		if(game_id==0)
			sleep=3;
		else if(game_id==9)
			sleep=10;
		else if(game_id==13)
			sleep=10;
		else if(game_id==3 )
			sleep=5;
		else if(game_id==19)
			sleep = 5;
		else if(game_id==20)
			sleep=5;
		else if(game_id==23)
			sleep = 3;
		try {
			
			game.startGame(); //9  3 16
			while (game.isRunning()) {
				if (!authomatic) {
					game.move();
					update_robot_list();
					update_fruits_list();
					String result = game.toString();
					try {
						score = readResult(result);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						setDestToAllRobots();
					
					if (count % mod[game_id] == 0 && allow_moves[game_id] >countMove) {
						game.move();
						
						update_robot_list();
						update_fruits_list();
						countMove++;
					}
					count++;
					String result = game.toString();
					try {
						score = readResult(result);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
					catch(NullPointerException e){
						
					}	
				}

			}
			kml.closeDocument();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		game.stopGame();
		
		try {
			score = readResult(game.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.print("Stage: "+game_id+" ");
		System.out.print("Moves: " + countMove+ " ");
		System.out.println("Score: "+score);
		game.sendKML(kml.getKMLStr());

	}

	private double readResult(String s) throws JSONException {
		JSONObject robot1 = new JSONObject(s);
		JSONObject robot = robot1.getJSONObject("GameServer");

		double num = (Integer) robot.get("grade");
		return num;

	}

	
	
	/**
	 * return the time for a path (in seconds)
	 * 
	 * @param path
	 * @param speed
	 * @return
	 */

	private double calcTime(List<node_data> path, double speed) {
		double time = 0;
		int i = 0;
		node_data pre = null;
		for (node_data n : path) {
			if (i != 0) {
				//double dis = distance(win.scalePoint(pre.getLocation()), win.scalePoint(n.getLocation()));
				double dis = graph.getEdge(pre.getKey(), n.getKey()).getWeight();
				time += dis / speed;
			}
			pre = n;
			i++;
		}
		return time / 100;
	}
	
	public int get_gameID()
	{
		return this.game_id;
	}
	public int get_allow_moves(int get_gameID) {
		return allow_moves[get_gameID];
	}

}
