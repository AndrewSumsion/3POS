package io.github.andrewsumsion.threepos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

public class PluginLoader {
    private static PluginLoader INSTANCE;

    public static PluginLoader getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PluginLoader();
        }
        return INSTANCE;
    }

    private PluginLoader() {

    }

    private Map<String, Plugin> plugins = new HashMap<String, Plugin>();
    private Map<String, Thread> threads = new HashMap<String, Thread>();

    public void loadPlugins(File folder) {
        if(!folder.isDirectory()) return;
        File[] files = folder.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jar");
            }
        });
        for(File file : files) {
            try {
                loadPlugin(file);
            }
            catch (Exception e) {
                System.out.println("An error occurred loading " + file.getName());
            }
        }
    }

    public Plugin loadPlugin(File file) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        JarFile jarFile = new JarFile(file);
        InputStream configStream = jarFile.getInputStream(jarFile.getEntry("plugin.json"));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(configStream);
        String name = jsonNode.get("name").asText();
        String main = jsonNode.get("main").asText();
        URLClassLoader child = new URLClassLoader(
                new URL[] {file.toURI().toURL()},
                this.getClass().getClassLoader()
        );
        Class pluginClass = Class.forName(main, true, child);
        Plugin plugin = (Plugin) pluginClass.getConstructor().newInstance();
        plugins.put(name, plugin);
        return plugin;
    }

    public void enablePlugins() {
        for(final Map.Entry<String, Plugin> entry : plugins.entrySet()) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    entry.getValue().run();
                }
            });
            thread.start();
            threads.put(entry.getKey(), thread);
        }
    }

    public Map<String, Plugin> getPlugins() {
        return plugins;
    }
}
