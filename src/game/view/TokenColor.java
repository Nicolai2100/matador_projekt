package game.view;

import java.awt.*;
import java.util.HashMap;

/**
 * Nicolai L
 */
public class TokenColor {
    private HashMap<String, Color> colorMap;
    private List stringList;


    public TokenColor() {
        instaColorMap();
        instaStringList();
    }

    public Color colorChosen(String colorChoiceString) {
        Color returnColor = colorMap.get(colorChoiceString);
        updateColorStringSet(colorChoiceString);
        return returnColor;
    }

    public String setColorsToChooseFrom() {
        String returnString = "";
        for (int i = 0; i < stringList.getItemCount(); i++) {
            returnString = returnString + stringList.getItem(i) + " ";
        }
        return returnString;
    }

    public void updateColorStringSet(String lastChosen) {
        stringList.remove(lastChosen);
    }

    private void instaColorMap() {
        colorMap = new HashMap<>();
        colorMap.put("Grey", Color.GRAY);
        colorMap.put("Green", Color.GREEN);
        colorMap.put("Blue", Color.BLUE.brighter());
        colorMap.put("Magenta", Color.MAGENTA);
        colorMap.put("Red", Color.RED);
        colorMap.put("Yellow", Color.yellow.brighter());
    }

    private void instaStringList() {
        stringList = new List();
        stringList.add("Grey");
        stringList.add("Green");
        stringList.add("Blue");
        stringList.add("Magenta");
        stringList.add("Red");
        stringList.add("Yellow");
    }
}
