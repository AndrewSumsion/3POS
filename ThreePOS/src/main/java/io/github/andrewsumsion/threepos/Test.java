package io.github.andrewsumsion.threepos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws JsonProcessingException {
        Order order = new Order("Andrew Sumsion", "1");
        order.getItems().add(new Item("Fried Chicken", 8.5));
        order.getItems().add(new Item("Hamburger", 7.25, Arrays.asList("No Mayo")));
        JsonSerializer serializer = new JsonSerializer();
        String json = serializer.serialize(order);
        System.out.println(json);
        System.out.println(serializer.serialize(serializer.deserialize(json)));
    }
}
