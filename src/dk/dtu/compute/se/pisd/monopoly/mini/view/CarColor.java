package dk.dtu.compute.se.pisd.monopoly.mini.view;

import java.awt.*;
import java.util.HashMap;

public class CarColor {
    private HashMap<String, Color> colorMap;
    private List stringList;


    public CarColor() {
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
        colorMap.put("Grey", Color.DARK_GRAY);
        colorMap.put("Green", Color.GREEN);
        colorMap.put("Blue", Color.BLUE);
        colorMap.put("Magenta", Color.MAGENTA);
    }

    private void instaStringList() {
        stringList = new List();
        stringList.add("Grey");
        stringList.add("Green");
        stringList.add("Blue");
        stringList.add("Magenta");
    }
}
