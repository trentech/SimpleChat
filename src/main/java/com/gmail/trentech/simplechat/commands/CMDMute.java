package com.gmail.trentech.simplechat.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.simplechat.data.Mute;

public class CMDMute implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("mute").get();
		
		if (args.hasAny("help")) {		
			help.execute(src);
			return CommandResult.empty();
		}
		
		if (!(src instanceof Player)) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player"), false);
		}
		Player player = (Player) src;

		Player target = args.<Player> getOne("player").get();

		if (target.hasPermission("simplechat.unmute")) {
			throw new CommandException(Text.of(TextColors.RED, "You cannot mute this player"), false);
		}

		Mute mute = Mute.get(player).get();

		if (mute.getPlayers().contains(target.getUniqueId().toString())) {
			mute.removePlayer(target);
			player.sendMessage(Text.of(TextColors.DARK_GREEN, target.getName(), " can now speak"));
		} else {
			mute.addPlayer(target);
			player.sendMessage(Text.of(TextColors.DARK_GREEN, target.getName(), " has been muted"));
		}

		return CommandResult.success();
	}

}
