package game.model;

/**
 * This is the color model for property colors
 *
 * @author Malte B. Kristensen,s185039@student.dtu.dk
 * @author Nicolai d T. Wulff,	s185036@student.dtu.dk
 * @author Neal P. Norman, 	s060527@student.dtu.dk
 *
 */

import java.awt.*;

public enum ColorGroup {

    lightblue(2),
    pink(3),
    green(3),
    darkgrey(3),
    red(3),
    white(3),
    yellow(3),
    purple(2),
    darkgreen(2),
    navy(4);

    private int numOfGroup;

    ColorGroup(int numOfGroup){
        this.numOfGroup = numOfGroup;
    }

    public int getNumOfGroup(){
        return this.numOfGroup;
    }
    //todo flyt til view
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
            case darkgreen: //Brewery
                return new Color(0, 100, 0);
            case navy: //Ferry
                return new Color(0, 0, 128);
        }
        return Color.black;
    }
}