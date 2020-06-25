package io.github.andrewsumsion.threepos;

public abstract class Plugin {
    public abstract void run();
    protected void submitOrder(Order order) {
        ThreePOS.submitOrder(order);
    }
}
