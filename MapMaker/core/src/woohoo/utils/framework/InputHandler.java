package woohoo.utils.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import woohoo.utils.gameobjects.Tile;
import woohoo.utils.gameobjects.TileMap;
import woohoo.utils.gameworld.GameRenderer;

public class InputHandler implements InputProcessor
{        
    private static Vector2 mouseDown;
    private static boolean wallMode;
	private static boolean decorationMode;
    
    public InputHandler()
    {
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {        
        int dispX = (int)(GameRenderer.getCamera().position.x - Gdx.graphics.getWidth() / 2);
        int dispY = (int)(GameRenderer.getCamera().position.y - Gdx.graphics.getHeight() / 2);
        
        mouseDown = new Vector2(screenX + dispX, screenY + dispY);
        Tile tile = TileMap.selectTile(screenX + dispX, screenY + dispY);
		if (tile != null)
			tile.highlight(true);
        return false;
    }

    @Override
    public boolean keyDown(int keycode)
    {
//        switch (keycode) {
//            case Keys.LEFT:				
//                return true;
//            case Keys.RIGHT:
//                return true;
//            case Keys.UP:
//                return true;
//            case Keys.DOWN:
//                return true;
//        }
        return false;
    }
    
    public static boolean isKeyPressed(int keycode)
    {
        return Gdx.input.isKeyPressed(keycode);
    }
    
    public static void toggleWallMode()
    {
        wallMode = !wallMode;
    }
    
    public static void toggleDecorationMode()
    {
        decorationMode = !decorationMode;
    }
	
	public static boolean getDecorationMode()
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
        int dispX = (int)(GameRenderer.getCamera().position.x - Gdx.graphics.getWidth() / 2);
        int dispY = (int)(GameRenderer.getCamera().position.y - Gdx.graphics.getHeight() / 2);
        
        TileMap.deselectAll();
                
        if (wallMode)
        {
            TileMap.toggleWall(TileMap.selectTiles((int)mouseDown.x, (int)mouseDown.y, screenX + dispX, screenY + dispY));
        }
        else
        {
            TileMap.replaceAll(TileMap.selectTiles((int)mouseDown.x, (int)mouseDown.y, screenX + dispX, screenY + dispY), decorationMode);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        int dispX = (int)(GameRenderer.getCamera().position.x - Gdx.graphics.getWidth() / 2);
        int dispY = (int)(GameRenderer.getCamera().position.y - Gdx.graphics.getHeight() / 2);
        
        TileMap.deselectAll();
        TileMap.highlightAll(TileMap.selectTiles((int)mouseDown.x, (int)mouseDown.y, screenX + dispX, screenY + dispY));
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
