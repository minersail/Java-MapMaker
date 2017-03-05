package woohoo.utils.screens;

import com.badlogic.gdx.Screen;
import woohoo.utils.framework.InputHandler;
import woohoo.utils.framework.UndoManager;
import woohoo.utils.gameobjects.Tile;
import woohoo.utils.gameobjects.TileSelector;
import woohoo.utils.gameworld.GameRenderer;
import woohoo.utils.gameworld.GameWorld;

public class PlayingScreen implements Screen
{
    private float runTime;
	private final GameRenderer renderer;
	private final GameWorld world;
	private final UndoManager undoManager;
	private final InputHandler input;
	private final TileSelector selector;

    public PlayingScreen()
    {
		Tile.setTilesets();
		
		renderer = new GameRenderer(this);
		world = new GameWorld(this);
		undoManager = new UndoManager(this);
		input = new InputHandler(this);
		selector = new TileSelector(this);
    }

    @Override
    public void render(float delta)
    {
        runTime += delta;
        world.update(delta);
        renderer.render(runTime);
		selector.draw();
    }
	
	public GameRenderer getRenderer()
	{
		return renderer;
	}
	
	public GameWorld getWorld()
	{
		return world;
	}
	
	public UndoManager getUndoManager()
	{
		return undoManager;
	}
	
	public InputHandler getInput()
	{
		return input;
	}
	
	public TileSelector getSelector()
	{
		return selector;
	}

    @Override
    public void resize(int width, int height)
    {
		renderer.resize(width, height);		
		selector.resize(width, height);
    }

    @Override
    public void show()
    {
        System.out.println("GameScreen - show called");
    }

    @Override
    public void hide()
    {
        System.out.println("GameScreen - hide called");
    }

    @Override
    public void pause()
    {
        System.out.println("GameScreen - pause called");
    }

    @Override
    public void resume()
    {
        System.out.println("GameScreen - resume called");
    }

    @Override
    public void dispose()
    {

    }
}
