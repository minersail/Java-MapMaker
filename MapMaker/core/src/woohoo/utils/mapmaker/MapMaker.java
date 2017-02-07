package woohoo.utils.mapmaker;

import com.badlogic.gdx.Game;
import woohoo.utils.gameworld.GameRenderer;
import woohoo.utils.screens.PlayingScreen;

public class MapMaker extends Game
{		
	GameRenderer renderer;
	
	@Override
	public void create()
	{
		renderer = new GameRenderer();
		setScreen(new PlayingScreen(renderer));
	}

	@Override
	public void render()
	{
		super.render();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
}
