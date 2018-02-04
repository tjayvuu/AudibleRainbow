package com.example.acer.savehinhve;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vmt on 2/4/2018.
 */

public class BasicColors {
    private static Map<Integer, List<String>> descriptionByColor = basicColorsMap();
    private static String closestColorName;
    private static Map<Integer, List<String>> basicColorsMap() {
        Map<Integer, List<String>> descriptionByColor = new HashMap<>();
        // create all basic color description objects
        ColorDescription redDes = ColorDescription.getRed();
        ColorDescription greenDes = ColorDescription.getGreen();
        ColorDescription blueDes = ColorDescription.getBlue();
        ColorDescription brownDes = ColorDescription.getBrown();
        ColorDescription yellowDes = ColorDescription.getYellow();
        ColorDescription orangeDes = ColorDescription.getOrange();
        ColorDescription grayDes = ColorDescription.getGray();

        // put color objects to map
        descriptionByColor.put(redDes.getRGB(),
                Arrays.asList(redDes.getName(), redDes.getDescription()));
        descriptionByColor.put(greenDes.getRGB(),
                Arrays.asList(greenDes.getName(),greenDes.getDescription()));
        descriptionByColor.put(blueDes.getRGB(),
                Arrays.asList(blueDes.getName(), blueDes.getDescription()));
        descriptionByColor.put(brownDes.getRGB(),
                Arrays.asList(brownDes.getName(), brownDes.getDescription()));
        descriptionByColor.put(yellowDes.getRGB(),
                Arrays.asList(yellowDes.getName(), yellowDes.getDescription()));
        descriptionByColor.put(orangeDes.getRGB(),
                Arrays.asList(orangeDes.getName(), orangeDes.getDescription()));
        descriptionByColor.put(grayDes.getRGB(),
                Arrays.asList(greenDes.getName(), grayDes.getDescription()));

        return descriptionByColor;

    }

    public static String getClosestColorDescription(int rgb) {
        String closest_color_description = "";
        List<String> closest_set = new ArrayList<>();
        Set<Integer> rgbSet = descriptionByColor.keySet();
        double lowest_difference = 100000000;
        Log.d("RGBTAG", "" + rgb);
        Log.d("RGBTAG222", "" + rgb);
        for (Integer i : rgbSet){
            Log.d("RGBTAG222", "" + i);
            double current_diff = ColorUtils.getColorDifference(rgb, i);
            Log.d("RGBTAG333", "" + current_diff);

            if (current_diff < lowest_difference) {
                lowest_difference = current_diff;
                closest_set = descriptionByColor.get(i);
                closest_color_description = closest_set.get(1);
                closestColorName = closest_set.get(0);
            }
        }
        return closest_color_description;
    }

    public static String getClosestColorName() {return closestColorName;}

}
