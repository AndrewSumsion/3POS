package io.github.andrewsumsion.threepos;

import java.util.List;

public class Menu {
    private static Menu INSTANCE;

    public static Menu getInstance() {
        return INSTANCE;
    }

    public static void instantiate() {

    }

    private List<Item> menuItems;

    public List<Item> getMenuItems() {
        return menuItems;
    }
}
