package si.um.feri.battleship.screen;

import static si.um.feri.battleship.screen.GameScreen.soundOne;
import static si.um.feri.battleship.screen.GameScreen.soundTwo;
import static si.um.feri.battleship.screen.IntroScreen.introSound;
import static si.um.feri.battleship.screen.SettingsScreen.isPlayingMusic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import si.um.feri.battleship.Battleship;
import si.um.feri.battleship.assets.AssetDescriptors;
import si.um.feri.battleship.config.GameConfig;

public class MenuScreen extends ScreenAdapter {

    public static Music music = Gdx.audio.newMusic(Gdx.files.internal("desktop/music/music.mp3"));
    private final Battleship game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;
    public static Skin skin;
    private TextureAtlas gameplayAtlas;


    public MenuScreen(Battleship game) {

        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        music.setVolume(0.25f);
        music.setLooping(true);
        if(isPlayingMusic) {
            music.play();
        }
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
        assetManager.finishLoading();

        stage.addActor(createUi());
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

    private Actor createUi() {
        Table table = new Table();
        table.defaults().pad(20);

        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton leaderboardButton = new TextButton("Leaderboard", skin);
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderBoardScreen(game));
            }
        });

        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundOne.dispose();
                soundTwo.dispose();
                introSound.dispose();
                music.dispose();
                Gdx.app.exit();
            }
        });

        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(30).padRight(30);

        buttonTable.add(playButton).padBottom(15).expandX().fill().row();
        buttonTable.add(leaderboardButton).padBottom(15).fillX().row();
        buttonTable.add(settingsButton).padBottom(15).fillX().row();
        buttonTable.add(quitButton).fillX();

        buttonTable.center();

        table.add(buttonTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
    }
}
