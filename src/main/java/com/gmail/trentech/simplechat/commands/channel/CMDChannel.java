package com.gmail.trentech.simplechat.commands.channel;

import java.util.HashMap;
import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.utils.Help;

public class CMDChannel implements CommandExecutor {

	public static HashMap<UUID, String> hash = new HashMap<>();

	public CMDChannel() {
		Help help = new Help("channel", "channel", " Set channel player will send and receives chat messages from. Set to global to reset");
		help.setSyntax(" /channel [channel]\n /m [channel]");
		help.setExample(" /channel whatever\n /channel global");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof ConsoleSource) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "Must be a player"));
			return CommandResult.empty();
		}
		Player player = (Player) src;

		if (!args.hasAny("channel")) {
			src.sendMessage(Text.of(TextColors.GREEN, "Current Channel: ", TextColors.WHITE, hash.get(player.getUniqueId())));

			return CommandResult.empty();
		}

		String channel = args.<String> getOne("channel").get();

		if (!src.hasPermission("simplechat.channel." + channel) && !channel.equalsIgnoreCase("global")) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to use this channel"));
			return CommandResult.empty();
		}

		hash.put(player.getUniqueId(), channel);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set channel to ", channel));

		return CommandResult.success();
	}
}
