package woohoo.utils.gameworld;

import com.badlogic.gdx.Input.Keys;
import woohoo.utils.framework.InputHandler;

public class GameWorld
{	
	public static void update(float delta)
	{
		if (InputHandler.isKeyPressed(Keys.UP))
		{
			GameRenderer.scrollCamera(0, -5);
		}
		else if (InputHandler.isKeyPressed(Keys.DOWN))
		{
			GameRenderer.scrollCamera(0, 5);			
		}
		else if (InputHandler.isKeyPressed(Keys.LEFT))
		{			
			GameRenderer.scrollCamera(-5, 0);
		}
		else if (InputHandler.isKeyPressed(Keys.RIGHT))
		{
			GameRenderer.scrollCamera(5, 0);
		}
	}
}
