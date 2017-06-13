package com.gmail.trentech.simplechat.commands.chat;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjc.help.Help;

public class CMDChat implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help.executeList(src, Help.get("chat").get().getChildren());
		src.sendMessage(Text.of(TextColors.YELLOW, "chat <command> --help"));
		return CommandResult.success();
	}

}
