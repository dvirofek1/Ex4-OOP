package dataStructure;

import java.io.Serializable;
import java.util.Comparator;

public class VerComperator implements Comparator<node_data> ,Serializable{
	
	public VerComperator() {;}
	@Override
	public int compare(node_data o1, node_data o2) {
			Vertex x = (Vertex)o1;
			Vertex y = (Vertex)o2;
			return (int)(x.getWeight()-y.getWeight());
	}

}