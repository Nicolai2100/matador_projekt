package game.model;

import java.awt.*;

public enum PlayerColor {
    GREY,
    GREEN,
    BlUE,
    MAGENTA,
    RED,
    YELLOW;

    public Color getColor(PlayerColor playerColor) {
        switch (this) {
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
            default: return Color.WHITE;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case GREY: return "Grå";
            case GREEN: return "Grøn";
            case BlUE: return "Blå";
            case MAGENTA: return "Magenta";
            case RED: return "Rød";
            case YELLOW:return "Gul";
            default: return "Hvid";
        }
    }
}
