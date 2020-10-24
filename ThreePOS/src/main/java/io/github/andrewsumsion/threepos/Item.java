package io.github.andrewsumsion.threepos;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String name;
    private double price;
    private List<String> modifiers;
    private int quantity;

    public Item() {
        this("", 0);
    }

    public Item(String name, double price) {
        this(name, price, new ArrayList<String>());
    }

    public Item(String name, double price, List<String> modifiers) {
        this.name = name;
        this.price = price;
        this.modifiers = modifiers;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
