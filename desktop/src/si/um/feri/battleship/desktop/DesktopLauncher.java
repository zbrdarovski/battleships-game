package si.um.feri.battleship.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import si.um.feri.battleship.Battleship;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Battleship";
		config.width = 1024;
		config.height = 480;
		config.forceExit = false; // do I need it https://gamedev.stackexchange.com/questions/109047/how-to-close-an-app-correctly-on-desktop
		config.addIcon("icon.png", Files.FileType.Internal);
		new LwjglApplication(new Battleship(), config);
	}
}