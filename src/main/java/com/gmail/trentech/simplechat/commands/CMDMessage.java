package com.gmail.trentech.simplechat.commands;

import org.spongepowered.api.Sponge;
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
		help.setPermission("simplechat.cmd.message");
		help.setSyntax(" /message <player> <message>\n /msg <player> <message>");
		help.setExample(" /message Notch I found diamond");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Player player = args.<Player> getOne("player").get();

		if ((src instanceof Player) && Mute.get(player).get().getPlayers().contains(((Player) src).getUniqueId().toString())) {
			throw new CommandException(Text.of(TextColors.RED, player.getName(), " has muted you. You can only reply to a message from this player"), false);
		}

		String messagePlain = args.<String> getOne("message").get();

		Text message = Text.of(TextColors.GOLD, "[", src.getName(), "] --> [", player.getName(), "]", TextColors.WHITE, " ", Main.instance().processText(messagePlain));

		player.sendMessage(message);
		src.sendMessage(message);

		ConfigurationNode config = ConfigManager.get().getConfig();

		if (config.getNode("options", "pm_snoop").getBoolean() && (!(src instanceof ConsoleSource))) {
			Sponge.getServer().getConsole().sendMessage(message);
		}

		CMDReply.getReply().put(player.getName(), src.getName());

		return CommandResult.success();
	}

}
