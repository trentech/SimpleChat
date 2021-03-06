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

import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.simplechat.Main;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDReply implements CommandExecutor {

	private static HashMap<String, String> reply = new HashMap<String, String>();

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("reply").get();
		
		if (args.hasAny("help")) {		
			help.execute(src);
			return CommandResult.empty();
		}
		
		if (!getReply().containsKey(src.getName())) {
			throw new CommandException(Text.of(TextColors.RED, "No message to reply to."), false);
		}
		String playerName = getReply().get(src.getName());

		CommandSource recipient;
		if (!playerName.equalsIgnoreCase("Server")) {
			if (!(Sponge.getServer().getPlayer(playerName).isPresent())) {
				getReply().remove(src.getName());
				throw new CommandException(Text.of(TextColors.RED, playerName, " is offline."), false);
			}
			recipient = Sponge.getServer().getPlayer(playerName).get();
		} else {
			recipient = Sponge.getServer().getConsole();
		}

		String messagePlain = args.<String> getOne("message").get();

		Text message = Text.of(TextColors.GOLD, "[", src.getName(), "] --> [", recipient.getName(), "]", TextColors.WHITE, " ", Main.instance().getText(messagePlain, src.hasPermission("simplechat.text.color"), src.hasPermission("simplechat.text.url")));

		recipient.sendMessage(message);
		src.sendMessage(message);

		ConfigurationNode config = ConfigManager.get(Main.getPlugin()).getConfig();

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
