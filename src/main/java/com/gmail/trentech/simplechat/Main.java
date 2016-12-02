package com.gmail.trentech.simplechat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.commands.CommandManager;
import com.gmail.trentech.simplechat.commands.channel.CMDTagChannel;
import com.gmail.trentech.simplechat.data.ChannelTag;
import com.gmail.trentech.simplechat.listeners.EventListener;
import com.gmail.trentech.simplechat.listeners.TagListener;
import com.gmail.trentech.simplechat.utils.CommandHelp;
import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Resource;
import com.gmail.trentech.simplechat.utils.SQLUtils;
import com.google.inject.Inject;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = Resource.NAME, repoOwner = Resource.AUTHOR, version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, description = Resource.DESCRIPTION, authors = Resource.AUTHOR, url = Resource.URL, dependencies = { @Dependency(id = "Updatifier", optional = true), @Dependency(id = "simpletags", optional = true), @Dependency(id = "helpme", version = "0.2.1", optional = true) })
public class Main {

	@Inject @ConfigDir(sharedRoot = false)
    private Path path;

	@Inject
	private Logger log;

	private static PluginContainer plugin;
	private static Main instance;
	
	@Listener
	public void onPreInitializationEvent(GamePreInitializationEvent event) {
		plugin = Sponge.getPluginManager().getPlugin(Resource.ID).get();
		instance = this;
		
		try {			
			Files.createDirectories(path);		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		if (Sponge.getPluginManager().isLoaded("simpletags")) {
			Sponge.getEventManager().registerListeners(this, new TagListener());

			com.gmail.trentech.simpletags.Main.registerTag(ChannelTag.class);
			com.gmail.trentech.simpletags.Main.registerCommand(CMDTagChannel.cmd, "channel", "ch");
		}
		
		ConfigManager.init();

		Sponge.getEventManager().registerListeners(this, new EventListener());

		CommandManager commandManager = new CommandManager();

		Sponge.getCommandManager().register(this, commandManager.cmdChannel, "channel", "ch");
		Sponge.getCommandManager().register(this, commandManager.cmdChat, "chat", "cm");
		Sponge.getCommandManager().register(this, commandManager.cmdMail, "mail", "ml");
		Sponge.getCommandManager().register(this, commandManager.cmdMessage, "message", "msg");
		Sponge.getCommandManager().register(this, commandManager.cmdMute, "mute", "m");
		Sponge.getCommandManager().register(this, commandManager.cmdReply, "reply", "r");
		Sponge.getCommandManager().register(this, commandManager.cmdSay, "say", "s");

		SQLUtils.createTable();

		CommandHelp.init();
	}

	@Listener
	public void onPostInitializationEvent(GamePostInitializationEvent event) {
		if (Sponge.getPluginManager().isLoaded("simpletags")) {
			ChannelTag.init();
		}
	}

	public Logger getLog() {
		return log;
	}

	public Path getPath() {
		return path;
	}

	public Text processText(String msg, boolean color) {
		if(color) {
			return TextSerializers.FORMATTING_CODE.deserialize(msg);
		} else {
			return Text.of(msg);
		}
	}

	public Text getText(String msg, boolean color, boolean url) {
		if(url) {
			Text message = Text.EMPTY;
			
			while (msg.contains("&u")) {
				message = Text.join(message, processText(msg.substring(0, msg.indexOf("&u{")).replace("&u{", ""), color));

				String work = msg.substring(msg.indexOf("&u{"), msg.indexOf("}")).replaceFirst("&u\\{", "").replaceFirst("}", "");

				message = Text.join(message, getLink(work, color));

				msg = msg.substring(msg.indexOf("}"), msg.length()).replaceFirst("}", "");
			}

			return Text.of(message, processText(msg, color));
		} else {
			return processText(msg, color);
		}
	}

	private Text getLink(String link, boolean color) {
		Text.Builder builder = Text.builder();
		String[] work = link.split(";");

		if (work.length != 3) {
			return Text.of(TextColors.RED, "Invalid TextAction detected");
		}

		if (work[0].equalsIgnoreCase("url")) {
			if (!work[1].toLowerCase().contains("http://") && !work[1].toLowerCase().contains("https://")) {
				work[1] = "http://" + work[1];
			}

			URL url = null;
			try {
				url = new URL(work[1]);
				builder.onClick(TextActions.openUrl(url)).append(processText(work[2], color));
			} catch (MalformedURLException e) {
				return Text.of(TextColors.RED, "Invalid URL detected");
			}
		} else if (work[0].equalsIgnoreCase("cmd")) {
			builder.onClick(TextActions.runCommand(work[1])).append(processText(work[2], color));
		} else if (work[0].equalsIgnoreCase("suggest")) {
			builder.onClick(TextActions.suggestCommand(work[1])).append(processText(work[2], color));
		} else if (work[0].equalsIgnoreCase("hover")) {
			builder.onHover(TextActions.showText(processText(work[1], color))).append(processText(work[2], color));
		} else {
			return Text.of(TextColors.RED, "Invalid TextAction detected");
		}

		return builder.build();
	}

	public static PluginContainer getPlugin() {
		return plugin;
	}
	
	public static Main instance() {
		return instance;
	}
}
