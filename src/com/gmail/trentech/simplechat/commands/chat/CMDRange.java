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

	public CMDRange(){
		Help help = new Help("range", "range", " Toggle on and off chat range");
		help.setSyntax(" /chat range <boolean> [value]\n /c r <boolean> [value]");
		help.setExample(" /chat range false\n /chat range true 64");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("boolean")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/chat range <boolean> [value]"));
			return CommandResult.empty();
		}
		String bool = args.<String>getOne("boolean").get();
		
		if(!bool.equalsIgnoreCase("true") || !bool.equalsIgnoreCase("false")){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Boolean must be true or false"));
			return CommandResult.empty();
		}
		
		ConfigManager configManager = new ConfigManager();
		ConfigurationNode config = configManager.getConfig();
		
		if(args.hasAny("value")) {
			String value = args.<String>getOne("value").get();
			try{
				config.getNode("Options", "Ranged-Chat", "Range").setValue(Integer.parseInt(value));
			}catch(Exception e){
				src.sendMessage(Text.of(TextColors.DARK_RED, value, " is not a valid value"));
				return CommandResult.empty();
			}			
		}
		
		config.getNode("Options", "Ranged-Chat", "Enable").setValue(Boolean.parseBoolean(bool));
		
		configManager.save();
		
		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set ranged chat to ", bool));

		return CommandResult.success();
	}

}
