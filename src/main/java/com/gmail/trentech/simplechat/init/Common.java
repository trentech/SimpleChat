package com.gmail.trentech.simplechat.init;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.spongepowered.api.Sponge;

import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.core.SQLManager;
import com.gmail.trentech.pjc.help.Argument;
import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.pjc.help.Usage;
import com.gmail.trentech.simplechat.Main;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Common {

	public static void init() {
		initConfig(Main.getPlugin().getId());
		initHelp();
		initData();
	}
	
	public static void initData() {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + sqlManager.getPrefix("SIMPLECHAT.MUTED") + " (Name TEXT, Players TEXT)");
			statement.executeUpdate();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initHelp() {
		Usage usageSay = new Usage(Argument.of("<message ...>", "The message to be broadcasted"));
		
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
		
		Usage usagePlayer = new Usage(Argument.of("<player>", "Specifies the targetted player"));
		
		Help mute = new Help("mute", "mute", "Mutes player from sending you any kind of message, public or private")
				.setPermission("simplechat.cmd.mute")
				.setUsage(usagePlayer)
				.addExample("/mute Notch");
		
		Help.register(mute);
		
		Usage usageMessage = new Usage(Argument.of("<player>", "Specifies the targetted player"))
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
		
		Usage usageChannel = new Usage(Argument.of("[channel]", "Specifies the targetted channel"));
		
		Help channel = new Help("channel", "channel", "Set channel player will send and receives chat messages from. Set to global to reset")
				.setPermission("simplechat.cmd.channel")
				.setUsage(usageChannel)
				.addExample("/channel whatever")
				.addExample("/channel global");
		
		Help.register(channel);
		
		if(Sponge.getPluginManager().isLoaded("simpletags")) {
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
	
	public static void initConfig(String configName) {
		ConfigManager configManager = ConfigManager.init(Main.getPlugin(), configName);
		CommentedConfigurationNode config = configManager.getConfig();

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
		if (config.getNode("settings", "sql", "database").isVirtual()) {
			config.getNode("settings", "sql", "database").setValue(Main.getPlugin().getId());
		}
		configManager.save();
	}
}
