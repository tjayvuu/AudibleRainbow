package com.example.acer.savehinhve;

/**
 * Created by vmt on 2/4/2018.
 */

public class ColorDescription {
    private int rgb;
    private String name;
    private String taste;
    private String feel;
    private String sound;

    public ColorDescription(int rgb, String name, String taste, String feel, String sound){
        this.rgb = rgb;
        this.name = name;
        this.taste = taste;
        this.feel = feel;
        this.sound = sound;
    }

    public String getDescription(){
        return feel + taste + sound;
    }

    public String getName(){return name;}

    public int getRGB(){return rgb;}

    // Red
    public static ColorDescription getRed() {
        return new ColorDescription(16711680, "Red",
                "If you have ever had a sun burn, your skin turns a red color. " +
                        "Or, if you have felt embarrassed and blushed, that heat on your cheeks " +
                        "looks red.",
                "Just like how you can feel red from feeling heat, you can also taste it " +
                        "when eating something hot and spicy.",
                "When you hear a siren, it is to cause people to be alert and pay " +
                        "attention right away, because there might be danger. Red is " +
                        "like that – it’s urgent and grabs your attention.");
    }

    // Green
    public static ColorDescription getGreen() {
        return new ColorDescription(65280,
                "Green",
                "Green smells like mint – fresh, clean, and healthy.",
                "The smoothness and suppleness of the leaves feels like green; " +
                                "green feels like life. But when the leaves are crispy like " +
                        "these other " +
                                "ones, they have turned brown and aren’t alive anymore.",
                        "When you hear trees rustling and birds singing, that is what " +
                                "green sounds like.");
    }

    // Blue
    public static ColorDescription getBlue(){
        return new ColorDescription(255,
                "Blue",
                "",
                         "How you feel when you’re swimming in water, the cool wetness " +
                                 "that feels relaxing, is how blue feels." +
                                 "Blue is calm and nice, like how the sound of water makes " +
                                "you feel relaxed",
                "The sound of running water, especially a stream bubbling or the " +
                        "ocean waves crashing, they make you think of blue.");
    }

    // Brown
    public static ColorDescription getBrown(){
        return new ColorDescription(9127187,
                "Brown",
                "","Brown feels like the earth, or the dead parts of things that " +
                        "grew out of the dirt from the earth.",
                "");

    }

    // Gray
    public static ColorDescription getGray(){
        return new ColorDescription(8421504,
                "Gray",
                "",
                "Grey is very hard and strong. It feels sturdy like a road under" +
                        " your feet, or the wall that you can lean against, but it isn’t" +
                        " alive and doesn’t grow or have feelings.",
                "Storms are grey. The sounds of the loud thunder and " +
                        "rain mean that it looks grey outside, it’s a bit dark and " +
                        "depressing because the sun isn’t out.");
    }

    // Yellow
    public static ColorDescription getYellow(){
        return new ColorDescription(16776960,
                "Yellow",
                "Lemons and bananas are the color yellow. Even though they are different " +
                        "flavors, both are yellow, and yellow can either taste sour and citrusy, " +
                        "or sweet and nourishing.",
                "Yellow foods also need lots of sun, they are bright and happy.",
                "");
    }

    // Orange
    public static ColorDescription getOrange(){
        return new ColorDescription(16753920,
                "Orange",
                "Oranges are usually refreshing, sweet, and tropical.",
                "The sun is orange, and many orange foods need a lot of sun to grow.",
                "");
    }
}
