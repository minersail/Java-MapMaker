package woohoo.utils.gameobjects;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import woohoo.utils.gameworld.GameRenderer;

public class Tile
{	
    private GridPoint2 position = new GridPoint2();
    private TextureRegion tile; // Base layer
	private TextureRegion decoration; // Decorative layer, starts as null
    private int textureID; // 0 < textureID < 256, two-byte storage for tile's texture
    private int functionID; // 0 < functionID < 256, two-byte storage for tile's function
	private int decorationID; // 0 < decorationID < 256, two-byte storage for decoration's texture
    private int rotation = 0; // must be in multiples of 90
	private int decorationRotation = 0; // must be in multiples of 90
    private Color color = Color.WHITE; // Only for editor's purpose, does not show up in function/texture ids
    private boolean isWall;
	
	/* Dimensions of tiles in-game */
    public static final int G_TILE_WIDTH = 64;
    public static final int G_TILE_HEIGHT = 64;
    
	/* Dimensions of tiles on the spritesheet */
    public static final int T_TILE_WIDTH = 16;
    public static final int T_TILE_HEIGHT = 16;
    
    public static final Texture wallOutline = new Texture("images/wallOutline.png");
	public static Texture tileset1;
	public static Texture tileset2;
    
    public Tile(int decor, int tileID, int function, int initX, int initY)
    {		
		decorationID = decor % 256;
		
		int columns = tileset1.getWidth() / T_TILE_WIDTH;
		int tileX = (tileID % columns) * T_TILE_WIDTH;
		int tileY = (tileID / columns) * T_TILE_HEIGHT;
		
		int columns2 = tileset2.getWidth() / T_TILE_WIDTH;
		int tileX2 = (decorationID % columns2) * T_TILE_WIDTH;
		int tileY2 = (decorationID / columns2) * T_TILE_HEIGHT;
		
        textureID = tileID;
        functionID = function;
		decorationRotation = (decor / 256) * 90;
        position.x = initX;
        position.y = initY;
        
        tile = new TextureRegion(tileset1, tileX, tileY, T_TILE_WIDTH, T_TILE_HEIGHT);
		if (decorationID != 0)
		{
			decoration = new TextureRegion(tileset2, tileX2, tileY2, T_TILE_WIDTH, T_TILE_HEIGHT);
			decoration.flip(false, true);
		}
		rotation = 90 * (functionID % 4);
        isWall = functionID >= 4 && functionID <= 7;
		
		tile.flip(false, true);
    }
    
	// Critical copy constructor for storing undo states
    public Tile(Tile t)
    {
		int columns = tileset1.getWidth() / T_TILE_WIDTH;
		int tileX = (t.textureID % columns) * T_TILE_WIDTH;
		int tileY = (t.textureID / columns) * T_TILE_HEIGHT;
		
		int columns2 = tileset2.getWidth() / T_TILE_WIDTH;
		int tileX2 = (t.decorationID % columns2) * T_TILE_WIDTH;
		int tileY2 = (t.decorationID / columns2) * T_TILE_HEIGHT;
        
        textureID = t.textureID;
        functionID = t.functionID;
		decorationID = t.decorationID;
        position.x = t.position.x;
        position.y = t.position.y;       
        
        tile = new TextureRegion(tileset1, tileX, tileY, T_TILE_WIDTH, T_TILE_HEIGHT);
		rotation = t.rotation;
		decorationRotation = t.decorationRotation;
        isWall = t.isWall;
		
		if (t.decoration != null)
		{
			decoration = new TextureRegion(tileset2, tileX2, tileY2, T_TILE_WIDTH, T_TILE_HEIGHT);
			decoration.flip(false, true);
		}
		
		tile.flip(false, true);
    }
	
	public static void setTilesets()
	{
		FileHandle path = new FileHandle("config.txt");
		String[] paths = path.readString().split("\n");
		
		tileset1 = new Texture(paths[1].substring(0, paths[1].length() - 1));
		tileset2 = new Texture(paths[2]);
	}
    
    public void draw(SpriteBatch batcher)
    {
        batcher.setColor(color);
        batcher.draw(tile, position.x * G_TILE_WIDTH, position.y * G_TILE_HEIGHT,   // Position
                     G_TILE_WIDTH / 2, G_TILE_HEIGHT / 2,                           // Origin
                     G_TILE_WIDTH, G_TILE_HEIGHT,                                   // Size
                     1, 1,                                                          // Scale
                     rotation);                                                     // Rotation
		
		if (decoration != null)
			batcher.draw(decoration, position.x * G_TILE_WIDTH, position.y * G_TILE_HEIGHT,   // Position
                     G_TILE_WIDTH / 2, G_TILE_HEIGHT / 2,                           // Origin
                     G_TILE_WIDTH, G_TILE_HEIGHT,                                   // Size
                     1, 1,                                                          // Scale
                     decorationRotation);                                           // Rotation
		
        if (isWall)
            batcher.draw(new TextureRegion(wallOutline), position.x * G_TILE_WIDTH, position.y * G_TILE_HEIGHT);
        batcher.setColor(Color.WHITE);
    }
	
	public String getCode()
	{
		String function = Integer.toString(functionID, 16);
		String texture = Integer.toString(textureID, 16);
		
		function = (function.length() == 1 ? "0" + function : function);
		texture = (texture.length() == 1 ? "0" + texture : texture);
		
		return function + texture;
	}
	
	public String getDecorationCode()
	{
		String dec = Integer.toString(decorationID, 16);
		dec = (dec.length() == 1 ? "0" + dec : dec);
		
		int rot = (decorationRotation / 90) % 4;
		
		return "0" + rot + dec;
	}
    
    public void highlight(boolean highlight)
    {
        if (highlight)
        {
            color = new Color(0.85f, 0.64f, 0.13f, 1);
        }
        else
        {
            color = Color.WHITE;
        }
    }
    
    public void replaceTexture(int tileID, boolean isDecoration)
    {    
		if (isDecoration)
		{
			int columns = tileset2.getWidth() / T_TILE_WIDTH;
			int tileX = (tileID % columns) * T_TILE_WIDTH;
			int tileY = (tileID / columns) * T_TILE_HEIGHT;        

			// Lazy initialization
			if (decoration == null) decoration = new TextureRegion(tileset2, tileX, tileY, T_TILE_WIDTH, T_TILE_HEIGHT);
			else decoration.setRegion(tileX, tileY, decoration.getRegionWidth(), decoration.getRegionHeight());
			
			decoration.flip(false, true);

			decorationID = tileID;
		}
		else
		{
			int columns = tileset1.getWidth() / T_TILE_WIDTH;
			int tileX = (tileID % columns) * T_TILE_WIDTH;
			int tileY = (tileID / columns) * T_TILE_HEIGHT;        

			tile.setRegion(tileX, tileY, tile.getRegionWidth(), tile.getRegionHeight());
			tile.flip(false, true);

			textureID = tileID;
		}
    }
    
    public void setRotation(int rot, boolean isDecoration)
    {
		while (rot < 0) rot += 360; // Ensures positive rotation
		
		if (isDecoration)
		{
			decorationRotation = rot;
		}
		else
		{	
			rotation = rot;		
			functionID = functionID / 4 * 4; // Handy integer division trick to set functionID to closest lower multiple of four
			functionID += (rot / 90) % 4;
		}
    }
    
    /*
    DeltaX and deltaY are in tile coordinates, not pixels
    
    e.g. move(1, 1) instead of move(64, 64)
    */
    public void move(int deltaX, int deltaY)
    {
        position.x += deltaX;
        position.y += deltaY;
    }
    
    public void toggleWall()
    {
		isWall = !isWall;
		
		if (isWall)
		{
			functionID += 4;
		}
		else
		{
			functionID -= 4;
		}
    }
}
