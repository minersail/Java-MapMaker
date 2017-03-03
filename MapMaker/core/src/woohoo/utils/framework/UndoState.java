package woohoo.utils.framework;

import java.util.ArrayList;
import java.util.List;
import woohoo.utils.gameobjects.Tile;

public class UndoState
{
	public List<Tile> state;
	public int width;
	public int height;
	
	public UndoState(List<Tile> s, int w, int h)
	{
		width = w;
		height = h;
		state = new ArrayList<>();
        
        for (Tile tile : s)
        {
            Tile newTile = new Tile(tile);
            state.add(newTile);
        }
	}
}
