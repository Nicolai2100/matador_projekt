package dk.dtu.compute.se.pisd.monopoly.mini.database;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GameDao implements IGameDAO {

    private Connection createConnection(){
        return null;
    }

    @Override
    public void saveGame(Game game) {

    }

    /**
     *
     * @param gameId
     * @return a game
     * @author Jeppe s170196
     */
    @Override
    public Game loadGame(int gameId) {
        Game game = new Game();
        try (Connection c = createConnection()) {

            PreparedStatement gameStm = c.prepareStatement("SELECT * FROM Game WHERE GameID=?");
            gameStm.setInt(1, gameId);

            ResultSet gameRS = gameStm.executeQuery();


            while (gameRS.next()) {
                //TODO: Make resultset
            }


        } catch (SQLException e) {
            //TODO: Handle exception?
        }
        return game;
    }

    @Override
    public List<Game> getGamesList() {
        return null;
    }

    @Override
    public void updateGame(Game game) {

    }

    @Override
    public void deleteGame(Game game) {

    }
}
