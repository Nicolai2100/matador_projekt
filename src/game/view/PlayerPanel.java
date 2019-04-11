package game.view;

import game.model.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @Malte, Nicolai L og Nicolai W
 */
public class PlayerPanel extends JFrame {
    private Game game;
    private Player player;
    private JPanel mainPanel;
    private Dimension dimension;
    private Dimension dimension2;
    private Map<ColorGroup, JPanel> maltesMap;

    public PlayerPanel(Game game, Player player) {
        this.game = game;
        this.player = player;
        dimension = new Dimension(72, 120);
        dimension2 = new Dimension(90, 20);

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        this.setContentPane(mainPanel);
        this.setLocation(710, game.getPlayers().indexOf(player) * 210);
        this.setSize(dimension);
        this.validate();
        this.setVisible(true);

        update();

        //todo Måske noget med en fængselsstatus
        //todo få panelerne til at se bedre ud
    }

    public void update() {
        mainPanel.removeAll();
        maltesMap = new HashMap<>();

        JPanel playerPanel = new JPanel();
        playerPanel.setBackground(Color.WHITE);
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        //playerPanel.setBorder(new EtchedBorder());
        playerPanel.setMaximumSize(dimension);
        playerPanel.setMinimumSize(dimension);
        playerPanel.setPreferredSize(dimension);

        JLabel nameLabel = new JLabel(player.getName());
        nameLabel.setMinimumSize(dimension2);
        nameLabel.setMaximumSize(dimension2);
        nameLabel.setPreferredSize(dimension2);

        JLabel balanceLabel = new JLabel("" + player.getBalance());
        balanceLabel.setMinimumSize(dimension2);
        balanceLabel.setMaximumSize(dimension2);
        balanceLabel.setPreferredSize(dimension2);

        JPanel colorPanel = new JPanel();
        colorPanel.setMinimumSize(dimension2);
        colorPanel.setMaximumSize(dimension2);
        colorPanel.setPreferredSize(dimension2);
        colorPanel.setBackground(player.getColor());

        playerPanel.add(colorPanel);
        playerPanel.add(nameLabel);
        playerPanel.add(balanceLabel);
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

        mainPanel.setPreferredSize(new Dimension(maltesMap.size() * 72, 100));
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
}
