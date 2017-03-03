package woohoo.utils.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import woohoo.utils.gameobjects.Tile;
import woohoo.utils.gameobjects.TileMap;
import woohoo.utils.screens.PlayingScreen;

public class InputHandler implements InputProcessor
{        
	private PlayingScreen screen;
	
    private Vector2 mouseDown;
    private boolean wallMode;
	private boolean decorationMode;
    
    public InputHandler(PlayingScreen scr)
    {
		screen = scr;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {        
		TileMap tileMap = screen.getRenderer().getMap();
		
        int dispX = (int)(screen.getRenderer().getCamera().position.x - Gdx.graphics.getWidth() / 2);
        int dispY = (int)(screen.getRenderer().getCamera().position.y - Gdx.graphics.getHeight() / 2);
        
        mouseDown = new Vector2(screenX + dispX, screenY + dispY);
        Tile tile = tileMap.selectTile(screenX + dispX, screenY + dispY);
		if (tile != null)
			tile.highlight(true);
        return false;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }
    
    public void toggleWallMode()
    {
        wallMode = !wallMode;
    }
    
    public void toggleDecorationMode()
    {
        decorationMode = !decorationMode;
    }
	
	public boolean getDecorationMode()
	{
		return decorationMode;
	}

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
		TileMap tileMap = screen.getRenderer().getMap();
		
        int dispX = (int)(screen.getRenderer().getCamera().position.x - Gdx.graphics.getWidth() / 2);
        int dispY = (int)(screen.getRenderer().getCamera().position.y - Gdx.graphics.getHeight() / 2);
        
        tileMap.deselectAll();
                
        if (wallMode)
        {
            tileMap.toggleWall(tileMap.selectTiles((int)mouseDown.x, (int)mouseDown.y, screenX + dispX, screenY + dispY));
        }
        else
        {
            tileMap.replaceAll(tileMap.selectTiles((int)mouseDown.x, (int)mouseDown.y, screenX + dispX, screenY + dispY), decorationMode);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
		TileMap tileMap = screen.getRenderer().getMap();
		
        int dispX = (int)(screen.getRenderer().getCamera().position.x - Gdx.graphics.getWidth() / 2);
        int dispY = (int)(screen.getRenderer().getCamera().position.y - Gdx.graphics.getHeight() / 2);
        
        tileMap.deselectAll();
        tileMap.highlightAll(tileMap.selectTiles((int)mouseDown.x, (int)mouseDown.y, screenX + dispX, screenY + dispY));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
