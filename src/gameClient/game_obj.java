package gameClient;

import org.json.JSONException;

/** This interface represent game object(Robots,Fruits) functions **/

public interface game_obj {
	
	public void initFromString(String s) throws JSONException;

}
