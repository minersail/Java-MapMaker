package woohoo.utils.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import woohoo.utils.gameworld.GameRenderer;

public class Tile
{
    private GridPoint2 position = new GridPoint2();
    private TextureRegion tile;
    private int textureID;
    private int rotation = 0;
    private Color color = Color.WHITE;
	
	/* Dimensions of tiles in-game */
    public static final int G_TILE_WIDTH = 64;
    public static final int G_TILE_HEIGHT = 64;
    
	/* Dimensions of tiles on the spritesheet */
    public static final int T_TILE_WIDTH = 16;
    public static final int T_TILE_HEIGHT = 16;
    
    public Tile(int tileID, int functionID, int initX, int initY)
    {
		int columns = Gdx.graphics.getWidth() / G_TILE_WIDTH;
		int tileX = (tileID % columns) * T_TILE_WIDTH;
		int tileY = (tileID / columns) * T_TILE_HEIGHT;
		
        textureID = tileID;
        position.x = initX;
        position.y = initY;
        
        tile = new TextureRegion(GameRenderer.tileSet, tileX, tileY, T_TILE_WIDTH, T_TILE_HEIGHT);
		rotation = 90 * functionID / 16;
		
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
        batcher.setColor(Color.WHITE);
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
    }
    
    public void setRotation(int rot)
    {
        rotation = rot;
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
}