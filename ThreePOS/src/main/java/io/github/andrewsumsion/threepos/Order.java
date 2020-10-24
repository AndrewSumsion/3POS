package io.github.andrewsumsion.threepos;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String name;
    private String id;
    private List<Item> items;
    private boolean paid = false;
    private String source;

    public Order() {
        this("", "");
    }

    public Order(String name, String id) {
        this.name = name;
        this.id = id;
        this.items = new ArrayList<Item>();
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
