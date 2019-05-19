package game.view;

import designpattern.Observer;
import designpattern.Subject;
import game.model.*;
import game.model.properties.Brewery;
import game.model.properties.RealEstate;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a view on the Monopoly game based
 * on the original Matador GUI; it serves as a kind of
 * adapter to the Matador GUI. This class realizes the
 * MVC-principle on top of the Matador GUI. In particular,
 * the view implements an observer of the model in the
 * sense of the MVC-principle, which updates the GUI when
 * the state of the game (model) changes.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Nicolai J. Larsen
 * @author Nicolai d T. Wulff
 */
public class View implements Observer, Runnable {
    private Game game;
    private GUI gui;
    private Map<Player, GUI_Player> player2GuiPlayer = new HashMap<Player, GUI_Player>();
    private Map<Player, Integer> player2position = new HashMap<Player, Integer>();
    private Map<Space, GUI_Field> space2GuiField = new HashMap<Space, GUI_Field>();
    private Map<Player, PlayerPanel> player2PlayerPanel = new HashMap<Player, PlayerPanel>();
    private boolean disposed = false;

    /**
     * Constructor for the view of a game based on a game and an already
     * running Matador GUI.
     *
     * @param game the game
     * @param gui  the GUI
     */
    public View(Game game, GUI gui) {
        this.game = game;
        this.gui = gui;
        GUI_Field[] guiFields = gui.getFields();
        int i = 0;
        for (Space space : game.getSpaces()) {
            space2GuiField.put(space, guiFields[i]);
            space.attach(this);
            i++;
        }
    }

    /**
     * This method is used to decide which part of the view have to be updated as a response
     * to a change in the state of a model object.
     *
     * @param subject the subject which changed
     */
    @Override
    public void update(Subject subject) {
        if (!disposed) {

            if (subject instanceof Player) {
                updatePlayer((Player) subject);
            }
            if (subject instanceof Property) {
                updateProperty((Property) subject);
            }
            if (subject instanceof Game) {
                initializeGUI();
            }
        }
    }

    /**
     * This method is used to update the GUI when a Property-object have
     * changed its state.
     *
     * @param property
     */
    public void updateProperty(Property property) {
        GUI_Field gui_field = this.space2GuiField.get(property);
        if (gui_field instanceof GUI_Ownable) {
            GUI_Ownable gui_ownable = (GUI_Ownable) gui_field;
            Player owner = property.getOwner();
            if (owner != null) {
                gui_ownable.setBorder(owner.getColor());
                gui_ownable.setOwnerName(owner.getName());
            } else {
                gui_ownable.setBorder(null);
                gui_ownable.setOwnerName(null);
            }
            gui_ownable.setRent(property.getRent() + "");
        }
        if (property instanceof RealEstate) {
            GUI_Street street = (GUI_Street) gui_field;
            street.setHotel(false);
            if (((RealEstate) property).getHouseCount() > 0 && ((RealEstate) property).getHouseCount() < 5) {
                street.setHouses(((RealEstate) property).getHouseCount());
            } else if (((RealEstate) property).getHouseCount() == 5) {
                street.setHotel(true);
            }
        }
        if (property instanceof Brewery) {
            ((GUI_Ownable) gui_field).setRent(property.getRent() + " gange øjnene.");
        }
        if (property.getOwner() != null) {
            player2PlayerPanel.get(property.getOwner()).update();
        }
    }

    /**
     * This method updates a player's state in the GUI.
     *
     * @param player the player who's state is to be updated
     * @author Ekkart Kindler, ekki@dtu.dk
     * edited by
     * @author Nicolai J. Larsen
     */
    private void updatePlayer(Player player) {
        GUI_Player guiPlayer = this.player2GuiPlayer.get(player);
        if (guiPlayer != null) {
            guiPlayer.setBalance(player.getBalance());

            GUI_Field[] guiFields = gui.getFields();
            Integer oldPosition = player2position.get(player);
            int pos = player.getCurrentPosition().getIndex();

            if (oldPosition != null && oldPosition < guiFields.length) {
                int moves = calcNumOfMoves(player);
                for (int i = 1; i <= moves; i++) {
                    run();
                    guiFields[((oldPosition + i - 1) % 40)].setCar(player2GuiPlayer.get(player), false);
                    player2position.put(player, pos);
                    guiFields[((oldPosition + i) % 40)].setCar(player2GuiPlayer.get(player), true);
                }
            }
            String name = player.getName();
            if (player.isBroke()) {
                guiPlayer.setName(player.getName() + " (konkurs)");
            } else if (player.isInPrison()) {
                guiPlayer.setName(player.getName() + " (i fængsel)");
            } else if (!name.equals(guiPlayer.getName())) {
                guiPlayer.setName(name);
            }
            player2PlayerPanel.get(player).update();
        }
    }

    /**
     * This method is used to calculate the number of spaces a players token have to move on the gameboard
     * to get to the new position.
     *
     * @param player
     * @return
     * @author Nicolai J. Larsen
     */
    public int calcNumOfMoves(Player player) {
        Integer oldPos = player2position.get(player);
        if (oldPos == null) {
            oldPos = 0;
        }
        int curPos = player.getCurrentPosition().getIndex();
        int moves;
        if (curPos > oldPos) {
            moves = curPos - oldPos;
        } else {
            try {
                moves = (curPos - oldPos + 40) % 40;
            } catch (ArithmeticException e) {
                moves = 0;
            }
        }
        return moves;
    }

    /**
     * This method is used to remove the View-class as an observer for the game's Player- and Space-objects.
     */
    public void dispose() {
        if (!disposed) {
            disposed = true;
            for (Player player : game.getPlayers()) {
                // unregister from the player as observer
                player.detach(this);
            }
            for (Space space : game.getSpaces()) {
                space.detach(this);
            }
        }
    }

    /**
     * This method is used to create the games Player-objects in the GUI. It is only used when starting
     * a new game or loading a saved game.
     * @author Nicolai J. Larsen
     */
    public void initializeGUI() {
        for (Player player : game.getPlayers()) {
            if (player.getColorEnumType() != null) player.setColor(transformPlayerColor(player.getColorEnumType()));
            GUI_Car car = new GUI_Car(player.getColor(), Color.BLUE, transformCarType(player.getCarType()), GUI_Car.Pattern.FILL);
            GUI_Player guiPlayer = new GUI_Player(player.getName(), player.getBalance(), car);
            player2GuiPlayer.put(player, guiPlayer);
            gui.addPlayer(guiPlayer);

            if (player.getCurrentPosition() != null) {
                player2position.put(player, player.getCurrentPosition().getIndex());
            } else {
                player2position.put(player, 0);
            }
            PlayerPanel playerPanel = new PlayerPanel(game, player, gui);
            player2PlayerPanel.put(player, playerPanel);
            space2GuiField.get(player.getCurrentPosition()).setCar(player2GuiPlayer.get(player), true);
            player.attach(this);
        }

        for (Space space : game.getSpaces()) {
            if (space instanceof Property)
                if (((Property) space).getOwner() != null)
                updateProperty((Property) space);
        }
    }

    /**
     * This method is used for visualizing that the players token is moved stepwise.
     *
     * @author Nicolai J. Larsen
     */
    @Override
    public void run() {
        try {
            Thread.sleep(150);
        } catch (Exception e) {
        }
    }

    /**
     * This method is used to return a color from the Color-class, which corresponds
     * to the choice the player made, when choosing his color.
     *
     * @param playerColor
     * @return
     * @author Nicolai d T. Wulff
     */
    private Color transformPlayerColor(Player.PlayerColor playerColor) {
        switch (playerColor) {
            case GREY:
                return Color.DARK_GRAY;
            case GREEN:
                return Color.GREEN;
            case BlUE:
                return Color.BLUE.brighter();
            case MAGENTA:
                return Color.MAGENTA;
            case RED:
                return Color.RED;
            case YELLOW:
                return Color.yellow.brighter();
            default:
                return null;
        }
    }

    /**
     * This method is used to return an enum that corresponds to the choice of token
     * the player have chosen.
     *
     * @param carType
     * @return
     * @author Nicolai d T. Wulff
     */
    private GUI_Car.Type transformCarType(Player.CarType carType) {
        switch (carType) {
            case CAR:
                return GUI_Car.Type.CAR;
            case RACECAR:
                return GUI_Car.Type.RACECAR;
            case TRACTOR:
                return GUI_Car.Type.TRACTOR;
            case UFO:
                return GUI_Car.Type.UFO;
            default:
                return null;
        }
    }
}