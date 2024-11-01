package si.um.feri.battleship.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import si.um.feri.battleship.CellState;
import si.um.feri.battleship.Battleship;

public class GameManager {

    public static final GameManager INSTANCE = new GameManager();

    private static final String INIT_MOVE_KEY = "initMove";

    private final Preferences PREFS;
    private CellState initMove;

    private GameManager() {
        PREFS = Gdx.app.getPreferences(Battleship.class.getSimpleName());
        String moveName = PREFS.getString(INIT_MOVE_KEY, CellState.ONE.name());
        initMove = CellState.valueOf(moveName);
    }

    public CellState getInitMove() {
        return initMove;
    }

}