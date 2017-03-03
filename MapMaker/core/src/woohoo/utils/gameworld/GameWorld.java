package woohoo.utils.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import woohoo.utils.screens.PlayingScreen;

public class GameWorld
{	
	PlayingScreen screen;
	
	public GameWorld(PlayingScreen scr)
	{
		screen = scr;
	}
	
	public void update(float delta)
	{
		if (Gdx.input.isKeyPressed(Keys.UP))
		{
			screen.getRenderer().scrollCamera(0, -5);
		}
		else if (Gdx.input.isKeyPressed(Keys.DOWN))
		{
			screen.getRenderer().scrollCamera(0, 5);			
		}
		else if (Gdx.input.isKeyPressed(Keys.LEFT))
		{			
			screen.getRenderer().scrollCamera(-5, 0);
		}
		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			screen.getRenderer().scrollCamera(5, 0);
		}
	}
}
