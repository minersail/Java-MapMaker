package woohoo.utils.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import woohoo.utils.gameworld.GameRenderer;

public class Tile
{
    private GridPoint2 position = new GridPoint2();
    private TextureRegion tile;
    private int textureID;
    private int functionID;
    private int rotation = 0;
    private Color color = Color.WHITE;
    private boolean isWall;
	
	/* Dimensions of tiles in-game */
    public static final int G_TILE_WIDTH = 64;
    public static final int G_TILE_HEIGHT = 64;
    
	/* Dimensions of tiles on the spritesheet */
    public static final int T_TILE_WIDTH = 16;
    public static final int T_TILE_HEIGHT = 16;
    
    public static final Texture wallOutline = new Texture("images/wallOutline.png");
    
    public Tile(int tileID, int function, int initX, int initY)
    {
		int columns = GameRenderer.tileSet.getWidth() / T_TILE_WIDTH;
		int tileX = (tileID % columns) * T_TILE_WIDTH;
		int tileY = (tileID / columns) * T_TILE_HEIGHT;
		
        textureID = tileID;
        functionID = function;
        position.x = initX;
        position.y = initY;
        
        tile = new TextureRegion(GameRenderer.tileSet, tileX, tileY, T_TILE_WIDTH, T_TILE_HEIGHT);
		rotation = 90 * (functionID / 4);
        isWall = functionID >= 4 && functionID <= 7;
		
		tile.flip(false, true);
    }
    
    public Tile(Tile t)
    {
		int columns = GameRenderer.tileSet.getWidth() / T_TILE_WIDTH;
		int tileX = (t.textureID % columns) * T_TILE_WIDTH;
		int tileY = (t.textureID / columns) * T_TILE_HEIGHT;
        
        textureID = t.textureID;
        functionID = t.functionID;
        position.x = t.position.x;
        position.y = t.position.y;       
        
        tile = new TextureRegion(GameRenderer.tileSet, tileX, tileY, T_TILE_WIDTH, T_TILE_HEIGHT);
		rotation = t.rotation;
        isWall = t.isWall;
		
		tile.flip(false, true);
    }
    
    public void draw(SpriteBatch batcher)
    {
        batcher.setColor(color);
        batcher.draw(tile, position.x * G_TILE_WIDTH, position.y * G_TILE_HEIGHT,   // Position
                     G_TILE_WIDTH / 2, G_TILE_HEIGHT / 2,                           // Origin
                     G_TILE_WIDTH, G_TILE_HEIGHT,                                   // Size
                     1, 1,                                                          // Scale
                     rotation);                                                     // Rotation
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
    
    public void replaceTexture(int tileID)
    {        
		int columns = GameRenderer.tileSet.getWidth() / T_TILE_WIDTH;
		int tileX = (tileID % columns) * T_TILE_WIDTH;
		int tileY = (tileID / columns) * T_TILE_HEIGHT;        
        
        tile.setRegion(tileX, tileY, tile.getRegionWidth(), tile.getRegionHeight());
        tile.flip(false, true);
		
		textureID = tileID;
    }
    
    public void setRotation(int rot)
    {
        rotation = rot;
		functionID = functionID / 4 * 4 + Math.abs((rot / 90) % 4);
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
