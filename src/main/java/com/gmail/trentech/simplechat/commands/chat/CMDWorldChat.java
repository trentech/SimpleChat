package com.gmail.trentech.simplechat.commands.chat;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.utils.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDWorldChat implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		boolean bool = args.<Boolean> getOne("true|false").get();

		ConfigManager configManager = ConfigManager.get();
		ConfigurationNode config = configManager.getConfig();

		config.getNode("options", "world_chat").setValue(bool);

		configManager.save();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "World specific chat to to ", bool));

		return CommandResult.success();
	}

}
