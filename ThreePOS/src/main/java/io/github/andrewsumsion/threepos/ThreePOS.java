package io.github.andrewsumsion.threepos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreePOS {
    private static BlockingQueue<Order> orders = new LinkedBlockingQueue<Order>();
    public static JsonNode config;

    public static void main(String[] args) {
        try {
            config = new ObjectMapper().readTree(new File("config.json"));
        } catch (IOException e) {
            System.out.println("Unable to read config.json!");
            throw new RuntimeException(e);
        }
        Menu.instantiate();
        PluginLoader.getInstance().loadPlugins(new File("plugins"));
        PluginLoader.getInstance().enablePlugins();
        PluginLoader.getInstance().loadTranslators();
        while(true) {
            if(Thread.currentThread().isInterrupted()) {
                break;
            }
            Order order;
            try {
                order = orders.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            try {
                sendOrder(order);
            } catch (IOException e) {
                System.out.println("Sending order " + order.getId() + " (" + order.getName() + ") failed!");
                e.printStackTrace();
                continue;
            }
        }
    }

    private static void sendOrder(Order order) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(config.get("server-ip").asText() + "/order");
        post.setEntity(new StringEntity(new JsonSerializer().serialize(order), ContentType.APPLICATION_JSON));
        client.execute(post);
    }

    public static void submitOrder(Plugin plugin, Order order) {
        Translator translator = Translators.getInstance().getTranslator(plugin.getName());
        for(int i = 0; i < order.getItems().size(); i++) {
            Item[] newItems = translator.translate(order.getItems().get(i));
            order.getItems().set(i, newItems[0]);
            if(newItems.length > 1) {
                for(int j = 1; j < newItems.length; j++) {
                    order.getItems().add(i + 1, newItems[j]);
                    i += 1;
                }
            }
        }

        try {
            orders.put(order);
        } catch (InterruptedException ignored) {

        }
    }
}
