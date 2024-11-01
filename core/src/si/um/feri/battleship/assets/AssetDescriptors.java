package si.um.feri.battleship.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {

    public static final AssetDescriptor<Skin> UI_SKIN =
            new AssetDescriptor<>(AssetPaths.UI_SKIN, Skin.class);

    public static final AssetDescriptor<TextureAtlas> GAMEPLAY =
            new AssetDescriptor<>(AssetPaths.GAMEPLAY, TextureAtlas.class);

    private AssetDescriptors() {
    }
}
