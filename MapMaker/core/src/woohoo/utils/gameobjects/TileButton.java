package woohoo.utils.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import woohoo.utils.gameworld.GameRenderer;

public class TileButton extends Button
{
    private TextureRegion sprite;
    
    private int spriteRotation = 0;
    
    public TileButton()
    {
        sprite = new TextureRegion(GameRenderer.tileSet);
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
    
    public int getSpriteRotation()
    {
        return spriteRotation;
    }
    
    public void setSpriteRotation(int newRotation)
    {
        spriteRotation = newRotation;
    }
        
    @Override
    public void draw(Batch batcher, float parentAlpha)
    {
        super.draw(batcher, parentAlpha);
        batcher.draw(sprite, getX() + 5, getY() + 5,                    // Position
                     (getWidth() - 10) / 2, (getHeight() - 10) / 2,     // Origin
                     getWidth() - 10, getHeight() - 10,                 // Size
                     1, 1,                                              // Scale
                     getSpriteRotation());                              // Rotation
    }
}
