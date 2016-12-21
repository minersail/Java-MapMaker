package woohoo.utils.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
import woohoo.utils.framework.UndoManager;
import woohoo.utils.framework.UndoState;

public class TileMap
{
    public static int mapHeight;
    public static int mapWidth;    
	private static List<Tile> tileList = new ArrayList<>();
	
	public TileMap()
	{
		FileHandle mapHandle = Gdx.files.internal("maps/map1.txt");
		String map = mapHandle.readString();
		
		String[] rows = map.split("\n");
		mapWidth = rows[0].length() / 5;
		mapHeight = rows.length;
		
		int i = 0;
		int j = 0;
		for (String row : rows)
		{
			String[] tiles = row.split(" ");
			for (String tile : tiles)
			{
				int function = Integer.parseInt(tile.substring(0, 2), 16);
				int texture = Integer.parseInt(tile.substring(2, 4), 16);
								
				Tile t = new Tile(texture, function, j, i);
				tileList.add(t);
				j++;
			}
			i++;
			j = 0;
		}
	}
	
	public void draw(SpriteBatch batcher)
	{
		batcher.begin();
		for (Tile tile : tileList)
		{
			tile.draw(batcher);
		}
		batcher.end();
	}
    
    public static Tile selectTile(int x, int y)
    {
        x /= Tile.G_TILE_WIDTH;
        y /= Tile.G_TILE_HEIGHT;
        
        int location = y * mapWidth + x;
        
        Tile clicked = (Tile)tileList.toArray()[location];
        return clicked;
    }
    
    public static List<Tile> selectTiles(int x1, int y1, int x2, int y2)
    {
		// Translate pixel coords to tile coords
        x1 /= Tile.G_TILE_WIDTH;
        x2 /= Tile.G_TILE_WIDTH;
        y1 /= Tile.G_TILE_HEIGHT;
        y2 /= Tile.G_TILE_HEIGHT;
        
		// Make sure no crashes due to mouse going out of screen
		if (x2 < 0)	x2 = 0;
		if (x2 >= mapWidth) x2 = mapWidth - 1;
		if (y2 < 0) y2 = 0;
		if (y2 >= mapHeight) y2 = mapHeight - 1;
		
		// Make x2 the coord farther to right and y2 the coord farther to bottom
        if (x1 > x2)
        {
            int x = x2;
            x2 = x1;
            x1 = x;
        }
        if (y1 > y2)
        {
            int y = y2;
            y2 = y1;
            y1 = y;
        }
                
        List<Integer> locations = new ArrayList<>();
        
        for (int i = y1; i <= y2; i++)
        {
            for (int j = x1; j <= x2; j++)
            {
                locations.add(i * mapWidth + j);
            }
        }
        
        List<Tile> tiles = new ArrayList<>();
        for (Integer i : locations)
        {
            tiles.add(tileList.get(i));
        }
        
        return tiles;
    }
    
    public static void deselectAll()
    {
        for (Tile tile : tileList)
        {
            tile.highlight(false);
        }
    }
    
    public static void highlightAll(List<Tile> tiles)
    {        
        for (Tile tile : tiles)
        {
            tile.highlight(true);
        }
    }
    
    public static void replaceAll(List<Tile> tiles)
    {
        UndoManager.add(new UndoState(tileList, mapWidth, mapHeight));
        
        for (Tile tile : tiles)
        {
            tile.replaceTexture(TileSelector.getCurrentID());
            tile.setRotation(TileSelector.getCurrentRotation());
        }
    }
    
    public static void toggleWall(List<Tile> tiles)
    {
        UndoManager.add(new UndoState(tileList, mapWidth, mapHeight));
        
        for (Tile tile : tiles)
        {
            tile.toggleWall();
        }
    }
    
    /*
    DeltaX and deltaY are in tile coordinates, not pixels
    
    e.g. move(1, 1) instead of move(64, 64)
    */
    public static void moveAll(int deltaX, int deltaY)
    {
        for (Tile tile : tileList)
        {
            tile.move(deltaX, deltaY);
        }
    }
    
    public static void addRow(String dir)
    {
        UndoManager.add(new UndoState(tileList, mapWidth, mapHeight));
        
        if (dir.equals("down"))
        {
            for (int i = 0; i < mapWidth; i++)
            {
                Tile t = new Tile(TileSelector.getCurrentID(), TileSelector.getCurrentRotation() / 90 * 16, i, mapHeight);
                tileList.add(t);
            }
            mapHeight++;
        }
        else if (dir.equals("up"))
        {            
            moveAll(0, 1);
            
            for (int i = 0; i < mapWidth; i++)
            {
                Tile t = new Tile(TileSelector.getCurrentID(), TileSelector.getCurrentRotation() / 90 * 16, i, 0);
				t.setRotation(TileSelector.getCurrentRotation());
                tileList.add(i, t);
            }
            mapHeight++;
        }
        else if (dir.equals("right"))
        {
            for (int i = 0; i < mapHeight; i++)
            {
                Tile t = new Tile(TileSelector.getCurrentID(), TileSelector.getCurrentRotation() / 90 * 16, mapWidth, i);
				t.setRotation(TileSelector.getCurrentRotation());
                tileList.add(mapWidth + i * (mapWidth + 1), t);
            }
            mapWidth++;
        }
        else if (dir.equals("left"))
        {
            moveAll(1, 0);
            
            for (int i = 0; i < mapHeight; i++)
            {
                Tile t = new Tile(TileSelector.getCurrentID(), TileSelector.getCurrentRotation() / 90 * 16, 0, i);
				t.setRotation(TileSelector.getCurrentRotation());
                tileList.add(i * (mapWidth + 1), t);
            }
            mapWidth++;
        }
    }
    
    public static void deleteRow(String dir)
    {
        UndoManager.add(new UndoState(tileList, mapWidth, mapHeight));
        
        if (dir.equals("down"))
        {
            for (int i = mapHeight * mapWidth - 1; i > (mapHeight - 1) * mapWidth - 1; i--)
            {
                tileList.remove(i);
            }
            mapHeight--;
        }
        else if (dir.equals("up"))
        {            
            moveAll(0, -1);
            
            for (int i = mapWidth - 1; i >= 0; i--)
            {
                tileList.remove(i);
            }
            mapHeight--;
        }
        else if (dir.equals("right"))
        {
            for (int i = mapHeight * mapWidth - 1; i > 0; i -= mapWidth)
            {
                tileList.remove(i);
            }
            mapWidth--;
        }
        else if (dir.equals("left"))
        {
            moveAll(-1, 0);
            
            for (int i = mapWidth * (mapHeight - 1); i >= 0; i -= mapWidth)
            {
                tileList.remove(i);
            }
            mapWidth--;
        }
    }
	
    public static List<String> getCodes()
    {
        List<String> codes = new ArrayList<>();

        for (Tile tile : tileList)
        {
            codes.add(tile.getCode());
        }		
        return codes;
    }
    
    public static void useUndo(UndoState undo)
    {
        tileList = getTileListCopy(undo.state);
        mapWidth = undo.width;
        mapHeight = undo.height;
    }
    
    public static UndoState getCurrentState()
    {
        return new UndoState(tileList, mapWidth, mapHeight);
    }
    
    public static List<Tile> getTileListCopy(List<Tile> list)
    {
        List<Tile> newList = new ArrayList<>();
        
        for (Tile tile : list)
        {
            Tile newTile = new Tile(tile);
            newList.add(newTile);
        }
        
        return newList;
    }
}
