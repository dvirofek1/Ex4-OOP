package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Vertex;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import gameClient.Manual_client;
import gameClient.SimpleDB;
import gameClient.StartClass;
import game_objects.Fruit;
import game_objects.GameState;
import game_objects.Robot;

public class MyGameGUI extends JFrame implements ActionListener,MouseListener,KeyListener, Runnable
{
	
	Color[] robColors= new Color[5];
	Manual_client client;
	graph g;
	graph_algorithms gAlgo;
	Range[] range = new Range[2];
	int id_robot=-1;
	int node = -1;
	private Graphics offScreenGraphicsDrawed = null;
	private Image offScreenImageDrawed = null;
	
	final int SIZEOFROBOT = 12;
	final int SIZEOFNODE = 7;
	final int APPLE = 1;
	int counter =30;

	
	
	
	/**
	 * 
	 * @param g the graph
	 * 
	 */

	
	public MyGameGUI(graph g,Manual_client client)
	{
		robColors[0] = Color.cyan;
		robColors[1] = Color.gray;
		robColors[2] = Color.MAGENTA;
		robColors[3] = Color.YELLOW;
		robColors[4] = Color.ORANGE;
		Thread t2 = new Thread(this);
		t2.start();
		this.client = client;
		client.setGraphicWin(this);
		Thread t = new Thread(client);
		t.start();
		this.g=g;
		gAlgo = new Graph_Algo();
		gAlgo.init(g); 
		//locations = new HashMap<Integer,Point3D>();
		range = range();
		initGui();
		
	}
	
	/**
	 * gui settings
	 */
	
	private void initGui() {
		//init win
		this.setSize(1300, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.addMouseListener(this);
	
	}
	
	/**
	 * Draw the graph, robots, fruit... using double bufferedImage
	 */

	public void paint(Graphics g)
	{
		try {
        final Dimension d = getSize();
        if (offScreenImageDrawed == null) {   
            // Double-buffer: clear the offscreen image.                
            offScreenImageDrawed = createImage(this.getWidth(), this.getHeight());   
        }          
        offScreenGraphicsDrawed = offScreenImageDrawed.getGraphics();      
        offScreenGraphicsDrawed.setColor(Color.white);
        offScreenGraphicsDrawed.fillRect(0, 0, this.getWidth(), this.getHeight()) ;                           
        /////////////////////
        // Paint Offscreen //
        /////////////////////
        paintComponent((Graphics2D) offScreenImageDrawed.getGraphics());
        g.drawImage(offScreenImageDrawed, 0, 0, null);
		}
		catch(Exception e){
			
		}
	
	}


	public void draw()
	{
		repaint();
	}
	
	/**
	 * This is really the paintings
	 * @param g2d
	 * 
	 */

	protected void paintComponent(Graphics2D g2d) 
	{
	    //paint using g2d ...
		List<Robot> robots= null;
		
	    robots = client.getRobots();
		
		
		
		g2d.setFont(new Font("default", Font.BOLD, 20));
		g2d.setColor(Color.RED);
		g2d.drawString("Time to end "+client.get_timer(), this.getWidth()/2, 100);
		g2d.drawString("Score "+client.get_score(), this.getWidth()/4, 100);
		g2d.setFont(new Font("default", Font.BOLD, 12));
		List<node_data> v = new ArrayList<node_data>(g.getV());
		for (node_data ver : v) {
			//draw vertex

			Point3D p = new Point3D(ver.getLocation().x(),ver.getLocation().y());
			p = scalePoint(p);
			g2d.setColor(Color.blue);
			
           g2d.drawOval(p.ix()-2,p.iy(),2,2);
			//graphics.fillOval(p.ix(), p.iy(), 7, 7);
			g2d.drawString(String.valueOf
					(ver.getKey()), p.ix(), p.iy()-5);
			//draw edges from this vertex
			Collection<edge_data> curEdge;
			 try
			 {
				 curEdge =  g.getE(ver.getKey());
			 }
			 catch(NullPointerException e)
			 {
				 continue;
			 }
			 Iterator<edge_data> it1 = curEdge.iterator();
				 while (it1.hasNext()) {
					 edge_data curE=it1.next();
						 g2d.setColor(Color.BLACK);
					 int index = v.indexOf(new Vertex(curE.getDest(),null));
					 Point3D dest = new Point3D(v.get(index).getLocation());
					 dest = scalePoint(dest);
					 g2d.drawLine(p.ix(), p.iy(),dest.ix(), dest.iy());	
					 //graphics.drawString(String.valueOf(curE.getWeight()), (int)((p.x()+dest.x())/2),(int)((p.y()+dest.y())/2));
					 //draw the direction
					 Point3D dir = p;
					 
					 
					 g2d.setColor(Color.BLUE);
					 g2d.fillOval(dir.ix(), dir.iy(), SIZEOFNODE,SIZEOFNODE);
					 
					 Point3D direction = new Point3D((int)((p.x()+dest.x())/2),(int)((p.y()+dest.y())/2));
						
					 for(int i =0; i<3;i++)
					 {
						 direction = new Point3D((int)((direction.x()+p.x())/2),(int)((direction.y()+p.y())/2));
					 }
					 g2d.setColor(Color.ORANGE);
					 g2d.fillOval(direction.ix(), direction.iy(), SIZEOFNODE, SIZEOFNODE);
			}
				 //draw fruits:
				 List<Fruit> fruits = null;
				 synchronized(client.getFruits())
				 {
				 fruits= client.getFruits();
				 }
				 for (Fruit f : fruits) {
					Point3D loc = scalePoint(f.getPos());
					String type = "banana";
					g2d.setColor(Color.yellow);
					if(f.getType()==APPLE)
					{
						type="apple";
						g2d.setColor(Color.green);  
					}
					
			  
			          Shape fruit= new Arc2D.Double(loc.x(),loc.y()-10,20,20,0,360,Arc2D.CHORD);
			          g2d.fill(fruit);
			          g2d.setColor(Color.cyan);
			          g2d.drawString(String.valueOf(f.getValue()), loc.ix(), loc.iy());
			          if(counter ==0)
			          client.getKML().addFruitPlaceMark(type, f.getPos().toString());
					
				}

				 int i=0;
				 //Draw robots
				 List<Robot> robots_lst=null;
				 synchronized (client.getRobots())
					{
					 robots_lst = client.getRobots();
					}
					 //System.out.println("manual.getRobots().size(): " +client.getRobots().size());
				 for(Robot r: robots)
				 {
					 g2d.setColor(robColors[i%5]);
					 Point3D loc = scalePoint(r.getPos());
					 g2d.setColor(Color.black);
					 Shape dot= new Arc2D.Double(loc.x(),loc.y(),SIZEOFROBOT,SIZEOFROBOT,0,360,Arc2D.CHORD);
			            g2d.fill(dot);
			            if(counter ==0)
			            client.getKML().addRobotPlaceMark(r.getPos().toString());
			         i++;

				 }
				 if(counter ==0)
					 counter=30;
				 else
					 counter--;
					}
				 
		g2d.setColor(Color.BLACK); 	
			
		}
		
	/**
	 * export kml to db
	 */
	
	
	private void exportKML() {
		try {
			int reply = JOptionPane.showConfirmDialog(null, "You want to export KML log?", "Export kml log",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * setter to graph
	 * @param g
	 */
	
	public void setGraph(graph g)
	{
		this.g=g;
		gAlgo.init(g);
		repaint();
	}

	/**
	 * mouse click event
	 */
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		Point p = arg0.getLocationOnScreen();
		//check if its robot or node
		
	
		if(id_robot==-1)
		{
			LinkedList<Robot> rob = client.getRobots();
			for (Robot robot : rob) {
				Point3D rob_pos = scalePoint(robot.getPos());
				Range x_r = new Range(rob_pos.x()-SIZEOFROBOT,rob_pos.x()+SIZEOFROBOT);
				Range y_r = new Range(rob_pos.y()-SIZEOFROBOT,rob_pos.y()+SIZEOFROBOT);
				if(p.x<=x_r.get_max()&&p.x>=x_r.get_min() && p.y<=y_r.get_max()&&p.y>=y_r.get_min())
				{
					System.out.println("Choose "+robot.getId());
					id_robot = robot.getId();
					break;
				}
				
			}
		}
		else
		{
			Robot curRob = client.getRobot(id_robot);
			
			//find the node
			for (edge_data edge : g.getE(curRob.getSrc())) {
				Point3D node_pos = scalePoint(g.getNode(edge.getDest()).getLocation());
				Range x_r = new Range(node_pos.x()-SIZEOFNODE,node_pos.x()+SIZEOFNODE);
				Range y_r = new Range(node_pos.y()-SIZEOFNODE,node_pos.y()+SIZEOFNODE);
				if(p.x<=x_r.get_max()&&p.x>=x_r.get_min() && p.y<=y_r.get_max()&&p.y>=y_r.get_min())
				{
					System.out.println("Find node: "+edge.getDest());
					node = edge.getDest();
					break;
				}
			}
			
			if(id_robot !=-1&& node!=-1)
			{
				int src,dest;
				src= id_robot;
				dest = node;
				id_robot=-1;
				node = -1;
				System.out.println("move " +src+" to "+dest);
				client.moveRobot(src, dest);
			}
		}

		
	}

	

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * calculate the range of the window, updates the result in range array
	 * @return
	 */
	
	private Range[] range()
	{
		double min_x=Double.MAX_VALUE,max_x=Double.MIN_VALUE,min_y=Double.MAX_VALUE,max_y=Double.MIN_VALUE;
		for (node_data v : g.getV()) {
			if(v.getLocation().x()>max_x)
			{
				max_x = v.getLocation().x();
			}
			if(v.getLocation().x()<min_x)
			{
				min_x = v.getLocation().x();
			}
			if(v.getLocation().y()>max_y)
			{
				max_y = v.getLocation().y();
			}
			if(v.getLocation().y()<min_y)
			{
				min_y = v.getLocation().y();
			}
		}
		Range[] range = new Range[2];
		range[0] = new Range(min_x,max_x);
		range[1] = new Range(min_y,max_y);
		return range;
		
	}
	
	/**
	 * return a point in the form scale
	 * @param p
	 * @return
	 */
	
	public Point3D scalePoint(Point3D p)
	{
		try {
		if(p != null)
		{
		Point3D temp = new Point3D(scale(p.x(),range[0].get_min(),range[0].get_max(),50,this.getWidth()-50),scale(p.y(),range[1].get_min(),range[1].get_max(),250,this.getHeight()-70));
		return temp;
		}
		return null;
		}
		
		catch(NullPointerException e)
		{
			return null;
		}
	}
	private double scale(double data, double r_min, double r_max, 
			double t_min, double t_max)
	{
		
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		}
		
	

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * main thread of graphics
	 */
	@Override
	public void run() {
		while(true)
		{
			repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(client.get_timer()<=0)
				break;
		}
		showResult();
		showSQL();
		StartClass s = new StartClass();
		try {
		s.main(null);
		}
		catch(NullPointerException e)
		{
			
		}
		dispose();
		
		
	}

	private void showSQL() {
		String s = "";
		s+="You played in "+client.get_gameID()+ " "+SimpleDB.numOfGameInStage(client.get_gameID())+" times \n";
		s+="You played ";
		s+= ""+SimpleDB.numOfGame() +" games \n\n";
		System.out.println(client.get_allow_moves(client.get_gameID()));
		s+="Your rank is "+SimpleDB.rankByStage_2(client.get_gameID(), (int)client.get_score(),client.get_allow_moves(client.get_gameID()));
		s += "Your highest score: \n";
		s+= SimpleDB.getBestScore();
		
		
		infoBox(s,"RESULT");
	}

	public void showResult() {
		infoBox("Your score: "+client.get_score(),"Game over");
		
	}
	private  void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(this, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
