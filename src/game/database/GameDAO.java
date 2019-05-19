package game.database;

import game.model.Game;
import game.model.Player;
import game.model.Property;
import game.model.Space;
import game.model.properties.RealEstate;
import game.model.properties.Utility;

import java.awt.*;
import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeppe K. Larsen, 	s170196@student.dtu.dk
 * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
 * @author Nicolai J. Larsen, 	s185020@student.dtu.dk
 */
public class GameDAO implements IGameDAO {

    private static final String URL = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185020";
    private static final String USER = "s185020";
    private static final String PASSWORD = "iEFSqK2BFP60YWMPlw77I";
    private static Connection connection;

    public GameDAO() {
        initializeDataBase();
    }

    /**
     * This method either returns an open connection to the database, or creates and returns a new connection.
     * @return
     * @throws SQLException
     * @author Jeppe K. Larsen
     * @author Nicolai J. Larsen
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * This method is used to insert a game in the database.
     * @author Jeppe K. Larsen
     * @author Nicolai J. Larsen
     */
    @Override
    public void saveGame(Game game) {
        long performance = System.currentTimeMillis();
        try {
            getConnection().setAutoCommit(false);
            PreparedStatement insertGame = getConnection().prepareStatement(
                    "INSERT INTO game (curplayerid, date) " +
                            "VALUES(?,?);", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement insertPLayers = getConnection().prepareStatement(
                    "INSERT INTO player " +
                            "VALUES(?,?,?,?,?,?,?,?,?);");

            PreparedStatement insertProperties = getConnection().prepareStatement(
                    "INSERT INTO property " +
                            "VALUES(?,?,?,?,?,?);");
            int curPlayer = game.getPlayers().indexOf(game.getCurrentPlayer());
            insertGame.setInt(1, curPlayer);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            insertGame.setString(2, dtf.format(now));

            insertGame.executeUpdate();
            ResultSet gen = insertGame.getGeneratedKeys();
            int gameid = 0;
            if (gen.next()) {
                gameid = gen.getInt(1);
                game.setGameId(gameid);
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
                insertPLayers.setInt(8, player.getColor().getRGB());
                insertPLayers.setString(9, player.getCarType().toString());
                insertPLayers.executeUpdate();
                insertPLayers.clearParameters();

                for (Space space : player.getOwnedProperties()) {
                    if (space instanceof Property) {
                        insertProperties.setInt(1, space.getIndex());
                        if (space instanceof RealEstate) {
                            RealEstate realEstate = (RealEstate) space;
                            insertProperties.setInt(2, realEstate.getHouseCount());
                            insertProperties.setBoolean(3, realEstate.getSuperOwned());
                            insertProperties.setString(6, "realestate");
                        }
                        if (space instanceof Utility) {
                            Utility utility = (Utility) space;
                            insertProperties.setInt(2, 0);
                            insertProperties.setBoolean(3, utility.getSuperOwned());
                            insertProperties.setString(6, "utility");
                        }

                        int playerId = game.getPlayers().indexOf(player);
                        insertProperties.setInt(4, playerId);
                        insertProperties.setInt(5, gameid);
                        insertProperties.executeUpdate();
                        insertProperties.clearParameters();
                    }
                }
            }
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Tid det tog at gemme spillet i databasen: " + (System.currentTimeMillis() - performance) + "ms");
    }

    /**
     * This method is updating a already saved game in the database by deleting it and inserting it again.
     * @author Jeppe K. Larsen
     */
    @Override
    public void updateGame(Game game) {
        deleteGame(game);
        saveGame(game);
    }

    /**
     * This method is used to delete a game from the database
     * @author Jeppe K. Larsen, s170196
     */
    @Override
    public void deleteGame(Game game) {
        try {
            PreparedStatement gameStm = getConnection().prepareStatement("DELETE FROM game WHERE gameid = ?");
            gameStm.setInt(1, game.getGameId());
            gameStm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to download a saved game from the database.
     * @param game
     * @return a game
     * @author Jeppe K. Larsen
     */
    @Override
    public Game loadGame(Game game, String dateOfGameToLoad) {
        long performance = System.currentTimeMillis();
        try {
            PreparedStatement playerStm = getConnection().prepareStatement("SELECT * FROM game NATURAL JOIN player WHERE date=?");
            PreparedStatement propertyStm = getConnection().prepareStatement("SELECT * FROM game NATURAL JOIN property WHERE date=?");

            playerStm.setString(1, dateOfGameToLoad);
            propertyStm.setString(1, dateOfGameToLoad);

            ResultSet playerRS = playerStm.executeQuery();
            ResultSet propertyRS = propertyStm.executeQuery();

            int curplayerid = 0;
            List<Player> listOfPlayers = new ArrayList<Player>();
            List<Space> listOfSpaces = new ArrayList<Space>();

            while (playerRS.next()) {
                game.setGameId(playerRS.getInt("gameid"));
                curplayerid = playerRS.getInt("curplayerid");

                Player p = new Player();
                p.setName(playerRS.getString("name"));
                p.setCurrentPosition(game.getSpaces().get(playerRS.getInt("position")));
                p.setBalance(playerRS.getInt("balance"));
                p.setInPrison(playerRS.getBoolean("injail"));
                p.setBroke(playerRS.getBoolean("isbroke"));
                p.setColor(new Color(playerRS.getInt("color")));
                p.setCarType(Player.CarType.getCarTypeFromString(playerRS.getString("token")));
                listOfPlayers.add(playerRS.getInt("playerid"), p);
            }
            game.setPlayers(listOfPlayers);
            game.setCurrentPlayer(game.getPlayers().get(curplayerid));

            listOfSpaces.addAll(game.getSpaces());
            while (propertyRS.next()) {
                if (propertyRS.getInt("playerid") != -1) {
                    if (propertyRS.getString("type").equals("utility")) {
                        Utility utility = (Utility) listOfSpaces.get(propertyRS.getInt("posonboard"));

                        utility.setSuperOwned(propertyRS.getBoolean("superowned"));
                        utility.setOwner(game.getPlayers().get(propertyRS.getInt("playerid")));
                        utility.getOwner().addOwnedProperty(utility);

                        listOfSpaces.set(propertyRS.getInt("posonboard"), utility);
                    } else if (propertyRS.getString("type").equals("realestate")) {
                        RealEstate realEstate = (RealEstate) listOfSpaces.get(propertyRS.getInt("posonboard"));

                        realEstate.setSuperOwned(propertyRS.getBoolean("superowned"));
                        realEstate.setOwner(game.getPlayers().get(propertyRS.getInt("playerid")));
                        realEstate.getOwner().addOwnedProperty(realEstate);
                        realEstate.setHouseCount(propertyRS.getInt("numofhouses"));

                        listOfSpaces.set(propertyRS.getInt("posonboard"), realEstate);
                    }
                }
            }
            game.setSpaces(listOfSpaces);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Tid det tog at hente spillet i databasen:" + (System.currentTimeMillis() - performance) + "ms");
        return game;
    }

    /**
     * This method is used to return a list of dates of all the saved games in the database.
     * Each date implies a specific saved game.
     * @author Jeppe K. Larsen
     */
    @Override
    public List<String> getGamesList() {

        List gameList = new ArrayList<String>();

        try {
            PreparedStatement gameStm = getConnection().prepareStatement("SELECT * FROM game ORDER BY gameid DESC");
            ResultSet gameRS = gameStm.executeQuery();
            while (gameRS.next()) {
                gameList.add(gameRS.getString("date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameList;
    }
    /**
     * This method creates the all the tables needed to save and load the game
     * if the tables are not already created.
     * @author Nicolai J. Larsen
     */
    private void initializeDataBase() {
        try {
            getConnection().setAutoCommit(false);
            PreparedStatement createTableGame = getConnection().prepareStatement(
                    "CREATE TABLE if NOT EXISTS game " +
                            "(gameid int NOT NULL AUTO_INCREMENT, " +
                            "date VARCHAR(20) NOT NULL, " +
                            "curplayerid int, " +
                            "primary key (gameid));");

            PreparedStatement createTablePlayer = getConnection().prepareStatement(
                    "CREATE TABLE if NOT EXISTS player " +
                            "(playerid int, " +
                            "name varchar(20), " +
                            "balance int, " +
                            "position int, " +
                            "injail bit, " +
                            "isbroke bit, " +
                            "gameid int, " +
                            "color int, " +
                            "token varchar(10)," +
                            "primary key (playerid, gameid), " +
                            "FOREIGN KEY (gameid) REFERENCES game (gameid) " +
                            "ON DELETE CASCADE);");

            PreparedStatement createTableProperty = getConnection().prepareStatement(
                    "CREATE TABLE if NOT EXISTS property " +
                            "(posonboard int, " +
                            "numofhouses int, " +
                            "superowned bit, " +
                            "playerid int, " +
                            "gameid int, " +
                            "type varchar(20), " +
                            "primary key (posonboard, gameid), " +
                            "FOREIGN KEY (gameid) REFERENCES game (gameid) " +
                            "ON DELETE CASCADE);");

            createTableGame.execute();
            createTablePlayer.execute();
            createTableProperty.execute();
            getConnection().commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
