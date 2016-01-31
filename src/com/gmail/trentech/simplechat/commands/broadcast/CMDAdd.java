package com.gmail.trentech.simplechat.commands.broadcast;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Help;

public class CMDAdd implements CommandExecutor {

	public CMDAdd(){
		Help help = new Help("add", "add", " Add message to broadcast list");
		help.setSyntax(" /broadcast add <message>\n /b a <message>");
		help.setExample(" /broadcast add Welcome to the server");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("message")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/chat broadcast add <message>"));
			return CommandResult.empty();
		}	
		String message = args.<String>getOne("message").get();
		
		ConfigManager configManager = new ConfigManager();
		
		List<String> broadcasts = Main.getBroadcasts();
		
		Main.getBroadcasts().add(message);
		
		configManager.getConfig().getNode("Broadcast", "Messages").setValue(broadcasts);
		
		configManager.save();
		
		return CommandResult.success();
	}

}
