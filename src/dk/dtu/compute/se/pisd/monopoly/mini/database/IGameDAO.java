package dk.dtu.compute.se.pisd.monopoly.mini.database;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;

import java.util.List;

public interface IGameDAO {
    void saveGame(Game game);

    Game loadGame(int gameId);

    List<Game> getGamesList();

    void updateGame(Game game);

    void deleteGame(Game game);

}
