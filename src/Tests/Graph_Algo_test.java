package Tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Vertex;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class Graph_Algo_test 
{
	DGraph g ;
	Graph_Algo gAlgo;
	@BeforeEach
	void init() {
		g= new DGraph();
		g.addNode(new Vertex(1,new Point3D(30,500)));
		g.addNode(new Vertex(2,new Point3D(270,80)));
		g.addNode(new Vertex(3,new Point3D(50,100)));
		g.addNode(new Vertex(4,new Point3D(250,250)));
		g.addNode(new Vertex(5,new Point3D(500,250)));
		g.addNode(new Vertex(6,new Point3D(450,550)));
		g.connect(1,3,14);
		g.connect(1,4,9);
		g.connect(1,6,7);
		g.connect(3,1,14);
		g.connect(3,2,9);
		g.connect(3,4,2);
		g.connect(4,5,11);
		g.connect(4,3,2);
		g.connect(4,6,5);
		g.connect(4,1,9);
		g.connect(6,4,10);
		g.connect(6,5,15);
		g.connect(3,2,9);
	

		
		gAlgo = new Graph_Algo();
		gAlgo.init(g);
	}
	
	@Test
	public void isConnected_test()
	{
		assertEquals(false,gAlgo.isConnected());
		//make well connected
		g.connect(5,2,6);
		g.connect(5,4,5);
		g.connect(5,6,5);
		g.connect(2,3,5);
		g.connect(2,5,5);
		gAlgo.init(g);
		assertEquals(true,gAlgo.isConnected());
	}
	@Test
	public void shortestPathTest()
	{
		List<node_data> path= gAlgo.shortestPath(1, 2);
		node_data temp = path.remove(0);
		node_data temp1 = path.remove(0);
		node_data temp2 = path.remove(0);
		node_data temp3 = path.remove(0);
		assertEquals(temp.getKey(),1);
		assertEquals(temp1.getKey(),4);
		assertEquals(temp2.getKey(),3);
		assertEquals(temp3.getKey(),2);
	}
	@Test
	public void shortestPathDisTest()
	{
		double distance = gAlgo.shortestPathDist(1, 2);
		assertEquals(20,distance,0.001);
	}
	@Test
	public void TSPTest()
	{
		List<Integer> lst = new LinkedList<>();
		lst.add(1);
		lst.add(2);
		lst.add(3);
		g.connect(5,2,6);
		g.connect(5,4,5);
		g.connect(5,6,5);
		g.connect(2,3,5);
		g.connect(2,5,5);
		List<node_data> path= gAlgo.TSP(lst);
		System.out.println(path);
		assertEquals((int)(path.remove(0)).getKey(),1);
		assertEquals((int)(path.remove(0)).getKey(),4);
		assertEquals((int)(path.remove(0)).getKey(),3);
		assertEquals((int)(path.remove(0)).getKey(),2);
		assertEquals((int)(path.remove(0)).getKey(),3);
		
	}
	@Test
	public void copyTest()
	{
		graph g_copy = gAlgo.copy();
		g.removeEdge(1,3);
		assertNotEquals(null,g_copy.getEdge(1, 3));
	}
	
	
}
