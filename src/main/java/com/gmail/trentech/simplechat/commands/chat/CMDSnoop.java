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

public class CMDSnoop implements CommandExecutor {

	public CMDSnoop() {
		Help help = new Help("snoop", "snoop", " Toggle on and off private message snooping");
		help.setSyntax(" /chat snoop <boolean>\n /c s <boolean>");
		help.setExample(" /chat snoop true");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		boolean bool = args.<Boolean> getOne("boolean").get();

		ConfigManager configManager = ConfigManager.get();
		ConfigurationNode config = configManager.getConfig();

		config.getNode("options", "pm_snoop").setValue(bool);

		configManager.save();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Private snooping set to ", bool));

		return CommandResult.success();
	}

}
