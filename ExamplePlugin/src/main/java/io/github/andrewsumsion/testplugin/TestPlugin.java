package io.github.andrewsumsion.testplugin;

import io.github.andrewsumsion.threepos.JsonSerializer;
import io.github.andrewsumsion.threepos.Order;
import io.github.andrewsumsion.threepos.Plugin;

import java.util.Scanner;

public class TestPlugin extends Plugin {
    public void run() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            Order order = new JsonSerializer().deserialize(scanner.nextLine());
            submitOrder(order);
        }
    }
}
