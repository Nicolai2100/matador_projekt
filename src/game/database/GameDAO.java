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
 * @author Jeppe s170196, Nicolai s185036, Nicolai L s185020
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

    /**
     * Metoden opretter en forbindelse til databasen, som gemmes som i en lokal variabel.
     * Den er gjort public for at kunne bruges i test.
     * @return
     * @throws SQLException
     */
    public Connection createConnection() throws SQLException {
        String url = "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185020";
        String user = "s185020";
        String password = "iEFSqK2BFP60YWMPlw77I";
        return DriverManager.getConnection(url, user, password);
    }
    /**Metoden bruges til at gemme et spil i databasen
     * @author Jeppe s170196, Nicolai s185020
     */
    @Override
    public void saveGame(Game game) {

        try {
            c.setAutoCommit(false);

            PreparedStatement insertGame = c.prepareStatement(
                    "INSERT INTO game (curplayerid, date) " +
                            "VALUES(?,?);", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement insertPLayers = c.prepareStatement(
                    "INSERT INTO player " +
                            "VALUES(?,?,?,?,?,?,?,?,?);");

            PreparedStatement insertProperties = c.prepareStatement(
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
                insertPLayers.setString(9,player.getToken());
                insertPLayers.executeUpdate();
            }
            for (Space space : game.getSpaces()) {
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
                        insertProperties.setBoolean(3, utility.getSuperOwned());
                        insertProperties.setString(6, "utility");
                    }
                    int playerId = -1;
                    for (Player player : game.getPlayers()) {
                        if (player.getOwnedProperties().contains(space)) {
                            playerId = game.getPlayers().indexOf(player);
                        }
                    }
                    insertProperties.setInt(4, playerId);
                    insertProperties.setInt(5, gameid);
                    insertProperties.execute();
                }
            }
            c.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**Metoden "opdaterer" et allerede gemt spil.
     * @author Jeppe s170196
     */
    @Override
    public void updateGame(Game game) {
        deleteGame(game);
        saveGame(game);
    }

    /**Metoden bruges til at slette et spil fra databasen
     * @author Jeppe s170196
     */
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

    /**Metoden bruges til at hente et gemt spil fra databasen
     * @param game
     * @return a game
     * @author Jeppe s170196
     */
    @Override
    public Game loadGame(Game game, String dateOfGameToLoad) {
        int gameId = game.getGameId();

/*
    checkConnection();
*/
        try {
            PreparedStatement gameStm = c.prepareStatement("SELECT * FROM game WHERE date=?");
            gameStm.setString(1, dateOfGameToLoad);

            ResultSet gameRS = gameStm.executeQuery();

            if (gameRS.next()) {
                gameId = gameRS.getInt("gameid");
            }

            PreparedStatement playerStm = c.prepareStatement("SELECT * FROM player WHERE gameid=?");
            PreparedStatement propertyStm = c.prepareStatement("SELECT * FROM property WHERE gameid=?");

            playerStm.setInt(1, gameId);
            propertyStm.setInt(1, gameId);

            ResultSet playerRS = playerStm.executeQuery();
            ResultSet propertyRS = propertyStm.executeQuery();

            game.setGameId(gameId);

            List<Player> listOfPlayers = new ArrayList<Player>();
            while (playerRS.next()) {
                Player p = new Player();
                p.setName(playerRS.getString("name"));
                p.setCurrentPosition(game.getSpaces().get(playerRS.getInt("position")));
                p.setBalance(playerRS.getInt("balance"));
                p.setInPrison(playerRS.getBoolean("injail"));
                p.setBroke(playerRS.getBoolean("isbroke"));
                p.setColor(new Color(playerRS.getInt("color")));
                p.setToken(playerRS.getString("token"));
                listOfPlayers.add(playerRS.getInt("playerid"), p);
            }
            game.setPlayers(listOfPlayers);

            //Jeg har flyttet denne linje ud fra if-statementet nedenunder, fordi det aldrig blev kørt.
            game.setCurrentPlayer(game.getPlayers().get(gameRS.getInt("curplayerid")));
            /*
            if (gameRS.next()) {
                //flyt denne op
                game.setCurrentPlayer(game.getPlayers().get(gameRS.getInt("curplayerid")));
            }*/

            List<Space> listOfSpaces = new ArrayList<Space>();
            listOfSpaces.addAll(game.getSpaces());
            while (propertyRS.next()) {

                if (propertyRS.getString("type").equals("utility")) {
                    Utility utility = (Utility) listOfSpaces.get(propertyRS.getInt("posonboard"));

                    utility.setHouseCount(propertyRS.getInt("numofhouses"));
                    utility.setSuperOwned(propertyRS.getBoolean("superowned"));

                    if (propertyRS.getInt("playerid") != -1) {
                        utility.setOwner(game.getPlayers().get(propertyRS.getInt("playerid")));
                        utility.getOwner().addOwnedProperty(utility);
                    }

                    listOfSpaces.set(propertyRS.getInt("posonboard"), utility);

                } else if (propertyRS.getString("type").equals("realestate")) {
                    RealEstate realEstate = (RealEstate) listOfSpaces.get(propertyRS.getInt("posonboard"));

                    realEstate.setSuperOwned(propertyRS.getBoolean("superowned"));

                    if (propertyRS.getInt("playerid") != -1) {
                        realEstate.setOwner(game.getPlayers().get(propertyRS.getInt("playerid")));
                        realEstate.getOwner().addOwnedProperty(realEstate);
                    }

                    listOfSpaces.set(propertyRS.getInt("posonboard"), realEstate);
                }

            }
            game.setSpaces(listOfSpaces);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return game;
    }

    /**
     * @author Jeppe s170196
     */
    @Override
    public List<String> getGamesList() {

        List gameList = new ArrayList<String>();

        try {
            PreparedStatement gameStm = c.prepareStatement("SELECT * FROM game");

            ResultSet gameRS = gameStm.executeQuery();
            while (gameRS.next()) {
                gameList.add(gameRS.getString("date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameList;
    }

    /**Metoden sætter alle tabellerne op, hvis de ikke allerede ligger i databasen.
     * @author Nicolai s185020
     */
    public void initializeDataBase() {
        try {
            c.setAutoCommit(false);
            PreparedStatement createTableGame = c.prepareStatement(
                    "CREATE TABLE if NOT EXISTS game " +
                            "(gameid int NOT NULL AUTO_INCREMENT, " +
                            "date VARCHAR(20) NOT NULL, " +
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
                            "color int, " +
                            "token varchar(10)," +
                            "primary key (playerid, gameid), " +
                            "FOREIGN KEY (gameid) REFERENCES game (gameid) " +
                            "ON DELETE CASCADE);");

            PreparedStatement createTableProperty = c.prepareStatement(
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
            c.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Metoden er kun til brug i test - for hurtigt at kunne slette og oprette tabellerne ved fejl.
     * @author Nicolai s185020
     */
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
