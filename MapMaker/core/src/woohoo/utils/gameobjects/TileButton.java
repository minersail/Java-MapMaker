package woohoo.utils.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TileButton extends Button
{
    protected TextureRegion sprite;
    
    public TileButton(Texture texture)
    {
        sprite = new TextureRegion(texture);
    }
    
    public TileButton(Skin skin, TextureRegion spr)
    {
        super(skin);
        sprite = spr;
    }
    
    public TextureRegion getSprite()
    {
        return sprite;
    }
	
	public void switchTexture(Texture text, int tile, int mapWidth)
	{
		sprite = new TextureRegion(text, tile % mapWidth * 16, tile / mapWidth * 16, 16, 16);
	}
    
    public String getImagePath()
    {
        return ((FileTextureData)sprite.getTexture().getTextureData()).getFileHandle().path();
    }
    
    public int getTextureID()
    {
        return sprite.getRegionX() / 16 + sprite.getRegionY();
    }
    
    public void setTextureID(int ID)
    {
        sprite.setRegion((ID % 16) * 16, (ID / 16) * 16, sprite.getRegionWidth(), sprite.getRegionHeight());
    }
	
	protected void drawBackground(Batch batcher, float parentAlpha)
	{
        super.draw(batcher, parentAlpha);		
	}
        
    @Override
    public void draw(Batch batcher, float parentAlpha)
    {
		drawBackground(batcher, parentAlpha);
        batcher.draw(sprite, getX() + 5, getY() + 5,                    // Position
                     (getWidth() - 10) / 2, (getHeight() - 10) / 2,     // Origin
                     getWidth() - 10, getHeight() - 10,                 // Size
                     1, 1,                                              // Scale
                     0);												// Rotation
    }
}
