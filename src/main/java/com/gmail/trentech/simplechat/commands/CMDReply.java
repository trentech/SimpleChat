package com.gmail.trentech.simplechat.commands;

import java.util.HashMap;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDReply implements CommandExecutor {

	private static HashMap<String, String> reply = new HashMap<String, String>();

	public CMDReply() {
		Help help = new Help("reply", "reply", " Reply to a player that sent you a private message");
		help.setSyntax(" /reply <message>\n /r <message>");
		help.setExample(" /reply I don't need diamond");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!getReply().containsKey(src.getName())) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "No message to reply to."));
			return CommandResult.empty();
		}
		String playerName = getReply().get(src.getName());

		CommandSource recipient;
		if (!playerName.equalsIgnoreCase("Server")) {
			if (!(Sponge.getServer().getPlayer(playerName).isPresent())) {
				src.sendMessage(Text.of(TextColors.DARK_RED, playerName, " is offline."));
				getReply().remove(src.getName());
				return CommandResult.empty();
			}
			recipient = Sponge.getServer().getPlayer(playerName).get();
		} else {
			recipient = Sponge.getServer().getConsole();
		}

		if (!args.hasAny("message")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/reply <message>"));
			return CommandResult.empty();
		}

		String messagePlain = args.<String> getOne("message").get();

		Text message = Text.of(TextColors.GOLD, "[", src.getName(), "] --> [", recipient.getName(), "]", TextColors.WHITE, " ", Main.processText(messagePlain));

		recipient.sendMessage(message);
		src.sendMessage(message);

		ConfigurationNode config = new ConfigManager().getConfig();

		if (config.getNode("options", "pm_snoop").getBoolean() && (!(recipient instanceof ConsoleSource || src instanceof ConsoleSource))) {
			Sponge.getServer().getConsole().sendMessage(message);
		}

		getReply().remove(src.getName());
		getReply().put(recipient.getName(), src.getName());

		return CommandResult.success();
	}

	public static HashMap<String, String> getReply() {
		return reply;
	}

}
