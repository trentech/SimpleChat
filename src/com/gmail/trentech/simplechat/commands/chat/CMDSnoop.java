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

public class CMDSnoop implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("boolean")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/chat snoop <boolean>"));
			return CommandResult.empty();
		}
		String bool = args.<String>getOne("boolean").get();
		
		ConfigManager configManager = new ConfigManager();
		ConfigurationNode config = configManager.getConfig();
		
		if(bool.equalsIgnoreCase("true")){
			config.getNode("Options", "PM-Snoop").setValue(true);

			configManager.save();
			
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Private snooping enabled"));
			
			return CommandResult.success();
		}else if(bool.equalsIgnoreCase("false")){
			config.getNode("Options", "PM-Snoop").setValue(false);

			configManager.save();
			
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Private snooping disabled"));
			
			return CommandResult.success();
		}
		
		src.sendMessage(Text.of(TextColors.DARK_RED, "true or false"));
		
		return CommandResult.empty();
	}

}
