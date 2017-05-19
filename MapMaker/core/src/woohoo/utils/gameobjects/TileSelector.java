package woohoo.utils.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.List;
import woohoo.utils.gameobjects.Tile.TileFunction;
import woohoo.utils.screens.PlayingScreen;

public class TileSelector
{    
	private Texture tileSet1;
	private Texture tileSet2;
	
	private PlayingScreen screen;
	
    private final Stage uiStage;
    private final Skin skin;
    private final Table selectRow;
    private Table editRow;
    
    private final Texture left;
    private final Texture right;
    private final Texture left2;
    private final Texture right2;
	
	private final Texture wall;
	private final Texture eye;
	private final Texture reset;
	
	private final TileButton up;
	private final TileButton down;
    
    private final int MAX_ID = 255;
	private int savedID = 0; // ID of current tile
	private int savedIndex = 0; // Index of first tile
	private TileFunction function;
	
	private int SHIFT_INDEX = 2; // Location of the first select tile (because first two are arrow buttons)
    
    public TileSelector(PlayingScreen scr)
    {
		screen = scr;
		
		tileSet1 = Tile.tileset1;
		tileSet2 = Tile.tileset2;
		
        skin = new Skin(Gdx.files.internal("ui/skin.json"));
		right = new Texture("images/rightbutton.png");
		left = new Texture("images/leftbutton.png");
		right2 = new Texture("images/rightbutton2.png");
		left2 = new Texture("images/leftbutton2.png");
		
		wall = new Texture("images/wall.png");
		eye = new Texture("images/vision.png");
		reset = new Texture("images/reset.png");
		
        uiStage = new Stage(new ScreenViewport());
        selectRow = new Table();
        editRow = new Table();
        
        //======================================================================
        //======================================================================
        
        CurrentButton current = new CurrentButton(skin, new TextureRegion(tileSet1, 0, 0, 16, 16));
        current.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
				if (screen.getInput().getFunctionMode())
				{
					switch(function)
					{
						case Wall:
							function = TileFunction.Blind;
							current.switchTexture(new TextureRegion(eye));
							break;
						case Blind:
							function = TileFunction.Normal;
							current.switchTexture(new TextureRegion(reset));
							break;
						case Normal:
							function = TileFunction.Wall;
							current.switchTexture(new TextureRegion(wall));
							break;
					}
				}
				else
				{
					if (screen.getInput().getDecorationMode())
						current.setDecorationRotation(current.getDecorationRotation() + 90);					
					else
						current.setSpriteRotation(current.getSpriteRotation() + 90);
				}
            }
        });
        editRow.add(current).prefWidth(80).prefHeight(80);
        editRow.row();
        
        String[] directions = {"up", "down", "left", "right"};
        for (String direction : directions)
        {
            TileButton expandButton = new TileButton(skin, new TextureRegion(new Texture("images/expand" + direction + ".png")));
            expandButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    screen.getRenderer().getMap().addRow(direction);
                }
            });
            editRow.add(expandButton).prefWidth(80).prefHeight(80);
            editRow.row();
        }
        
        for (String direction : directions)
        {
            TileButton retractButton = new TileButton(skin, new TextureRegion(new Texture("images/retract" + direction + ".png")));
            retractButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    screen.getRenderer().getMap().deleteRow(direction);
                }
            });
            editRow.add(retractButton).prefWidth(80).prefHeight(80);
            editRow.row();
        }
        
        TileButton functionButton = new TileButton(skin, new TextureRegion(new Texture("images/functionbutton.png")));
        functionButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screen.getInput().toggleFunctionMode();
				current.toggleFunctionMode();
				function = TileFunction.Wall;
				
				if (screen.getInput().getFunctionMode())
				{
					functionButton.setColor(Color.GOLD);
					current.switchTexture(new TextureRegion(wall));
				}
				else
				{
					functionButton.setColor(Color.WHITE);
					current.switchTexture(screen.getInput().getDecorationMode() ? tileSet2 : tileSet1, savedID);
				}
            }
        });
        editRow.add(functionButton).prefWidth(80).prefHeight(80);
        editRow.row();
		
		TileButton layer = new TileButton(skin, new TextureRegion(new Texture("images/switchlayer.png")));
        layer.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
				layer.setColor(screen.getInput().getDecorationMode() ? Color.WHITE : Color.GOLD); // ffd700ff is gold rgb
				switchTexture(screen.getInput().getDecorationMode() ? tileSet1 : tileSet2);
				screen.getInput().toggleDecorationMode();
				current.toggleDecorationMode();
            }
        });
        editRow.add(layer).prefWidth(80).prefHeight(80);
        editRow.row();
		
		TileButton undo = new TileButton(skin, new TextureRegion(new Texture("images/undo.png")));
        undo.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                screen.getRenderer().getMap().useUndo(screen.getUndoManager().getLastState());
            }
        });
        editRow.add(undo).prefWidth(80).prefHeight(80);
        editRow.row();
		
		TileButton export = new TileButton(skin, new TextureRegion(new Texture("images/export.png")));
        export.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.input.getTextInput(new TextInputListener()
				{					
					@Override
					public void input(String text)
					{
						if (text.equals(""))
							return;
						
						List<String> codes = screen.getRenderer().getMap().getCodes();
						FileHandle file = Gdx.files.local("maps/" + text + ".txt");
						
						if (file.exists())
						{		
							Gdx.input.getTextInput(new TextInputListener()
							{
								@Override
								public void input(String text)
								{
									if (text.toLowerCase().equals("yes"))
									{
										file.delete();
										writeCodes(codes, file);
									}
								}
								
								@Override public void canceled(){}
							}, "Overwrite?", "", "Yes to overwrite, no to try again");
						}
						else
						{						
							writeCodes(codes, file);
						}
					}

					@Override public void canceled(){}
				}, "Choose a name for this file", "", ".txt will automatically be appended");
            }
        });
        editRow.add(export).prefWidth(80).prefHeight(80);
        editRow.row();
        
        editRow.setWidth(80);
        editRow.setHeight(editRow.getCells().size * 80);
        
        //======================================================================
        //======================================================================
                
        selectRow.setWidth(880);
        selectRow.setHeight(80);
		
		TileButton leftbutton2 = new TileButton(skin, new TextureRegion(left2));
        leftbutton2.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                shift(false, 7);
            }
        });
        selectRow.add(leftbutton2).prefWidth(80).prefHeight(80);
        
        TileButton leftbutton = new TileButton(skin, new TextureRegion(left));
        leftbutton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                shift(false, 1);
            }
        });
        selectRow.add(leftbutton).prefWidth(80).prefHeight(80);

        for (int i = 0; i < 7; i++)
        {
            TileButton button = new TileButton(skin, new TextureRegion(tileSet1, i * 16, 0, 16, 16));

            button.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
					if (!screen.getInput().getFunctionMode())
						current.setTextureID(button.getTextureID());
                }
            });
            
            selectRow.add(button).prefWidth(80).prefHeight(80);
        }
        
        TileButton rightbutton = new TileButton(skin, new TextureRegion(right));
        rightbutton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                shift(true, 1);
            }
        });
        selectRow.add(rightbutton).prefWidth(80).prefHeight(80);
        
        TileButton rightbutton2 = new TileButton(skin, new TextureRegion(right2));
        rightbutton2.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                shift(true, 7);
            }
        });
        selectRow.add(rightbutton2).prefWidth(80).prefHeight(80);
		
        uiStage.addActor(selectRow);
        uiStage.addActor(editRow);
        //======================================================================
        //======================================================================
              
        up = new TileButton(skin, new TextureRegion(new Texture("images/upbutton.png")));
        up.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (editRow.getY() > Gdx.graphics.getHeight() / 2 + 320 - editRow.getCells().size * 80)
                    editRow.setY(editRow.getY() - 80);
            }
        });
        uiStage.addActor(up);
        up.setSize(80, 80);
        
        down = new TileButton(skin, new TextureRegion(new Texture("images/downbutton.png")));
        down.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (editRow.getY() < 480 - Gdx.graphics.getHeight() / 2)
                    editRow.setY(editRow.getY() + 80);
            }
        });
        uiStage.addActor(down);
        down.setSize(80, 80);
                
        InputMultiplexer im = new InputMultiplexer(uiStage, screen.getInput());
        Gdx.input.setInputProcessor(im);
    }
    
    public void draw()
    {        
        uiStage.act();
        uiStage.draw();
    }
    
    public void shift(boolean forward, int shift)
    {
        TileButton first = (TileButton)selectRow.getCells().get(SHIFT_INDEX).getActor();
        int firstID = first.getTextureID();
        
        for (Cell cell : selectRow.getCells())
        {
            TileButton button = (TileButton)cell.getActor();
            
            if (!button.getImagePath().equals("images/leftbutton.png") && !button.getImagePath().equals("images/rightbutton.png") &&
				!button.getImagePath().equals("images/leftbutton2.png") && !button.getImagePath().equals("images/rightbutton2.png"))
            {
                if (forward)
                {
					shift = firstID + shift < MAX_ID ? shift : MAX_ID - firstID;
                    button.setTextureID(button.getTextureID() + shift);
                }
                else
                {
					shift = firstID - shift > 0 ? shift : firstID;
                    button.setTextureID(button.getTextureID() - shift);
                }
            }
        }
    }
	
	public void switchTexture(Texture text)
	{		
		int i = savedIndex;
		savedIndex = ((TileButton)selectRow.getCells().get(SHIFT_INDEX).getActor()).getTextureID();
		
		for (Cell cell : selectRow.getCells())
        {
            TileButton button = (TileButton)cell.getActor();
            
			if (!button.getImagePath().equals("images/leftbutton.png") && !button.getImagePath().equals("images/rightbutton.png") &&
				!button.getImagePath().equals("images/leftbutton2.png") && !button.getImagePath().equals("images/rightbutton2.png"))
			{
				button.switchTexture(text, i);
				i++;
			}
        }
		
		TileButton current = (TileButton)editRow.getCells().first().getActor();
		
		int temp = savedID;
		savedID = getCurrentID();
		current.switchTexture(text, temp);
	}
    
    public int getCurrentID()
    {
        TileButton current = (TileButton)editRow.getCells().first().getActor();
        
        return current.getTextureID();
    }    
	
	public int getCurrentRotation()
    {
        CurrentButton current = (CurrentButton)editRow.getCells().first().getActor();
        // 360 is because UI is not y-flipped and so rotates the other direction
        int rotation = 360 - (int)current.getSpriteRotation();
		
		while (rotation < 0) rotation += 360;
		
		return rotation;
    }
    
    public int getCurrentRotation(boolean decoration)
    {
        CurrentButton current = (CurrentButton)editRow.getCells().first().getActor();
        // 360 is because UI is not y-flipped and so rotates the other direction
        return decoration ? 360 - (int)current.getDecorationRotation() : 360 - (int)current.getSpriteRotation();
    }
	
	public TileFunction getFunction()
	{
		return function;
	}
	
	private void writeCodes(List<String> codes, FileHandle file)
	{
		for (int i = 0; i < codes.size(); i++)
		{
			file.writeString(codes.get(i), true);

			if ((i + 1) % screen.getRenderer().getMap().mapWidth == 0)
			{
				file.writeString("\r\n", true); // Export with \r\n for maximum compatibility
			} 
			else
			{
				file.writeString(" ", true);
			}
		}
	}
	
	public void resize(int width, int height)
	{
		uiStage.getViewport().update(width, height, false);
		
		editRow.setX(width / 2 + 320);
		editRow.setY(height / 2 + 320 - editRow.getCells().size * 80);
		up.setX(editRow.getX());
		up.setY(editRow.getY() + editRow.getCells().size * 80);
		down.setX(editRow.getX());
		down.setY(400 - height / 2);
		
		selectRow.setX(-80);
		selectRow.setY(400 - height / 2);
	}
}
