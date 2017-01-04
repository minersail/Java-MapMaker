package woohoo.utils.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CurrentButton extends TileButton
{    
    private int spriteRotation = 0;
	private int decorationRotation = 0;
	private boolean decorationMode = false;
       
    public CurrentButton(Skin skin, TextureRegion spr)
    {
        super(skin, spr);
    }
	
    public int getSpriteRotation()
    {
        return spriteRotation;
    }
    
    public void setSpriteRotation(int newRotation)
    {
        spriteRotation = newRotation;
    }
    
    public int getDecorationRotation()
    {
        return decorationRotation;
    }
    
    public void setDecorationRotation(int newRotation)
    {
        decorationRotation = newRotation;
    }
	
	public void toggleDecorationMode()
	{
		decorationMode = !decorationMode;
	}
	
	@Override
    public void draw(Batch batcher, float parentAlpha)
    {
		drawBackground(batcher, parentAlpha);
        batcher.draw(sprite, getX() + 5, getY() + 5,                    // Position
                     (getWidth() - 10) / 2, (getHeight() - 10) / 2,     // Origin
                     getWidth() - 10, getHeight() - 10,                 // Size
                     1, 1,                                              // Scale
                     decorationMode ? getDecorationRotation() : getSpriteRotation()); // Rotation
    }
}
