package woohoo.utils.framework;

import java.util.ArrayList;
import java.util.List;
import woohoo.utils.gameobjects.TileMap;

public class UndoManager
{
	private static List<UndoState> states = new ArrayList<>();
	
	public static List<UndoState> getStates()
	{
		return states;
	}
	
	public static void add(UndoState state)
	{
		states.add(state);
	}
	
	public static UndoState getLastState()
	{
        if (states.size() == 0)
        {
            return TileMap.getCurrentState();
        }
        
        return states.remove(states.size() - 1);
	}
    
    public static int getUndosLeft()
    {
        return states.size();
    }
}
