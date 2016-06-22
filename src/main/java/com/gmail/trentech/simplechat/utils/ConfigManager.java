package com.gmail.trentech.simplechat.utils;

import java.io.File;
import java.io.IOException;

import com.gmail.trentech.simplechat.Main;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigManager {

	private File file;
	private CommentedConfigurationNode config;
	private ConfigurationLoader<CommentedConfigurationNode> loader;

	public ConfigManager(String configName) {
		String folder = "config" + File.separator + "simplechat";
		
		if (!new File(folder).isDirectory()) {
			new File(folder).mkdirs();
		}
		file = new File(folder + configName);

		create();
		load();
	}

	public ConfigManager() {
		String folder = "config" + File.separator + "simplechat";
		
		if (!new File(folder).isDirectory()) {
			new File(folder).mkdirs();
		}
		file = new File(folder, "config.conf");

		create();
		load();
	}

	public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
		return loader;
	}

	public CommentedConfigurationNode getConfig() {
		return config;
	}

	public void save() {
		try {
			loader.save(config);
		} catch (IOException e) {
			Main.getLog().error("Failed to save config");
			e.printStackTrace();
		}
	}

	public void init() {
		if (file.getName().equalsIgnoreCase("config.conf")) {
			if (config.getNode("options", "pm_snoop").isVirtual()) {
				config.getNode("options", "pm_snoop").setValue(false);
			}
			if (config.getNode("options", "world_chat").isVirtual()) {
				config.getNode("options", "world_chat").setValue(false);
			}
			if (config.getNode("options", "ranged_chat", "enable").isVirtual()) {
				config.getNode("options", "ranged_chat", "enable").setValue(false);
				config.getNode("options", "ranged_chat", "range").setValue(32);
			}
		}
		save();
	}

	private void create() {
		if (!file.exists()) {
			try {
				Main.getLog().info("Creating new " + file.getName() + " file...");
				file.createNewFile();
			} catch (IOException e) {
				Main.getLog().error("Failed to create new config file");
				e.printStackTrace();
			}
		}
	}

	private void load() {
		loader = HoconConfigurationLoader.builder().setFile(file).build();
		try {
			config = loader.load();
		} catch (IOException e) {
			Main.getLog().error("Failed to load config");
			e.printStackTrace();
		}
	}
}
