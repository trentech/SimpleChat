package com.gmail.trentech.simplechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.simplechat.commands.CommandManager;
import com.gmail.trentech.simplechat.utils.Broadcast;
import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Resource;
import com.gmail.trentech.simplechat.utils.SQLUtils;

import ninja.leaping.configurate.ConfigurationNode;

@Plugin(id = Resource.ID, name = Resource.NAME, dependencies = "after:SimpleTags", version = Resource.VERSION)
public class Main {

	private static Game game;
	private static Logger log;
	private static PluginContainer plugin;

	private static List<String> broadcasts = new ArrayList<>();
	private static HashMap<String, String> reply = new HashMap<String, String>();
	
	@Listener
    public void onPreInitializationEvent(GamePreInitializationEvent event) {
		game = Sponge.getGame();
		plugin = getGame().getPluginManager().getPlugin(Resource.ID).get();
		log = getGame().getPluginManager().getLogger(plugin);
	}

	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		Optional<PluginContainer> optionalPlugin = game.getPluginManager().getPlugin("SimpleTags");
		
		if(!optionalPlugin.isPresent()){
			getLog().error("SimpleTags not found! SimpleChat disabled");
			return;
		}
		PluginContainer plugin = optionalPlugin.get();
		
		String[] version = plugin.getVersion().split("\\.");

		if(Integer.parseInt(version[2]) < 2){
			getLog().error("SimpleChat requires SimpleTags v0.1.2 or above");
			return;
		}
		
		getGame().getEventManager().registerListeners(this, new EventListener());
		
		getGame().getCommandManager().register(this, new CommandManager().cmdBroadcast, "broadcast", "b");
		getGame().getCommandManager().register(this, new CommandManager().cmdChannel, "channel", "ch");
		getGame().getCommandManager().register(this, new CommandManager().cmdChat, "chat", "cm");
		getGame().getCommandManager().register(this, new CommandManager().cmdGlobal, "global", "g");
		getGame().getCommandManager().register(this, new CommandManager().cmdMail, "mail", "ml");
		getGame().getCommandManager().register(this, new CommandManager().cmdMessage, "message", "msg");
		getGame().getCommandManager().register(this, new CommandManager().cmdMute, "mute", "m");
		getGame().getCommandManager().register(this, new CommandManager().cmdReply, "reply", "r");
		getGame().getCommandManager().register(this, new CommandManager().cmdSay, "say", "s");
		
		ConfigurationNode config = new ConfigManager().getConfig();
		
		broadcasts = config.getNode("Broadcast", "Messages").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		
		if(config.getNode("Broadcast", "Enable").getBoolean()){
			new Broadcast().start(config);
		}
		
		SQLUtils.createTable();
	}

	public static Game getGame() {
		return game;
	}

	public static Logger getLog() {
		return log;
	}

	public static PluginContainer getPlugin() {
		return plugin;
	}

	public static List<String> getBroadcasts() {
		return broadcasts;
	}

	public static HashMap<String, String> getReply() {
		return reply;
	}
}
