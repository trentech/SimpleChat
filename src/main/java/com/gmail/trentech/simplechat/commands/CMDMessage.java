package com.gmail.trentech.simplechat.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.data.Mute;
import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDMessage implements CommandExecutor {

	public CMDMessage() {
		Help help = new Help("message", "message", " Send a private message to a player");
		help.setSyntax(" /message <player> <message>\n /msg <player> <message>");
		help.setExample(" /message Notch I found diamond");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("playerName")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/message <player> <message>"));
			return CommandResult.empty();
		}
		String playerName = args.<String> getOne("playerName").get();

		Optional<Player> optionalPlayer = Main.getGame().getServer().getPlayer(playerName);

		if (!optionalPlayer.isPresent()) {
			CMDReply.getReply().remove(src.getName());

			if (!playerName.equalsIgnoreCase("Server")) {
				src.sendMessage(Text.of(TextColors.DARK_RED, playerName, " is offline."));
				return CommandResult.empty();
			}

			src.sendMessage(Text.of(TextColors.DARK_RED, "You cannot message the console directly, but can reply to message from."));
			return CommandResult.empty();
		}
		Player player = optionalPlayer.get();

		if ((src instanceof Player) && Mute.get(player).get().getPlayers().contains(((Player) src).getUniqueId().toString())) {
			src.sendMessage(Text.of(TextColors.DARK_RED, playerName, " has muted you. You can only reply to a message from this player"));
			return CommandResult.empty();
		}

		if (!args.hasAny("message")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/msg <player> <message>"));
			return CommandResult.empty();
		}
		String messagePlain = args.<String> getOne("message").get();

		Text message = Text.of(TextColors.GOLD, "[", src.getName(), "] --> [", player.getName(), "]", TextColors.WHITE, " ", Main.processText(messagePlain));

		player.sendMessage(message);
		src.sendMessage(message);

		ConfigurationNode config = new ConfigManager().getConfig();

		if (config.getNode("options", "pm_snoop").getBoolean() && (!(src instanceof ConsoleSource))) {
			Main.getGame().getServer().getConsole().sendMessage(message);
		}

		CMDReply.getReply().put(player.getName(), src.getName());

		return CommandResult.success();
	}

}
