package io.github.andrewsumsion.threepos;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.util.Map;

public abstract class Plugin {
    public abstract void run();
    public String getName() {
        for(Map.Entry<String, Plugin> entry : PluginLoader.getInstance().getPlugins().entrySet()) {
            if(entry.getValue().equals(this)) {
                return entry.getKey();
            }
        }
        return "";
    }

    protected void submitOrder(Order order) {
        ThreePOS.submitOrder(order);
    }
    protected File getPluginFolder() {
        File folder = new File("plugins", getName());
        if(!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }
}
