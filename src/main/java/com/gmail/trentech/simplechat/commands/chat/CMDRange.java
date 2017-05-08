package com.gmail.trentech.simplechat.commands.chat;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.simplechat.Main;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDRange implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		boolean bool = args.<Boolean> getOne("true|false").get();

		ConfigManager configManager = ConfigManager.get(Main.getPlugin());
		ConfigurationNode config = configManager.getConfig();

		if (args.hasAny("value")) {
			config.getNode("options", "ranged_chat", "range").setValue(args.<Integer> getOne("value").get());
		}

		config.getNode("options", "ranged_chat", "enable").setValue(bool);

		configManager.save();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set ranged chat to ", bool));

		return CommandResult.success();
	}

}
