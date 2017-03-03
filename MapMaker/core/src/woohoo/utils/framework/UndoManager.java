package woohoo.utils.framework;

import java.util.ArrayList;
import java.util.List;
import woohoo.utils.screens.PlayingScreen;

public class UndoManager
{
	private PlayingScreen screen;	
	
	private List<UndoState> states = new ArrayList<>();
	
	public UndoManager(PlayingScreen scr)
	{
		screen = scr;
	}
	
	public List<UndoState> getStates()
	{
		return states;
	}
	
	public void add(UndoState state)
	{
		states.add(state);
	}
	
	public UndoState getLastState()
	{
        if (states.isEmpty())
        {
            return screen.getRenderer().getMap().getCurrentState();
        }
        
        return states.remove(states.size() - 1);
	}
    
    public int getUndosLeft()
    {
        return states.size();
    }
}
