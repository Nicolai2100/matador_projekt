package dk.dtu.compute.se.pisd.monopoly.mini.view;

import dk.dtu.compute.se.pisd.monopoly.mini.model.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @Malte og Nicolai L
 */
public class PlayerPanel extends JFrame {
    private Game game;
    private Player player;
    private JPanel mainPanel;
    private Dimension dimension;
    private Map<ColorGroup, JPanel> maltesMap;

    public PlayerPanel(Game game, Player player) {
        this.game = game;
        this.player = player;
        dimension = new Dimension(72, 100);

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        this.setContentPane(mainPanel);
        this.setLocation(710, game.getPlayers().indexOf(player) * 210);
        //this.setSize(800, 120);
        this.validate();
        this.setVisible(true);

        update();

        //Måske noget med en fængselsstatus
    }
    public void update() {
        mainPanel.removeAll();
        maltesMap = new HashMap<>();

        JPanel playerPanel = new JPanel();
        playerPanel.setBackground(player.getColor());
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setBorder(new EtchedBorder());
        JLabel nameLabel = new JLabel(player.getName());
        playerPanel.add(nameLabel);
        JLabel balanceLabel = new JLabel("" + player.getBalance());
        playerPanel.add(balanceLabel);
        playerPanel.setMaximumSize(dimension);
        playerPanel.setPreferredSize(dimension);
        mainPanel.add(playerPanel);

        for (Space space : game.getSpaces()) {
            if (space instanceof Property) {
                Property property = (Property) space;
                if (property.getOwner() != null) {
                    if (property.getOwner() == player) {
                        ColorGroup colorGroup = property.getColorGroup();
                        if (!maltesMap.containsKey(colorGroup))
                            try {
                                JPanel jPanel = panelMaker(colorGroup);
                                maltesMap.put(colorGroup, jPanel);
                                labelMaker(jPanel, property);
                            } catch (NullPointerException e) {
                                e.getMessage();
                            }
                        else {
                            //Hvis mappet indeholder en værdi med denne farve skal den kun oprette et label
                            JPanel jPanel = maltesMap.get(colorGroup);
                            labelMaker(jPanel, property);
                        }
                    }
                }
            }
        }

        //Således er der oprettet netop et panel for hver farvegruppe
      /*  for (ColorGroup colorGroup : ColorGroup.values()) {
            JPanel jPanel = panelMaker(colorGroup);
            maltesMap.put(colorGroup, jPanel);
        }*/


        HashSet<ColorGroup> groupsWithPawns = new HashSet<>();
        for (Property property : player.getOwnedProperties()) {
            if (property.getMortgaged()) {
                groupsWithPawns.add(property.getColorGroup());
            }
        }
        for (ColorGroup colorGroup : groupsWithPawns) {
            JPanel jPanel = maltesMap.get(colorGroup);
            jPanel.add(new JLabel(" "));
            JLabel pawnedLabel = new JLabel("Pantsatte:");
            jPanel.add(pawnedLabel);
        }
        for (Property property : player.getOwnedProperties()) {
            ColorGroup colorGroup = property.getColorGroup();
            JPanel jPanel = maltesMap.get(colorGroup);
            pawnedLabelMaker(jPanel, property);
        }

        mainPanel.setPreferredSize(new Dimension(maltesMap.size()*72 + 72, 100));
        this.pack();
        this.revalidate();
        this.repaint();
    }

    public JPanel panelMaker(ColorGroup color) {

        JPanel colorGroupPanel = new JPanel();
        colorGroupPanel.setBackground(ColorGroup.colorGroupTransformer(color));
        colorGroupPanel.setLayout(new BoxLayout(colorGroupPanel, BoxLayout.Y_AXIS));
        colorGroupPanel.setBorder(new EtchedBorder());
        colorGroupPanel.setMaximumSize(dimension);
        colorGroupPanel.setPreferredSize(dimension);
        colorGroupPanel.setVisible(true);
        mainPanel.add(colorGroupPanel);

        return colorGroupPanel;
    }

    public void labelMaker(JPanel jPanel, Property property) {
        if (!property.getMortgaged()) {
            JLabel jLabel = new JLabel(property.getName());
            jPanel.add(jLabel);
        }
    }

    public void pawnedLabelMaker(JPanel jPanel, Property property) {
        if (property.getMortgaged()) {
            JLabel jLabel = new JLabel(property.getName());
            jPanel.add(jLabel);
        }
    }
    /*Tilføj en metode update() til klassen, som først sletter den gamle inhold fra panelet
    og så kreirer nogle paneler på framet, som viser de forskellige informationer (se skematiske
    tegninger fra forelæsing 7).

Når I er lidt længere i opgaven, så kan I prøve at eksperimentere
 lidt med layoutet, farverne, og randen (border) for at finde ud af hvilken
  effekt de forskellige indstillinger har (see Java tutorials om layouts og borders).

*/

}
