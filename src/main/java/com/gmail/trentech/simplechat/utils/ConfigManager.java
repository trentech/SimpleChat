package com.gmail.trentech.simplechat.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		init();
	}
	
	public ConfigManager() {
		String folder = "config" + File.separator + "simplechat";
        if (!new File(folder).isDirectory()) {
        	new File(folder).mkdirs();
        }
		file = new File(folder, "config.conf");
		
		create();
		load();
		init();
	}
	
	public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
		return loader;
	}

	public CommentedConfigurationNode getConfig() {
		return config;
	}

	public void save(){
		try {
			loader.save(config);
		} catch (IOException e) {
			Main.getLog().error("Failed to save config");
			e.printStackTrace();
		}
	}
	
	private void init() {
		if(file.getName().equalsIgnoreCase("config.conf")){
	        if(config.getNode("Options", "PM-Snoop").isVirtual()) {
	        	config.getNode("Options", "PM-Snoop").setValue(false);
	        }
	        if(config.getNode("Options", "World-Chat").isVirtual()) {
	        	config.getNode("Options", "World-Chat").setValue(false);
	        }
	        if(config.getNode("Options", "Ranged-Chat", "Enable").isVirtual()) {
	        	config.getNode("Options", "Ranged-Chat", "Enable").setValue(false);
	        	config.getNode("Options", "Ranged-Chat", "Range").setValue(32);
	        }

	        if(config.getNode("Broadcast", "Enable").isVirtual()) {
	        	config.getNode("Broadcast", "Enable").setValue(false);
	        	config.getNode("Broadcast", "Minutes").setValue(1);
	        		        	
	        	List<String> list = new ArrayList<String>(); 
	        	
	        	list.add("&eWelcome to &5SERVER NAME&e! This is an auto broacast!");
	        	list.add("&eBreak stone blocks with a pickaxe!");
	        	list.add("&eYou can delete these message and create your own!");
	        	list.add("&eCreate clickable link &u{url;www.google.com;&1Click Here}&e like so");
	        	list.add("&eCreate clickable command &u{cmd;/say hello world;&1Click Here}&e like so");
	        	list.add("&eCreate clickable suggested command &u{suggest;/say hello world;&1Click Here}&e like so");
	        	list.add("&eCreate hovered text &u{hover;&3secret text;&1Hover Here}&e like so");
	        	
	        	config.getNode("Broadcast", "Messages").setValue(list);     	
	        }
		}
		save();
	}

	private void create(){
		if(!file.exists()) {
			try {
				Main.getLog().info("Creating new " + file.getName() + " file...");
				file.createNewFile();		
			} catch (IOException e) {				
				Main.getLog().error("Failed to create new config file");
				e.printStackTrace();
			}
		}
	}
	
	private void load(){
		loader = HoconConfigurationLoader.builder().setFile(file).build();
		try {
			config = loader.load();
		} catch (IOException e) {
			Main.getLog().error("Failed to load config");
			e.printStackTrace();
		}
	}
}
