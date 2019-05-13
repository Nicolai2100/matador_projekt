package game.model;

import java.awt.*;

public enum PlayerColor {
    GREY,
    GREEN,
    BlUE,
    MAGENTA,
    RED,
    YELLOW;
/*
    public static Color getColor(PlayerColor playerColor) {
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
            default: return null;
        }
    }
*/
    @Override
    public String toString() {
        switch (this) {
            case GREY: return "Grå";
            case GREEN: return "Grøn";
            case BlUE: return "Blå";
            case MAGENTA: return "Magenta";
            case RED: return "Rød";
            case YELLOW:return "Gul";
            default: return null;
        }
    }

    public static PlayerColor getColor(String color) {
        switch (color) {
            case "Grå": return GREY;
            case "Grøn": return GREEN;
            case "Blå": return BlUE;
            case "Magenta": return MAGENTA;
            case "Rød": return RED;
            case "Gul": return YELLOW;
            default: return null;
        }
    }

    public Color getRGBcolor(PlayerColor playerColor) {
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
            default: return null;
        }
    }

    public PlayerColor getColorFromRGB(int colorRGB) {

        Color color = new Color(colorRGB);

        switch (color) {
            case (Color.DARK_GRAY): return PlayerColor.GREY;
        }
    }
}
