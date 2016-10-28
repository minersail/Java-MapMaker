package woohoo.utils.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import woohoo.utils.gameobjects.Tile;
import woohoo.utils.gameobjects.TileMap;
import woohoo.utils.gameobjects.TileSelector;

public class GameRenderer
{    
    public static Texture tileSet = new Texture("images/tileset.png");
	
    static OrthographicCamera cam;
    static SpriteBatch batcher = new SpriteBatch();
    static TileMap tilemap = new TileMap();
    static TileSelector bar = new TileSelector();

    public static void init()
    {
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batcher.setProjectionMatrix(cam.combined);
    }
	
    public static void render(float runTime)
    {		
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tilemap.draw(batcher);
        bar.draw();
    }
    
    public static OrthographicCamera getCamera()
    {
        return cam;
    }
	
	public static void scrollCamera(int deltaX, int deltaY)
	{		
		int leftBound = Gdx.graphics.getWidth() / 2;
		int rightBound = TileMap.mapWidth * Tile.G_TILE_WIDTH - leftBound + 80;
		int topBound = Gdx.graphics.getHeight() / 2;
		int bottomBound = TileMap.mapHeight * Tile.G_TILE_HEIGHT - topBound + 80;
		
		if (cam.position.x < rightBound  && deltaX > 0 ||		// Moving right
			cam.position.x > leftBound   && deltaX < 0 ||		// Moving left
			cam.position.y < bottomBound && deltaY > 0 ||		// Moving down
			cam.position.y > topBound    && deltaY < 0)		// Moving up
		{
			cam.translate(deltaX, deltaY);
			cam.update();
			batcher.setProjectionMatrix(cam.combined);
		}
	}
}
