package gameClient;

import java.util.Comparator;

import game_objects.Fruit;

public class FruitComperator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Fruit f1,f2;
		if(o1 instanceof Fruit)
			f1 = (Fruit)o1;
		else
			throw new ArithmeticException();
		if(o2 instanceof Fruit)
			f2 = (Fruit)o2;
		else
			throw new ArithmeticException();
		
		return (int) (f2.getValue()-f1.getValue());
		
	}

}
