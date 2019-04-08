package dk.dtu.compute.se.pisd.monopoly.mini.database;

import dk.dtu.compute.se.pisd.monopoly.mini.model.Game;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Player;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Property;
import dk.dtu.compute.se.pisd.monopoly.mini.model.Space;
import dk.dtu.compute.se.pisd.monopoly.mini.model.properties.RealEstate;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
                    "INSERT INTO game (curplayerid) " +
                            "VALUES(?);", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement insertPLayers = c.prepareStatement(
                    "INSERT INTO player " +
                            "VALUES(?,?,?,?,?,?,?);");

            PreparedStatement insertProperties = c.prepareStatement(
                    "INSERT INTO property " +
                            "VALUES(?,?,?,?,?);");

            int curPlayer = game.getPlayers().indexOf(game.getCurrentPlayer());
            insertGame.setInt(1, curPlayer);
            insertGame.executeUpdate();
            ResultSet gen = insertGame.getGeneratedKeys();
            int gameid = 0;
            if (gen.next()) {
                gameid = gen.getInt(1);
                game.setGameId(gameid);
                System.out.println(gameid);
            }

            int index = 0;
            for (Player player : game.getPlayers()) {

                index = game.getPlayers().indexOf(player);
                insertPLayers.setInt(1, index);
                insertPLayers.setString(2, player.getName());
                insertPLayers.setInt(3, player.getBalance());
                insertPLayers.setInt(4, player.getCurrentPosition().getIndex());
                insertPLayers.setBoolean(5, player.isInPrison());
                insertPLayers.setBoolean(6, player.isBroke());
                insertPLayers.setInt(7, gameid);
                insertPLayers.executeUpdate();
            }

            for (Space space : game.getSpaces()) {
                if (space instanceof Property) {

                    if (space instanceof RealEstate) {
                        RealEstate realEstate = (RealEstate) space;
                        insertProperties.setInt(1, space.getIndex());
                        insertProperties.setInt(2, realEstate.getHouseCount());
                        insertProperties.setBoolean(3, realEstate.getSuperOwned());

                        int playerId = -1;
                        for (Player player : game.getPlayers()) {
                            if (player.getOwnedProperties().contains(realEstate)) {
                                playerId = game.getPlayers().indexOf(player);
                            }
                        }
                        insertProperties.setInt(4, playerId);
                        insertProperties.setInt(5, gameid);
                        insertProperties.execute();
                    }
                }
            }
            c.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void updateGame(Game game) {
        deleteGame(game);
        saveGame(game);
    }

    @Override
    public void deleteGame(Game game) {
        try {
            PreparedStatement gameStm = c.prepareStatement("DELETE FROM game WHERE gameid = ?");
            gameStm.setInt(1, game.getGameId());
            gameStm.executeUpdate();

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


    public void initializeDataBase() {
        try {
            c.setAutoCommit(false);
            PreparedStatement createTableGame = c.prepareStatement(
                    "CREATE TABLE if NOT EXISTS game " +
                            "(gameid int NOT NULL AUTO_INCREMENT, " +
                            "date DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                            "curplayerid int, " +
                            "primary key (gameid));");

            PreparedStatement createTablePlayer = c.prepareStatement(
                    "CREATE TABLE if NOT EXISTS player " +
                            "(playerid int, " +
                            "name varchar(20), " +
                            "balance int, " +
                            "position int, " +
                            "injail bit, " +
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
                            "gameid int, " +
                            "primary key (posonboard), " +
                            "FOREIGN KEY (gameid) REFERENCES game (gameid) " +
                            "ON DELETE CASCADE);");

            createTableGame.execute();
            createTablePlayer.execute();
            createTableProperty.execute();
            c.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropAllTables(int deleteTable) {
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
            if (deleteTable == 1) {
                dropTableGame.execute();
            } else if (deleteTable == 2) {
                dropTablePlayer.execute();
            } else if (deleteTable == 3) {
                dropTableProperty.execute();
            } else if (deleteTable == 0) {
                dropTableProperty.execute();
                dropTablePlayer.execute();
                dropTableGame.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
