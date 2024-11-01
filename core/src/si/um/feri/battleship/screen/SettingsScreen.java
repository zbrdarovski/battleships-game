package si.um.feri.battleship.screen;

import static si.um.feri.battleship.screen.MenuScreen.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.um.feri.battleship.assets.AssetDescriptors;
import si.um.feri.battleship.Battleship;
import si.um.feri.battleship.config.GameConfig;

public class SettingsScreen extends ScreenAdapter {

    private final Battleship game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;
    private TextureAtlas gameplayAtlas;

    public static boolean isPlayingMusic = true;
    public static boolean isPlayingSounds = true;

    public SettingsScreen(Battleship game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
        assetManager.finishLoading();

        stage.addActor(createBackButton());
        stage.addActor(createMusicButton());
        stage.addActor(createSoundButton());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0 / 255f, 191 / 255f, 255 / 255f, 0f);

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

    private Actor createMusicButton() {
        final TextButton musicButton = new TextButton("Music", skin);
        musicButton.setWidth(200);
        musicButton.setHeight(100);
        musicButton.setPosition(GameConfig.HUD_WIDTH / 2f - musicButton.getWidth() / 2f, 250f);
        musicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(isPlayingMusic){
                music.pause();
                isPlayingMusic = false;
                } else {
                    music.play();
                    isPlayingMusic = true;
                }
            }
        });
        return musicButton;
    }

    private Actor createSoundButton() {
        final TextButton soundButton = new TextButton("Sounds", skin);
        soundButton.setWidth(200);
        soundButton.setHeight(100);
        soundButton.setPosition(GameConfig.HUD_WIDTH / 2f - soundButton.getWidth() / 2f, 150f);
        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(isPlayingSounds){
                    isPlayingSounds = false;
                } else {
                    isPlayingSounds = true;
                }
            }
        });
        return soundButton;
    }

    private Actor createBackButton() {
        final TextButton backButton = new TextButton("Back", skin);
        backButton.setWidth(200);
        backButton.setHeight(100);
        backButton.setPosition(GameConfig.HUD_WIDTH / 2f - backButton.getWidth() / 2f, 0f);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        return backButton;
    }
}
