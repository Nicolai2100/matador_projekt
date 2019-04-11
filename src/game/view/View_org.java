package game.view;

import designpattern.Observer;
import designpattern.Subject;
import game.model.Game;
import game.model.Player;
import game.model.Property;
import game.model.Space;
import game.model.properties.RealEstate;
import gui_fields.*;
import gui_fields.GUI_Car.Pattern;
import gui_fields.GUI_Car.Type;
import gui_main.GUI;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Dette er den oprindelige View klasse - bare for overblikkets skyld - skal slettes senere
 */
public class View_org implements Observer {

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
    public View_org(Game game, GUI gui) {
        this.game = game;
        this.gui = gui;

        for (Player player : game.getPlayers()) {
            PlayerPanel playerPanel = new PlayerPanel(game, player);
            player2PlayerPanel.put(player, playerPanel);
        }

        GUI_Field[] guiFields = gui.getFields();

        int i = 0;
        for (Space space : game.getSpaces()) {
            // TODO, here we assume that the games fields fit to the GUI's fields;
            // the GUI fields should actually be created according to the game's
            // fields
            space2GuiField.put(space, guiFields[i++]);
            space.attach(this);
        }

        // create the players in the GUI
        for (Player player : game.getPlayers()) {
            GUI_Car car = new GUI_Car(player.getColor(), Color.black, Type.CAR, Pattern.FILL);
            GUI_Player guiPlayer = new GUI_Player(player.getName(), player.getBalance(), car);
            player2GuiPlayer.put(player, guiPlayer);
            gui.addPlayer(guiPlayer);
            // player2position.put(player, 0);

            // register this view with the player as an observer, in order to update the
            // player's state in the GUI
            player.attach(this);

            updatePlayer(player);
        }
    }
    @Override
    public void update(Subject subject) {
        if (!disposed) {
            if (subject instanceof Player) {
                updatePlayer((Player) subject);
            }
            if (subject instanceof Property) {
                updateProperty((Property) subject);
            }
        }

        // TODO update other subjects in the GUI
        //      in particular properties (sold, houses, ...)

        //update(Subject subject) opdaterer grunde og viser deres ejere
        //med at indramme grunde med ejerens farve og også vise antalet af huse.
    }
    public void updateProperty(Property property) {
        GUI_Field gui_field = this.space2GuiField.get((Property) property);
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
        }
        if (property instanceof RealEstate) {
            GUI_Street street = (GUI_Street) gui_field;
            street.setHouses(((RealEstate) property).getHouseCount());
        }
    }

    /**
     * This method updates a player's state in the GUI. Right now, this
     * concerns the players position and balance only. But, this should
     * also include other information (being i prison, available cards,
     * ...)
     *
     * @param player the player who's state is to be updated
     */
    private void updatePlayer(Player player) {
        GUI_Player guiPlayer = this.player2GuiPlayer.get(player);
        if (guiPlayer != null) {
            guiPlayer.setBalance(player.getBalance());

            GUI_Field[] guiFields = gui.getFields();
            Integer oldPosition = player2position.get(player);
            if (oldPosition != null && oldPosition < guiFields.length) {
                guiFields[oldPosition].setCar(guiPlayer, false);
            }
            int pos = player.getCurrentPosition().getIndex();
            if (pos < guiFields.length) {
                player2position.put(player, pos);
                guiFields[pos].setCar(guiPlayer, true);
            }

            String name = player.getName();
            if (player.isBroke()) {
            } else if (player.isInPrison()) {
                guiPlayer.setName(player.getName() + " (in prison)");
            }
            if (!name.equals(guiPlayer.getName())) {
                guiPlayer.setName(name);
            }
            player2PlayerPanel.get(player).update();
        }
    }

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
}