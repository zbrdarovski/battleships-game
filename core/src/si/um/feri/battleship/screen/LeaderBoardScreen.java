package si.um.feri.battleship.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
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

public class LeaderBoardScreen extends ScreenAdapter {

    public static int scoreOne = 0;
    public static int scoreTwo = 0;
    private final Battleship game;
    private final AssetManager assetManager;
    private Viewport viewport;
    private Stage stage;
    private Skin skin;
    private TextureAtlas gameplayAtlas;

    public LeaderBoardScreen(Battleship game) {
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

        stage.addActor(createUi());
        stage.addActor(createBackButton());
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

        String firstScore = "Player I: " + scoreOne;
        String secondScore = "Player II: " + scoreTwo;

        TextButton title = new TextButton("Leaderboard", skin);

        TextButton scoreOne = new TextButton(firstScore, skin);

        TextButton scoreTwo = new TextButton(secondScore, skin);

        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(30).padRight(30);

        buttonTable.add(title).expandX().fill().row();
        buttonTable.add(scoreOne).expandX().fill().row();
        buttonTable.add(scoreTwo).expandX().fill().row();

        buttonTable.center();

        table.add(buttonTable);
        table.center();
        table.setFillParent(true);
        table.pack();

        return table;
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
