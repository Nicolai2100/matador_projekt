package dk.dtu.compute.se.pisd.monopoly.mini.database;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;

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

    public GameDAO() {

        try {
            c = createConnection();
            initializeDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Jeg har lavet den public så vi kan bruge den i tests - Nicolai L
    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185020",
                "s185020", "iEFSqK2BFP60YWMPlw77I");
    }

    @Override
    public void saveGame(Game game) {

        try {
            c.setAutoCommit(false);

            PreparedStatement insertGame = c.prepareStatement(
                    "INSERT INTO game");
            //blahblah

            PreparedStatement insertPLayers = c.prepareStatement(
                    "INSERT INTO player"
            );

            PreparedStatement insertProperties = c.prepareStatement(
                    "INSERT INTO propery"
            );


            for (Player player : game.getPlayers()) {

            }
            for (Space space : game.getSpaces()) {

            }
            c.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * @param gameId
     * @return a game
     * @author Jeppe s170196
     */
    @Override
    public Game loadGame(int gameId) {
        Game game = new Game();

/*
    checkConnection();
*/
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
    /*
            try (Connection c = createConnection()) {

                PreparedStatement gameStm = c.prepareStatement("SELECT * FROM Game");

                ResultSet gameRS = gameStm.executeQuery();
                while (gameRS.next()) {
                    //TODO: Make resultset
                }
            } catch (SQLException e) {
                //TODO: Handle exception?
            }
            */
        return gameList;


    }

    @Override
    public void updateGame(Game game) {

    }

    @Override
    public void deleteGame(Game game) {

    }

    public void initializeDataBase() {
        try {
            c.setAutoCommit(false);
            PreparedStatement createTableGame = c.prepareStatement(
                    "CREATE TABLE if NOT EXISTS game " +
                            "(gameid int, " +
                            "name varchar(20), " +
                            "timestamp varchar(20), " +
                            "primary key (gameid));");

            PreparedStatement createTablePlayer = c.prepareStatement(
                    "CREATE TABLE if NOT EXISTS player " +
                            "(playerid int, " +
                            "name varchar(20), " +
                            "balance int, " +
                            "position int, " +
                            "injain bit, " +
                            "isbroke bit, " +
                            "gameid int, " +
                            "primary key (playerid), " +
                            "FOREIGN KEY (gameid) REFERENCES game (gameid) " +
                            "ON DELETE CASCADE);");

            PreparedStatement createTableProperty = c.prepareStatement(
                    "CREATE TABLE if NOT EXISTS property " +
                            "(posonboard int, " +
                            "numofhouses int, " +
                            "superowned bit, " +
                            "playerid int, " +
                            "primary key (posonboard), " +
                            "FOREIGN KEY (playerid) REFERENCES player (playerid) " +
                            "ON DELETE CASCADE " +
                            "ON UPDATE CASCADE);");

            createTableGame.execute();
            createTablePlayer.execute();
            createTableProperty.execute();
            c.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropAllTables(int deleteTable){
        try {

            PreparedStatement dropTableGame = c.prepareStatement(
                    "drop table game;"
            );
            PreparedStatement dropTablePlayer = c.prepareStatement(
                    "drop table player;"
            );
            PreparedStatement dropTableProperty = c.prepareStatement(
                    "DROP TABLE property;"
            );
            if (deleteTable == 1){
                dropTableGame.execute();
            }
            else if (deleteTable == 2){
                dropTablePlayer.execute();
            }
            else if (deleteTable == 3){
                dropTableProperty.execute();
            }
            else if (deleteTable == 0){
                dropTableGame.execute();
                dropTablePlayer.execute();
                dropTableProperty.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
