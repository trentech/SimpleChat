package com.gmail.trentech.simplechat.commands.chat;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.simplechat.Main;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDSnoop implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("chat snoop").get();
		
		if (args.hasAny("help")) {		
			help.execute(src);
			return CommandResult.empty();
		}
		
		boolean bool = args.<Boolean> getOne("true|false").get();

		ConfigManager configManager = ConfigManager.get(Main.getPlugin());
		ConfigurationNode config = configManager.getConfig();

		config.getNode("options", "pm_snoop").setValue(bool);

		configManager.save();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Private snooping set to ", bool));

		return CommandResult.success();
	}

}
