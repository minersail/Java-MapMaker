package woohoo.utils.mapmaker;

import com.badlogic.gdx.Game;
import woohoo.utils.screens.PlayingScreen;

public class MapMaker extends Game
{			
	@Override
	public void create()
	{
		setScreen(new PlayingScreen());
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
