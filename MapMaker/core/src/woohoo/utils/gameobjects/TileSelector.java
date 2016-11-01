package woohoo.utils.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
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
import woohoo.utils.framework.InputHandler;
import static woohoo.utils.gameworld.GameRenderer.tileSet;

public class TileSelector
{    
    private static Stage uiStage;
    private static Skin skin;
    private static Table selectRow;
    private static Table editRow;
	
	private static InputHandler input = new InputHandler();
    
    private static Texture left = new Texture("images/leftbutton.png");
    private static Texture right = new Texture("images/rightbutton.png");
    
    private static final int MAX_ID = 16;
    
    public TileSelector()
    {
        skin = new Skin(Gdx.files.internal("ui/skin.json"));
        uiStage = new Stage(new ScreenViewport());
        selectRow = new Table();
        editRow = new Table();
        
        //======================================================================
        //======================================================================
        
        TileButton current = new TileButton(skin, new TextureRegion(tileSet, 0, 0, 16, 16));
        current.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                current.setSpriteRotation(current.getSpriteRotation() + 90);
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
                    TileMap.addRow(direction);
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
                    TileMap.deleteRow(direction);
                }
            });
            editRow.add(retractButton).prefWidth(80).prefHeight(80);
            editRow.row();
        }
		
		TileButton undo = new TileButton(skin, new TextureRegion(new Texture("images/undo.png")));
        undo.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                
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
						
						List<String> codes = TileMap.getCodes();
						FileHandle file = new FileHandle("maps/" + text + ".txt");
						
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
										
										for (int i = 0; i < codes.size(); i++)
										{
											file.writeString(codes.get(i), true);

											if ((i + 1) % TileMap.mapWidth == 0)
												file.writeString(System.getProperty("line.separator"), true);
											else if (i <= codes.size() - 1)
												file.writeString(" ", true);
										}
									}
								}
								
								@Override public void canceled(){}
							}, "Overwrite?", "", ".txt will automatically be appended");
						}
						else
						{						
							for (int i = 0; i < codes.size(); i++)
							{
								file.writeString(codes.get(i), true);

								if ((i + 1) % TileMap.mapWidth == 0)
									file.writeString(System.getProperty("line.separator"), true);
								else if (i <= codes.size() - 1)
									file.writeString(" ", true);
							}
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
        
        editRow.setPosition(720, 720 - editRow.getCells().size * 80);
        
        //======================================================================
        //======================================================================
                
        selectRow.setWidth(720);
        selectRow.setHeight(80);
        
        TileButton leftbutton = new TileButton(skin, new TextureRegion(left));
        leftbutton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                shift(false);
            }
        });
        selectRow.add(leftbutton).prefWidth(80).prefHeight(80);

        for (int i = 0; i < 7; i++)
        {
            TileButton button = new TileButton(skin, new TextureRegion(tileSet, i * 16, 0, 16, 16));

            button.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    current.setTextureID(button.getTextureID());
                }
            });
            
            selectRow.add(button).prefWidth(80).prefHeight(80);
        }
        
        TileButton rightbutton = new TileButton(skin, new TextureRegion(right));
        selectRow.add(rightbutton).prefWidth(80).prefHeight(80);
        rightbutton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                shift(true);
            }
        });
        
        uiStage.addActor(selectRow);
        uiStage.addActor(editRow);
        //======================================================================
        //======================================================================
              
        TileButton up = new TileButton(skin, new TextureRegion(new Texture("images/upbutton.png")));
        up.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (editRow.getY() > 720 - editRow.getCells().size * 80)
                    editRow.setY(editRow.getY() - 80);
            }
        });
        uiStage.addActor(up);
        up.setPosition(720, 720);
        up.setSize(80, 80);
        
        TileButton down = new TileButton(skin, new TextureRegion(new Texture("images/downbutton.png")));
        down.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (editRow.getY() < 80)
                    editRow.setY(editRow.getY() + 80);
            }
        });
        uiStage.addActor(down);
        down.setPosition(720, 0);
        down.setSize(80, 80);
                
        InputMultiplexer im = new InputMultiplexer(uiStage, input);
        Gdx.input.setInputProcessor(im);
    }
    
    public void draw()
    {        
        uiStage.act();
        uiStage.draw();
    }
    
    public void shift(boolean forward)
    {
        TileButton first = (TileButton)selectRow.getCells().get(1).getActor();
        int firstID = first.getTextureID();
        
        for (Cell cell : selectRow.getCells())
        {
            TileButton button = (TileButton)cell.getActor();
            
            if (button.getImagePath().equals("images/tileset.png"))
            {
                if (forward && firstID + selectRow.getCells().size - 2 < MAX_ID)
                {
                    button.setTextureID(button.getTextureID() + 1);
                }
                else if (!forward && firstID > 0)
                {
                    button.setTextureID(button.getTextureID() - 1);
                }
            }
        }
    }
    
    public static int getCurrentID()
    {
        TileButton current = (TileButton)editRow.getCells().first().getActor();
        
        return current.getTextureID();
    }    
    
    public static int getCurrentRotation()
    {
        TileButton current = (TileButton)editRow.getCells().first().getActor();
        
        return 360 - (int)current.getSpriteRotation(); // 360 is because UI is not y-flipped and so rotates the other direction
    }
}
