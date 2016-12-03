package woohoo.utils.framework;

import java.util.List;
import woohoo.utils.gameobjects.Tile;
import woohoo.utils.gameobjects.TileMap;

public class UndoState
{
	public List<Tile> state;
	public int width;
	public int height;
	
	public UndoState(List<Tile> s, int w, int h)
	{
		width = w;
		height = h;
		state = TileMap.getTileListCopy(s);
	}
}
