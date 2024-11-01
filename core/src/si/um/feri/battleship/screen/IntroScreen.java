package si.um.feri.battleship.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.um.feri.battleship.Battleship;
import si.um.feri.battleship.assets.AssetDescriptors;
import si.um.feri.battleship.assets.RegionNames;
import si.um.feri.battleship.config.GameConfig;

public class IntroScreen extends ScreenAdapter {

    public static final float INTRO_DURATION_IN_SEC = 3f;   // duration of the (intro) animation

    private final Battleship game;
    private final AssetManager assetManager;
    public static Sound introSound = Gdx.audio.newSound(Gdx.files.internal("desktop/sounds/wave.mp3"));
    private Viewport viewport;
    private TextureAtlas gameplayAtlas;
    private float duration = 0f;
    private Stage stage;


    public IntroScreen(Battleship game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        // load assets
        assetManager.load(AssetDescriptors.UI_SKIN);
        assetManager.load(AssetDescriptors.GAMEPLAY);
        assetManager.finishLoading();   // blocks until all assets are loaded

        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);

        stage.addActor(createAnimation());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0 / 255f, 191 / 255f, 255 / 255f, 0f);

        duration += delta;

        // go to the MenuScreen after INTRO_DURATION_IN_SEC seconds
        if (duration > INTRO_DURATION_IN_SEC) {
            game.setScreen(new MenuScreen(game));
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Actor createAnimation() {
        Image ship = new Image(gameplayAtlas.findRegion(RegionNames.PIRATE_SHIP));

        ship.setOrigin(Align.center);
        ship.setPosition(0f, viewport.getWorldHeight() / 2f);
        introSound.play();
        ship.addAction(
                Actions.sequence(
                        Actions.parallel(
                                Actions.moveTo(viewport.getWorldWidth() - ship.getWidth(), (viewport.getWorldHeight() / 2f) - ship.getHeight(), 3.0f)   // // move image to the end of the window
                        ),
                        Actions.removeActor()   // // remove image
                )
        );

        return ship;
    }
}
