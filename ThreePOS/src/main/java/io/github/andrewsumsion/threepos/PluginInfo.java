package io.github.andrewsumsion.threepos;

import java.io.File;

public class PluginInfo {
    private String name;
    private String main;
    private String[] dependencies;
    private File jarFile;

    public PluginInfo(String name, String main, String[] dependencies, File jarFile) {
        this.name = name;
        this.main = main;
        this.dependencies = dependencies;
        this.jarFile = jarFile;
    }

    public String getName() {
        return name;
    }

    public String getMain() {
        return main;
    }

    public File getJarFile() {
        return jarFile;
    }

    public String[] getDependencies() {
        return dependencies;
    }
}
