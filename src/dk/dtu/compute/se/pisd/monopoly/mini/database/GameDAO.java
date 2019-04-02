package dk.dtu.compute.se.pisd.monopoly.mini.database;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeppe s170196, Nicolai s185036
 */
public class GameDAO implements IGameDAO {

    private Connection c;

    private void createConnection() throws SQLException {
        c = DriverManager.getConnection("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185020", "s185020", "iEFSqK2BFP60YWMPlw77I");
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
        checkConnection();
        try {
            PreparedStatement gameStm = c.prepareStatement("SELECT * FROM Game WHERE GameID=?");
            gameStm.setInt(1, gameId);

            ResultSet gameRS = gameStm.executeQuery();


            if (gameRS.next()) {
                //TODO: Make resultset
            }


        } catch (SQLException e) {
            //TODO: Handle exception?
        }
        return game;
    }

    @Override
    public List<Game> getGamesList() {
        List gameList = new ArrayList<Game>();
        checkConnection();
        try {

            PreparedStatement gameStm = c.prepareStatement("SELECT * FROM Game");

            ResultSet gameRS = gameStm.executeQuery();


            while (gameRS.next()) {
                //TODO: Make resultset
            }


        } catch (SQLException e) {
            //TODO: Handle exception?
        }
        return gameList;
    }

    @Override
    public void updateGame(Game game) {

    }

    @Override
    public void deleteGame(Game game) {

    }
}
