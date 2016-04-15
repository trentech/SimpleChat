package com.gmail.trentech.simplechat;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.commands.CommandManager;
import com.gmail.trentech.simplechat.utils.Broadcast;
import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Resource;
import com.gmail.trentech.simplechat.utils.SQLUtils;

import me.flibio.updatifier.Updatifier;
import ninja.leaping.configurate.ConfigurationNode;

@Updatifier(repoName = "SimpleChat", repoOwner = "TrenTech", version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, authors = Resource.AUTHOR, url = Resource.URL, description = Resource.DESCRIPTION, dependencies = {@Dependency(id = "Updatifier", optional = true), @Dependency(id = "com.gmail.trentech.simpletags", version = "0.2.5")})
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
		log = getPlugin().getLogger();
	}

	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
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
	
	public static Text getBroadcast(String msg){
    	Text message = Text.EMPTY;

    	while(msg.contains("&u")){
    		message = Text.join(message, TextSerializers.FORMATTING_CODE.deserialize(msg.substring(0, msg.indexOf("&u{")).replace("&u{", "")));

    		String work = msg.substring(msg.indexOf("&u{"), msg.indexOf("}")).replaceFirst("&u\\{", "").replaceFirst("}", "");
		
    		message = Text.join(message, getLink(work));

    		msg = msg.substring(msg.indexOf("}"), msg.length()).replaceFirst("}", "");
    	}
    	
    	return Text.of(message, TextSerializers.FORMATTING_CODE.deserialize(msg));
	}

	public static HashMap<String, String> getReply() {
		return reply;
	}
	
    private static Text getLink(String link){
    	Text.Builder builder = Text.builder();
    	String[] work = link.split(";");
		if(work[0].equalsIgnoreCase("url")){
			if(!work[1].toLowerCase().contains("http://") || !work[1].toLowerCase().contains("https://")){
				work[1] = "http://" + work[1];	
			}
			URL url = null;
			try {
				url = new URL(work[1]);
				builder.onClick(TextActions.openUrl(url)).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));					
			} catch (MalformedURLException e) {}
		}else if(work[0].equalsIgnoreCase("cmd")){
			builder.onClick(TextActions.runCommand(work[1])).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));							
		}else if(work[0].equalsIgnoreCase("suggest")){
			builder.onClick(TextActions.suggestCommand(work[1])).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
		}else if(work[0].equalsIgnoreCase("hover")){
			builder.onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize(work[1]))).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
		}
		return builder.build();
    }
}
