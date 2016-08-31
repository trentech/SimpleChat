package com.gmail.trentech.simplechat.commands.chat;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDRange implements CommandExecutor {

	public CMDRange() {
		Help help = new Help("range", "range", " Toggle on and off chat range");
		help.setSyntax(" /chat range <boolean> [value]\n /c r <boolean> [value]");
		help.setExample(" /chat range false\n /chat range true 64");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		boolean bool = args.<Boolean> getOne("boolean").get();

		ConfigManager configManager = ConfigManager.get();
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
