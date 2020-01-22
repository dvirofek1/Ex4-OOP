package dataStructure;
 
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

 

public class DGraph implements graph ,Serializable{
    HashMap<Integer,node_data> verMap;
    HashMap<Tuple,edge_data> edges;
    HashMap<Integer,HashMap<Integer,edge_data>> edgesByVer;
    int mc;

   
    public DGraph()
    {
        verMap = new HashMap<Integer,node_data>();
         edges = new HashMap<Tuple,edge_data>();
         edgesByVer = new HashMap<Integer,HashMap<Integer,edge_data>>();
         mc = 0;
    }
   
    @Override
    //return node if exist, else return null
    public node_data getNode(int key) {
        node_data curVer =verMap.get(key);
        //if(curVer==null)
            //throw new Exception("There is no vertex id:"+key+" in the grapgh");
        return curVer;
    }
    @Override
    //return edge if exist, else return null
    public edge_data getEdge(int src, int dest) {
        edge_data e =edges.get(new Tuple(src,dest));
        return e;
    }
 
   
	@Override
    //add node to map if the node do not exist in the map
    public void addNode(node_data n) 
    {
    	if(verMap.get(n)!=null)
			System.out.println("Cannot add vertex to the graph, vertex "+n.getKey()+" already exist!");
    	else
    	{
	        verMap.put(n.getKey(),n);
	        mc++;
        }
    }
 
    @Override
    //connect between 2 nodes , if the nodes are connected or do not exist, throw aritmetic exception
    public void connect(int src, int dest, double w) {
        node_data source = verMap.get(src);
        node_data des = verMap.get(dest);

        if(des!=null&&source!=null&&verMap.get(src)!=null&&verMap.get(dest)!=null)
        {
            Edge edge = new Edge(src,dest,w);
            edges.put(new Tuple(src,dest),edge);
            edgesByVer.put(src, edgesByVer.get(src));
            if(edgesByVer.get(src)==null)
            {
                HashMap<Integer,edge_data> temp=new HashMap<Integer,edge_data>();
                temp.put(dest, edge);
                edgesByVer.put(src,temp);
            }
            else
            {
                edgesByVer.get(src).put(dest, edge);
            }
        }
        else
        {
        	throw new ArithmeticException(); //vertex do not exist
        }
        mc++;
    }
 
    @Override
    public Collection<node_data> getV() {
        return verMap.values();
    }
 
    @Override
    public Collection<edge_data> getE(int node_id) {
        return edgesByVer.get(node_id).values();
    }
    
    //remove nodes from the map
    @Override
    public  node_data removeNode(int key) {
        //remove the edges
         Iterator it = verMap.entrySet().iterator();
            while (it.hasNext()) { //O(n)
                Map.Entry pair = (Map.Entry)it.next();
                Tuple cur= new Tuple(key,(int)pair.getKey());
                Tuple cur1= new Tuple((int)pair.getKey(),key);
                if(edges.get(cur)!=null)
                    {  
                    removeEdge(key,(int)pair.getKey());
                    }
                if(edges.get(cur1)!=null)
                {
                    removeEdge((int)pair.getKey(),key);
                }
            }
            node_data remove =verMap.remove(key); //remove from ver map
            //remove from the list
            mc++;
            return remove;
    }
    //remove edge if exist, else throw arithmetic exception
    @Override
    public edge_data removeEdge(int src, int dest) {
    	try {
        edge_data removed = edges.remove(new Tuple(src,dest));//del from map<src,dest>
        edgesByVer.get(src).remove(dest);
        mc++;
        return removed;
    	}
    	catch(NullPointerException exception)
    	{
    		throw new ArithmeticException("Edge do not exist");
    	}
    }
 
    @Override
    public int nodeSize() {
        return verMap.size();
    }
 
    @Override
    public int edgeSize() {
        return edges.size();
    }
 
    @Override
    public int getMC() {
        return mc;
    }
    @Override
    public String toString()
    {
        return "The vertexes are: "+verMap.values().toString() +"\n The edges are: "+edges.values().toString();
    }

 
}