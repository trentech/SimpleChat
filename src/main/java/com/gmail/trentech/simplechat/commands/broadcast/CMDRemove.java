package com.gmail.trentech.simplechat.commands.broadcast;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.utils.Broadcast;
import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDRemove implements CommandExecutor {

	public CMDRemove(){
		Help help = new Help("bremove", "remove", " Remove message from broadcast list");
		help.setSyntax(" /broadcast remove <index>\n /b r <message>");
		help.setExample(" /broadcast remove 4");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("index")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/chat broadcast add <message>"));
			return CommandResult.empty();
		}
		int index;
		
		try{
			index = Integer.parseInt(args.<String>getOne("index").get());
		}catch(Exception e){
			src.sendMessage(Text.of(TextColors.YELLOW, "/chat broadcast remove <index>"));
			return CommandResult.empty();
		}
		
		ConfigManager configManager = new ConfigManager();
		
		List<Text> broadcasts = Broadcast.getBroadcasts();		
		
		broadcasts.remove(index);
		
		ConfigurationNode node = configManager.getConfig().getNode("Broadcast", "Messages");
		
		List<String> list = node.getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		list.remove(index);
		
		node.setValue(list);
		
		configManager.save();
		
		src.sendMessage(Text.of(TextColors.GREEN, "Message saved"));
		
		return CommandResult.success();
	}

}
