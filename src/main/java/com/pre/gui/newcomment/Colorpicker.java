package com.pre.gui.newcomment;

import com.pre.model.ChatBubbleColor;

import java.awt.*;
import java.io.Serializable;

public class Colorpicker implements Serializable {

    public static ChatBubbleColor colorPicker(String sender) {

        // users will be assigned a color based on their name
        switch (sender) {
            case "name":
                return new ChatBubbleColor(Color.decode("#fa7500"), Color.decode("#ffd9b8"));
            case "name2":
                return new ChatBubbleColor(Color.decode("#a39e00"), Color.decode("#fffdd1"));
            case "name3":
                return new ChatBubbleColor(Color.decode("#638a00"), Color.decode("#f1ffcc"));
            case "name4":
                return new ChatBubbleColor(Color.decode("#006b29"), Color.decode("#d1ffe3"));
            case "name5":
                return new ChatBubbleColor(Color.decode("#007575"), Color.decode("#dbffff"));
            case "name6":
                return new ChatBubbleColor(Color.decode("#004b7a"), Color.decode("#d1edff"));
            case "name7":
                return new ChatBubbleColor(Color.decode("#001799"), Color.decode("#cbcfe6"));
            case "name8":
                return new ChatBubbleColor(Color.decode("#390179"), Color.decode("#ecdbff"));
            case "name9":
                return new ChatBubbleColor(Color.decode("#690202"), Color.decode("#d5a4a4"));

            default:
                return new ChatBubbleColor(Color.decode("#690269"), Color.decode("#ffe0ff"));
        }
    }
}
