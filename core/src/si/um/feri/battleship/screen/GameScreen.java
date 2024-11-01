package si.um.feri.battleship.screen;

import static si.um.feri.battleship.screen.IntroScreen.introSound;
import static si.um.feri.battleship.screen.LeaderBoardScreen.scoreOne;
import static si.um.feri.battleship.screen.LeaderBoardScreen.scoreTwo;
import static si.um.feri.battleship.screen.MenuScreen.music;
import static si.um.feri.battleship.screen.SettingsScreen.isPlayingSounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

import si.um.feri.battleship.Battleship;
import si.um.feri.battleship.CellActor;
import si.um.feri.battleship.CellState;
import si.um.feri.battleship.assets.AssetDescriptors;
import si.um.feri.battleship.assets.RegionNames;
import si.um.feri.battleship.common.GameManager;
import si.um.feri.battleship.config.GameConfig;

public class GameScreen extends ScreenAdapter {

    private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);
    public static Integer counterOne = 0;
    public static Integer counterTwo = 0;
    public static boolean check = false;

    private final Battleship game;
    private final AssetManager assetManager;
    public static Sound soundOne = Gdx.audio.newSound(Gdx.files.internal("desktop/sounds/explosion.mp3"));
    public static Sound soundTwo = Gdx.audio.newSound(Gdx.files.internal("desktop/sounds/splash.mp3"));
    private Viewport viewport;
    private Viewport hudViewport;
    private Stage gameplayStage;
    private Stage hudStage;
    private Skin skin;
    private TextureAtlas gameplayAtlas;
    private CellState move = GameManager.INSTANCE.getInitMove();
    private Image infoImage;

    public GameScreen(Battleship game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);

        gameplayStage = new Stage(viewport, game.getBatch());
        hudStage = new Stage(hudViewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);
        gameplayAtlas = assetManager.get(AssetDescriptors.GAMEPLAY);
        assetManager.finishLoading();

        gameplayStage.addActor(createGrid(4, 7, 10f));
        hudStage.addActor(createInfo());
        hudStage.addActor(createBackButton());

        Gdx.input.setInputProcessor(new InputMultiplexer(gameplayStage, hudStage));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0 / 255f, 191 / 255f, 255 / 255f, 0f);

        // update
        gameplayStage.act(delta);
        hudStage.act(delta);

        // draw
        gameplayStage.draw();
        hudStage.draw();

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        gameplayStage.dispose();
        hudStage.dispose();
    }

    private Actor createGrid(int rows, int columns, final float cellSize) {
        final Random rand = new Random();
        final Table table = new Table();
        counterOne = 0;
        counterTwo = 0;
        check = false;
        table.setDebug(false);   // turn on all debug lines (table, cell, and widget)

        final Table grid = new Table();
        grid.defaults().size(cellSize);   // all cells will be the same size
        grid.setDebug(false);

        final TextureRegion waterRegion = gameplayAtlas.findRegion(RegionNames.WATER);
        final TextureRegion backgroundRegion = gameplayAtlas.findRegion(RegionNames.BACKGROUND);

        final ArrayList<TextureRegion> ships = new ArrayList<>();
        ships.add(gameplayAtlas.findRegion(RegionNames.CARGO_CONTAINER));
        ships.add(gameplayAtlas.findRegion(RegionNames.CARGO_WOOD));
        ships.add(gameplayAtlas.findRegion(RegionNames.CARGO_METAL));
        ships.add(gameplayAtlas.findRegion(RegionNames.PIRATE_SHIP));

        final TextureRegion oneRegion = gameplayAtlas.findRegion(RegionNames.ONE);
        final TextureRegion twoRegion = gameplayAtlas.findRegion(RegionNames.TWO);

        if (move == CellState.ONE) {
            infoImage = new Image(oneRegion);
        } else if (move == CellState.TWO) {
            infoImage = new Image(twoRegion);
        }

        for (int row = 0; row < rows; row++) {
            final int randOne = rand.nextInt(3);
            final int randTwo = rand.nextInt(3) + 4;
            for (int column = 0; column < columns; column++) {
                final CellActor cell;
                if (column != 3) {
                    cell = new CellActor(waterRegion);
                    final int finalColumn = column;
                    final int finalRow = row;
                    cell.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            final CellActor clickedCell = (CellActor) event.getTarget(); // it will be an image for sure :-)
                            if (counterOne < 4 && counterTwo < 4) {
                                if (clickedCell.isEmpty()) {
                                    switch (move) {
                                        case ONE:
                                            clickedCell.setState(move);
                                            if (finalColumn == randOne) {
                                                clickedCell.setDrawable(ships.get(0));
                                                counterOne += 1;
                                                if (counterOne == 3 && !check) {
                                                    scoreOne += 1;
                                                }
                                                if (counterOne == 3) {
                                                    check = true;
                                                }
                                                if(isPlayingSounds) {
                                                soundOne.play(); }
                                            } else {
                                                clickedCell.setDrawable(waterRegion);
                                                if(isPlayingSounds) {
                                                soundTwo.play(); }
                                            }
                                            infoImage.setDrawable(new TextureRegionDrawable(twoRegion));
                                            move = CellState.TWO;
                                            break;

                                        case TWO:
                                            clickedCell.setState(move);
                                            if (finalColumn == randTwo) {
                                                clickedCell.setDrawable(ships.get(1));
                                                counterTwo += 1;
                                                if (counterTwo == 3 && !check) {
                                                    scoreTwo += 1;
                                                }
                                                if (counterTwo == 3) {
                                                    check = true;
                                                }
                                                if(isPlayingSounds) {
                                                soundOne.play(); }
                                            } else {
                                                clickedCell.setDrawable(waterRegion);
                                                if(isPlayingSounds) {
                                                soundTwo.play(); }
                                            }
                                            infoImage.setDrawable(new TextureRegionDrawable(oneRegion));
                                            move = CellState.ONE;
                                            break;
                                    }
                                }
                            }
                            log.debug("clicked");
                        }
                    });
                } else {
                    cell = new CellActor(backgroundRegion);
                }
                grid.add(cell);

            }
            grid.row();
        }

        table.add(grid).row();
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

    private Actor createInfo() {
        final Table table = new Table();
        table.add(new Label("Turn: ", skin));
        table.add(infoImage).size(30).row();
        table.center();
        table.pack();
        table.setPosition(
                GameConfig.HUD_WIDTH / 2f - table.getWidth() / 2f,
                GameConfig.HUD_HEIGHT - table.getHeight() - 20f
        );
        return table;
    }
}