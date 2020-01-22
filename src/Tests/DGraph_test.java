package Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import dataStructure.*;

public class DGraph_test 
{
	DGraph g ;

	@BeforeEach
	void init() {
		g= new DGraph();
	}
		
	@Test
public	void addNodeTest()
	{
		LinkedList<Integer> lst = new LinkedList<>();
		g=new DGraph();
		for(int i=1; i<=4;i++)
		{
			g.addNode(new Vertex(i,null));//Add vertexes 1...4
			lst.add(i);
		}
		//check if vertexes 1..4 exist
		Collection<node_data> vertexes = g.getV();
		Iterator<node_data> it = vertexes.iterator();
		
		while(it.hasNext()&&lst.size()>0)
		{
			node_data cur = it.next();
			if(lst.contains(cur.getKey()))
				lst.remove(Integer.valueOf(cur.getKey()));
			else
				fail();
		}
		assertEquals(lst.size(),0);
	}
	@Test
	public	void getNodeTest()
	{
		g=new DGraph();
		for(int i=1; i<=4;i++)
		{
			g.addNode(new Vertex(i,null));//Add vertexes 1...4
			assertNotEquals(null,g.getNode(i));
		}
		
	}
	@Test
	public void connectTest()
	{
		g=new DGraph();
		for(int i=1; i<=4;i++)
		{
			g.addNode(new Vertex(i,null));//Add vertexes 1...4
		}
		try
		{
			g.connect(1,3,4);
			g.connect(2,3,4);
			g.connect(3,3,4);
			g.connect(4,3,4);
		}
		catch(ArithmeticException e)
		{
			fail("Vertex do not exist");
		}
	}
	@Test
	public void getEdgeTest()
	{
		g= new DGraph();
		for(int i=1; i<=4;i++)
		{
			g.addNode(new Vertex(i,null));//Add vertexes 1...4
		}
		try
		{
			g.connect(1,3,4);
			g.connect(1,2,4);
			g.connect(1,4,4);
		}
		catch(ArithmeticException e)
		{
			fail("Vertex do not exist");
		}
		//check if 1,3 1,2 1,4 edges exist
		assertNotEquals(null,g.getEdge(1,3));
		assertNotEquals(null,g.getEdge(1,2));
		assertNotEquals(null,g.getEdge(1,4));
	}
	@Test
	public void getETest()
	{
		g= new DGraph();
		getEdgeTest();
		Collection<edge_data> edges = g.getE(1);
		Iterator<edge_data> it = edges.iterator();
		LinkedList<edge_data> lst = new LinkedList<>();
		lst.add(new Edge(1,3,0));//this is the neighbors of vertex 1
		lst.add(new Edge(1,2,0));
		lst.add(new Edge(1,4,0));
		
		while(it.hasNext()&&lst.size()>0)
		{
			edge_data cur = it.next();
			assertTrue(lst.contains(cur));
			lst.remove(cur);
		} 
		assertEquals(lst.size(),0);
	}
	@Test
	public void removeEdgeTest()
	{
		connectTest();
		g.removeEdge(1, 3);
		//check if the edge 1,3 exist
		assertEquals(null,g.getEdge(1,3));
		
	}
	public void removeVertexTest()
	{
		connectTest();
		g.removeNode(3);
		//check if the edge 1,3 exist
		try {
			g.getEdge(1,3);
			fail();
		}
		catch(ArithmeticException e)
		{
			assertEquals(null,g.getNode(3));
		}
		
	}
	
}
