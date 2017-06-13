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

import com.gmail.trentech.pjc.help.Help;

public class CMDChannel implements CommandExecutor {

	public static HashMap<UUID, String> hash = new HashMap<>();

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("channel").get();
		
		if (args.hasAny("help")) {		
			help.execute(src);
			return CommandResult.empty();
		}
		
		if (src instanceof ConsoleSource) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player"), false);
		}
		Player player = (Player) src;

		if (!args.hasAny("channel")) {
			throw new CommandException(Text.of(TextColors.GREEN, "Current Channel: ", TextColors.WHITE, hash.get(player.getUniqueId())), false);
		}

		String channel = args.<String> getOne("channel").get();

		if (!src.hasPermission("simplechat.channel." + channel) && !channel.equalsIgnoreCase("global")) {
			throw new CommandException(Text.of(TextColors.RED, "You do not have permission to use this channel"), false);
		}

		hash.put(player.getUniqueId(), channel);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set channel to ", channel));

		return CommandResult.success();
	}
}
