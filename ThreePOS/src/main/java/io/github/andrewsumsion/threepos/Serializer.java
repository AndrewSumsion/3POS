package io.github.andrewsumsion.threepos;

public interface Serializer<T> {
    T serialize(Order order);
    Order deserialize(T input);
}
