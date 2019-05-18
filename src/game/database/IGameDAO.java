package game.database;

import game.model.Game;

import java.util.List;

public interface IGameDAO {
    void saveGame(Game game);

    Game loadGame(Game game, String dateOfGameToLoad);

    List<String> getGamesList();

    void updateGame(Game game);

    void deleteGame(Game game);

}
