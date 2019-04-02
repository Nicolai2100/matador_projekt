package dk.dtu.compute.se.pisd.monopoly.mini.model;


import java.awt.*;

public enum ColorGroup {

    lightblue,
    pink,
    green,
    darkgrey,
    red,
    white,
    yellow,
    purple,
    darkgreen,
    navy;

    public static Color colorGroupTransformer(ColorGroup e) {

        switch (e) {
            case lightblue:
                return new Color(75, 155, 225);
            case pink:
                return new Color(255, 135, 120);
            case green:
                return new Color(102, 204, 0);
            case darkgrey:
                return new Color(153, 153, 153);
            case red:
                return Color.red;
            case white:
                return Color.white;
            case yellow:
                return new Color(255, 255, 50);
            case purple:
                return new Color(150, 60, 150);
            case darkgreen:
                return new Color(0, 100, 0);
            case navy:
                return new Color(0, 0, 128);
        }
        return Color.black;
    }
}