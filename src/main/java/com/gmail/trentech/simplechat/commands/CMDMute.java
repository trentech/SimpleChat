package com.gmail.trentech.simplechat.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.data.Mute;
import com.gmail.trentech.simplechat.utils.Help;

public class CMDMute implements CommandExecutor {

	public CMDMute() {
		Help help = new Help("mute", "mute", " Mutes player from sending you any kind of message, public or private");
		help.setSyntax(" /mute <player>\n /m <player>");
		help.setExample(" /mute Notch");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("player")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/mute <player>"));
			return CommandResult.empty();
		}
		Player player = (Player) src;

		Player target = args.<Player> getOne("player").get();

		if (target.hasPermission("simplechat.unmute")) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "You cannot mute this player"));
			return CommandResult.empty();
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
