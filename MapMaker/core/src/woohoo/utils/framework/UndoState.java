package woohoo.utils.framework;

import java.util.List;
import woohoo.utils.gameobjects.Tile;

public class UndoState
{
	public List<Tile> state;
	public int width;
	public int height;
    public int[] shift = {0, 0};
	
	public UndoState(List<Tile> s, int w, int h)
	{
		width = w;
		height = h;
		state = s;
	}
    
    public void changeShift(int x, int y)
    {
        shift[0] = x;
        shift[1] = y;
    }
}
