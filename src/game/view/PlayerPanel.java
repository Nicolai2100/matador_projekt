package game.view;

import game.model.*;
import gui_main.GUI;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @Malte, Nicolai L og Nicolai W
 */
public class PlayerPanel extends JFrame implements MouseListener {
    private Game game;
    private GUI gui;
    private Player player;
    private JPanel mainPanel;
    private Dimension dimension;
    private Dimension smallPanelDimension;
    private Map<ColorGroup, JPanel> colorGroup2JPanel;
    private Map<JLabel, ColorGroup> nicolaisMap;

    public PlayerPanel(Game game, Player player, GUI gui) {
        this.game = game;
        this.gui = gui;
        this.player = player;
        dimension = new Dimension(80, 120);
        smallPanelDimension = new Dimension(64, 20);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle(player.getName());
        this.setLocation(710, game.getPlayers().indexOf(player) * 160);
        this.setSize(360, 120);
        this.setMinimumSize(new Dimension(360, 120));
        this.validate();
        this.setVisible(true);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        this.getContentPane().add(mainPanel);
        update();

        //todo Måske noget med en fængselsstatus
        //todo få panelerne til at se bedre ud
    }

    public void update() {
        mainPanel.removeAll();
        colorGroup2JPanel = new HashMap<>();
        nicolaisMap = new HashMap<>();

        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerPanel.setBorder(new EtchedBorder());
        playerPanel.setBackground(Color.WHITE);
        playerPanel.setMaximumSize(dimension);
        playerPanel.setMinimumSize(dimension);
        playerPanel.setPreferredSize(dimension);
        playerPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

        JLabel nameLabel = new JLabel(player.getName());
        nameLabel.setMinimumSize(smallPanelDimension);
        nameLabel.setMaximumSize(smallPanelDimension);
        nameLabel.setPreferredSize(smallPanelDimension);

        JLabel balanceLabel = new JLabel("" + player.getBalance());
        balanceLabel.setMinimumSize(smallPanelDimension);
        balanceLabel.setMaximumSize(smallPanelDimension);
        balanceLabel.setPreferredSize(smallPanelDimension);

        JPanel colorPanel = new JPanel();
        colorPanel.setMinimumSize(smallPanelDimension);
        colorPanel.setMaximumSize(smallPanelDimension);
        colorPanel.setPreferredSize(smallPanelDimension);
        colorPanel.setBackground(player.getColor());

        JLabel cardLabel = new JLabel();
        if (player.getOwnedCards().size() > 0) {
            cardLabel = new JLabel("Kort: " + player.getOwnedCards().size());
            cardLabel.setMinimumSize(smallPanelDimension);
            cardLabel.setMaximumSize(smallPanelDimension);
            cardLabel.setPreferredSize(smallPanelDimension);
        }


        playerPanel.add(colorPanel);
        playerPanel.add(nameLabel);
        playerPanel.add(balanceLabel);
        playerPanel.add(cardLabel);
        mainPanel.add(playerPanel);

        for (Space space : game.getSpaces()) {
            if (space instanceof Property) {
                Property property = (Property) space;
                if (property.getOwner() != null) {
                    if (property.getOwner() == player) {
                        ColorGroup colorGroup = property.getColorGroup();
                        if (!colorGroup2JPanel.containsKey(colorGroup))
                            try {
                                JPanel jPanel = panelMaker(colorGroup);
                                colorGroup2JPanel.put(colorGroup, jPanel);
                                labelMaker(jPanel, property);
                            } catch (NullPointerException e) {
                                e.getMessage();
                            }
                        else {
                            //Hvis mappet indeholder en værdi med denne farve skal den kun oprette et label
                            JPanel jPanel = colorGroup2JPanel.get(colorGroup);
                            labelMaker(jPanel, property);
                        }
                    }
                }
            }
        }
        HashSet<ColorGroup> groupsWithPawns = new HashSet<>();
        for (Property property : player.getOwnedProperties()) {
            if (property.getMortgaged()) {
                groupsWithPawns.add(property.getColorGroup());
            }
        }
        for (ColorGroup colorGroup : groupsWithPawns) {
            JPanel jPanel = colorGroup2JPanel.get(colorGroup);
            jPanel.add(new JLabel(" "));
            JLabel pawnedLabel = new JLabel("Pantsatte:");
            jPanel.add(pawnedLabel);
        }
        for (Property property : player.getOwnedProperties()) {
            ColorGroup colorGroup = property.getColorGroup();
            JPanel jPanel = colorGroup2JPanel.get(colorGroup);
            pawnedLabelMaker(jPanel, property);
        }

        mainPanel.setPreferredSize(new Dimension(colorGroup2JPanel.size() * 80 + 80, 100));
        this.pack();
        this.revalidate();
        this.repaint();
    }

    public JPanel panelMaker(ColorGroup color) {
        JPanel colorGroupPanel = new JPanel();
        colorGroupPanel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        colorGroupPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        colorGroupPanel.setBorder(new EtchedBorder());
        colorGroupPanel.setMaximumSize(dimension);
        colorGroupPanel.setPreferredSize(dimension);
        colorGroupPanel.setMinimumSize(dimension);
        colorGroupPanel.setVisible(true);

        JPanel colorPanel = new JPanel();
        colorPanel.setMinimumSize(new Dimension(64,20));
        colorPanel.setMaximumSize(new Dimension(64,20));
        colorPanel.setPreferredSize(new Dimension(64,20));
        colorPanel.setBackground(ColorGroup.colorGroupTransformer(color));
        colorGroupPanel.add(colorPanel);
//      colorGroupPanel.setBackground(ColorGroup.colorGroupTransformer(color));
        mainPanel.add(colorGroupPanel);
        return colorGroupPanel;
    }

    public void labelMaker(JPanel jPanel, Property property) {
        if (!property.getMortgaged()) {
            JLabel jLabel = new JLabel(property.getName());
/*
            jLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
*/
            nicolaisMap.put(jLabel, property.getColorGroup());
            jLabel.addMouseListener(this);
            jPanel.add(jLabel);
        }
    }

    public void pawnedLabelMaker(JPanel jPanel, Property property) {
        if (property.getMortgaged()) {
            JLabel jLabel = new JLabel(property.getName());
            nicolaisMap.put(jLabel, property.getColorGroup());
            jLabel.addMouseListener(this);
            jPanel.add(jLabel);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel jl = ((JLabel) e.getSource());
        Property property = null;
        for (Space s : game.getSpaces()) {
            if (s.getName().equals(jl.getText())) property = (Property) s;
        }
        gui.displayChanceCard("<div align=\"center\"><b>" + property.getName() + "</b></div>"
                + gui.getFields()[property.getIndex()].getDescription()
                + "<div align=\"center\">Pris: " + property.getCost() + "kr."
                + "<br>Pantsæt. værdi: " + property.getCost() / 2 + "kr.</div>");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //System.out.println("mouse pressed");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.println("mouse released");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel jl = ((JLabel) e.getSource());
        jl.setBackground(Color.white);
        jl.setOpaque(true);
        Font font = jl.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        jl.setFont(font.deriveFont(attributes));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JLabel jl = ((JLabel) e.getSource());
        //jl.setOpaque(false);
        jl.setBackground(ColorGroup.colorGroupTransformer(nicolaisMap.get(jl)));
        Font font = jl.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, -1);
        jl.setFont(font.deriveFont(attributes));
    }
}
