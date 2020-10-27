package io.github.andrewsumsion.threepos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
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

    private ClassLoader pluginClassLoader = null;
    private Map<String, Plugin> plugins = new HashMap<>();
    private Map<String, PluginInfo> pluginInfo = new HashMap<>();
    private Map<String, Thread> threads = new HashMap<>();

    public void loadPlugins(File folder) {
        registerPlugins(folder);
        for(String name : pluginInfo.keySet()) {
            try {
                loadPlugin(name);
            } catch (Exception e) {
                System.out.println("An error occurred while loading " + name);
                e.printStackTrace();
            }
        }
    }

    public void loadPlugin(String name) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        PluginInfo pluginInfo = this.pluginInfo.get(name);

        Class pluginClass = Class.forName(pluginInfo.getMain(), true, pluginClassLoader);
        if(!Plugin.class.isAssignableFrom(pluginClass)) {
            System.out.println("Error enabling " + name + ": Main class does not extend Plugin");
        }
        Plugin plugin = (Plugin) pluginClass.getConstructor().newInstance();

        plugins.put(name, plugin);
    }

    private void registerPlugins(File folder) {
        File[] files = folder.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jar");
            }
        });
        URL[] urls = new URL[files.length];
        for(int i = 0; i < files.length; i++) {
            try {
                urls[i] = files[i].toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        pluginClassLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
        for(File file : files) {
            try {
                registerPlugin(file);
            } catch (IOException e) {
                System.out.println("An error occurred while loading " + file.getName());
                e.printStackTrace();
            }
        }

        while(true) {
            int removedCounter = 0;
            for (Iterator<Map.Entry<String, PluginInfo>> iter = this.pluginInfo.entrySet().iterator(); iter.hasNext();) {
                Map.Entry<String, PluginInfo> entry = iter.next();
                PluginInfo pluginInfo = this.pluginInfo.get(entry.getKey());

                List<String> missingDependencies = getMissingDependencies(pluginInfo.getDependencies());
                if(missingDependencies.size() > 0) {
                    iter.remove();
                    removedCounter += 1;
                    System.out.println("Error while enabling plugin " + entry.getKey() + ": Missing dependencies: " + String.join(", ", missingDependencies));
                }
            }
            if(removedCounter == 0) {
                break;
            }
        }
    }

    private void registerPlugin(File file) throws IOException {
        JarFile jarFile = new JarFile(file);
        InputStream configStream = jarFile.getInputStream(jarFile.getEntry("plugin.json"));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(configStream);
        String name = jsonNode.path("name").asText();
        String main = jsonNode.path("main").asText();
        int dependencyCount = jsonNode.path("depends").size();
        String[] dependencies = new String[dependencyCount];
        for(int i = 0; i < dependencyCount; i++) {
            dependencies[i] = jsonNode.path("depends").path(i).asText();
        }
        PluginInfo pluginInfo = new PluginInfo(name, main, dependencies, file);
        this.pluginInfo.put(name, pluginInfo);
    }

    public void enablePlugins() {
        for(String name : pluginInfo.keySet()) {
            enablePlugin(name);
        }
    }

    private void sortPlugins(List<String> pluginNames) {
        while (true) {
            for (int i = 0; i < pluginNames.size(); i++) {

            }
        }
    }

    public void enablePlugin(String name) {
        PluginInfo pluginInfo = this.pluginInfo.get(name);

        Plugin plugin = plugins.get(name);

        Thread thread = new Thread(new Runnable() {
            public void run() {
                plugin.run();
            }
        });
        thread.start();
        threads.put(name, thread);
    }

    private List<String> getMissingDependencies(String[] dependencies) {
        List<String> result = new ArrayList<>();
        for(String dependency : dependencies) {
            if(!this.pluginInfo.containsKey(dependency)) {
                result.add(dependency);
            }
        }
        return result;
    }

    public void loadTranslators() {
        for(Plugin plugin : plugins.values()) {
            File translationFile = new File(plugin.getPluginFolder(), "translations.yml");
            Translators.getInstance().registerTranslator(plugin.getName(), new Translator(translationFile));
        }
    }

    public Map<String, Plugin> getPlugins() {
        return plugins;
    }

    public void disablePlugin(String name) {
        threads.get(name).interrupt();
    }
}
