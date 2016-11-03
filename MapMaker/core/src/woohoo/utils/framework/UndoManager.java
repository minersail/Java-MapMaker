package woohoo.utils.framework;

import java.util.ArrayList;
import java.util.List;

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
        return states.remove(states.size() - 1);
	}
    
    public static int getUndosLeft()
    {
        return states.size();
    }
}
