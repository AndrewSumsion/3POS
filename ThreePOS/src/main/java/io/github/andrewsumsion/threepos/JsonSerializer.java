package io.github.andrewsumsion.threepos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializer implements Serializer<String> {
    public String serialize(Order order) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Order deserialize(String input) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(input, Order.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
