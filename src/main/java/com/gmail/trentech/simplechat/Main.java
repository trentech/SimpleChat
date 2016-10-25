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

import com.gmail.trentech.helpme.help.Argument;
import com.gmail.trentech.helpme.help.Help;
import com.gmail.trentech.helpme.help.Usage;
import com.gmail.trentech.simplechat.commands.CommandManager;
import com.gmail.trentech.simplechat.commands.channel.CMDTagChannel;
import com.gmail.trentech.simplechat.data.ChannelTag;
import com.gmail.trentech.simplechat.listeners.EventListener;
import com.gmail.trentech.simplechat.listeners.TagListener;
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

		if(Sponge.getPluginManager().isLoaded("helpme")) {
			Usage usageSay = new Usage(Argument.of("<message>", "The message to be broadcasted"));
			
			Help say = new Help("say", "say", "Send a message to the server from the console")
					.setPermission("simplechat.cmd.say")
					.setUsage(usageSay)
					.addExample("/say Drop party coming soon");
			
			Help.register(say);
			
			Usage usageReply = new Usage(Argument.of("<message>", "The message to be sent"));
			
			Help reply = new Help("reply", "reply", "Reply to a player that sent you a private message")
					.setPermission("simplechat.cmd.reply")
					.setUsage(usageReply)
					.addExample("/reply I don't need diamond");
			
			Help.register(reply);
			
			Usage usagePlayer = new Usage(Argument.of("<player>", "Specifies the targgeted player"));
			
			Help mute = new Help("mute", "mute", "Mutes player from sending you any kind of message, public or private")
					.setPermission("simplechat.cmd.mute")
					.setUsage(usagePlayer)
					.addExample("/mute Notch");
			
			Help.register(mute);
			
			Usage usageMessage = new Usage(Argument.of("<player>", "Specifies the targgeted player"))
					.addArgument(Argument.of("<message>", "The message to be sent"));
			
			Help message = new Help("message", "message", "Send a private message to a player")
					.setPermission("simplechat.cmd.message")
					.setUsage(usageMessage)
					.addExample("/message Notch I found diamond");
			
			Help.register(message);

			Help mail = new Help("mail", "mail", "Sender messages to offline players")
					.setPermission("simplechat.cmd.mail")
					.setUsage(usageMessage)
					.addExample("/mail")
					.addExample("/mail Notch I destroyed your house...sorry");
			
			Help.register(mail);
			
			Usage usageBoolean = new Usage(Argument.of("<true|false>"));
			
			Help chatWorldChat = new Help("chat worldchat", "worldchat", "Toggle on and off world specific chat")
					.setPermission("simplechat.cmd.chat.worldchat")
					.setUsage(usageBoolean)
					.addExample("/chat worldchat true");
			
			Help chatSnoop = new Help("chat snoop", "snoop", "Toggle on and off private message snooping")
					.setPermission("simplechat.cmd.chat.snoop")
					.setUsage(usageBoolean)
					.addExample("/chat snoop true");
			
			Usage usageRange = new Usage(Argument.of("<true|false>"))
					.addArgument(Argument.of("[value]", "Specifies the range"));
			
			Help chatRange = new Help("chat range", "range", "Toggle on and off chat range")
					.setPermission("simplechat.cmd.chat.range")
					.setUsage(usageRange)
					.addExample("/chat range false")
					.addExample("/chat range true 64");
			
			Help chat = new Help("chat", "chat", "Chat settings for SimpleChat")
					.setPermission("simplechat.cmd.chat")
					.addChild(chatRange)
					.addChild(chatSnoop)
					.addChild(chatWorldChat);
			
			Help.register(chat);
			
			Usage usageChannel = new Usage(Argument.of("[channel]", "Specifies the targgeted channel"));
			
			Help channel = new Help("channel", "channel", "Set channel player will send and receives chat messages from. Set to global to reset")
					.setPermission("simplechat.cmd.channel")
					.setUsage(usageChannel)
					.addExample("/channel whatever")
					.addExample("/channel global");
			
			Help.register(channel);
			
			Usage usageTag = new Usage(Argument.of("<channel>", "Specifies the name of a channel"))
					.addArgument(Argument.of("<tag>", "Set custom tag. Accepts color codes"));
			
			Help tagChannel = new Help("tag channel", "channel", "View and edit channel tags")
					.setPermission("simpletags.cmd.tag.channel")
					.setUsage(usageTag)
					.addExample(" /tag channel private")
					.addExample("/tag channel private &e[private]")
					.addExample("/tag channel private reset");
			
			Help.register(Help.get("tag").get().addChild(tagChannel));
		}

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
	
	public Text processText(String msg) {
		Text message = Text.EMPTY;

		while (msg.contains("&u")) {
			message = Text.join(message, TextSerializers.FORMATTING_CODE.deserialize(msg.substring(0, msg.indexOf("&u{")).replace("&u{", "")));

			String work = msg.substring(msg.indexOf("&u{"), msg.indexOf("}")).replaceFirst("&u\\{", "").replaceFirst("}", "");

			message = Text.join(message, getLink(work));

			msg = msg.substring(msg.indexOf("}"), msg.length()).replaceFirst("}", "");
		}

		return Text.of(message, TextSerializers.FORMATTING_CODE.deserialize(msg));
	}

	private Text getLink(String link) {
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
				builder.onClick(TextActions.openUrl(url)).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
			} catch (MalformedURLException e) {
				return Text.of(TextColors.RED, "Invalid URL detected");
			}
		} else if (work[0].equalsIgnoreCase("cmd")) {
			builder.onClick(TextActions.runCommand(work[1])).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
		} else if (work[0].equalsIgnoreCase("suggest")) {
			builder.onClick(TextActions.suggestCommand(work[1])).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
		} else if (work[0].equalsIgnoreCase("hover")) {
			builder.onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize(work[1]))).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
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
