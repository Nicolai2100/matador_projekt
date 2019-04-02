package dk.dtu.compute.se.pisd.monopoly.mini.database;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;

public interface IGameDAO {
    void saveGame(Game game);

    Game loadGame(int gameId);

    void updateGame(Game game);

    void deleteGame(Game game);

}
