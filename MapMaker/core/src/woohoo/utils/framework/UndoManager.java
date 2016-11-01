package woohoo.utils.framework;

import java.util.List;

public class UndoManager
{
	private static List<UndoState> states;
	
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
		return states.get(states.size() - 1);
	}
}
