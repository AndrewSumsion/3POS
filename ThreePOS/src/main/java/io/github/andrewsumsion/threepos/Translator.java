package io.github.andrewsumsion.threepos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Translator {
    private JsonNode config;

    public Translator(JsonNode config) {
        this.config = config;
    }

    public Translator(File configFile) {
        try {
            this.config = new ObjectMapper(new YAMLFactory()).readTree(configFile);
        } catch (IOException e) {
            this.config = NullNode.getInstance();
        }
    }

    public Item[] translate(Item item) {
        if(!config.fieldNames().hasNext()) {
            return new Item[]{item};
        }

        Item input = new Item();
        input.setName(item.getName());
        input.setQuantity(item.getQuantity());
        input.setModifiers(new ArrayList<>(item.getModifiers()));
        input.setPrice(item.getPrice());

        String originalName = null;
        int smallestDistance = 0;
        for (Iterator<String> iter = config.fieldNames(); iter.hasNext();) {
            String fieldName = iter.next();
            String itemName = fieldName.split("\\.")[0];
            int distance = levDistance(input.getName().toLowerCase(), itemName.toLowerCase());
            if(originalName == null) {
                originalName = itemName;
                smallestDistance = distance;
                continue;
            }
            if(distance < smallestDistance) {
                originalName = itemName;
                smallestDistance = distance;
            }
        }

        int mostMatches = -1;
        String key = null;

        for (Iterator<String> iter = config.fieldNames(); iter.hasNext();) {
            String fieldName = iter.next();
            if(!fieldName.startsWith(originalName)) {
                continue;
            }
            int matches = 0;
            String[] modifiers = fieldName.split("\\.");
            for(int i = 1; i < modifiers.length; i++) {
                String modifier = modifiers[i];
                if(input.getModifiers().contains(modifier)) {
                    matches += 1;
                }
            }

            if(matches > mostMatches) {
                mostMatches = matches;
                key = fieldName;
            }
        }

        String[] modifiers = key.split("\\.");

        for(int i = 1; i < modifiers.length; i++) {
            String modifier = modifiers[i];
            input.getModifiers().remove(modifier);
        }

        int mappingSize = config.get(key).size();
        if(mappingSize > 0) {
            Item[] result = new Item[mappingSize];
            input.setName(config.get(key).get(0).asText());
            applyNewModifiers(input);
            result[0] = input;
            for(int i = 1; i < mappingSize; i++) {
                result[i] = new Item(config.get(key).get(i).asText(), 0.0);
                applyNewModifiers(result[i]);
            }
            return result;
        } else {
            input.setName(config.get(key).asText());
            applyNewModifiers(input);
            return new Item[] {input};
        }
    }

    private void applyNewModifiers(Item item) {
        String[] newModifiers = item.getName().split("\\.");
        item.setName(newModifiers[0]);
        for(int i = 1; i < newModifiers.length; i++) {
            item.getModifiers().add(0, newModifiers[i]);
        }
    }

    private int levDistance(String left, String right) {
        return LevenshteinDistance.getDefaultInstance().apply(left, right);
    }
}
