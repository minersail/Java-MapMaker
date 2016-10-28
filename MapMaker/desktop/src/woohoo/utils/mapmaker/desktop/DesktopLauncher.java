package woohoo.utils.mapmaker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import woohoo.utils.mapmaker.MapMaker;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        
        cfg.title = "TileGame";
        cfg.width = 800;
        cfg.height = 800;
        
		new LwjglApplication(new MapMaker(), cfg);
	}
}
