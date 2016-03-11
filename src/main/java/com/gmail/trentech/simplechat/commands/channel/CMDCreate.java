package com.gmail.trentech.simplechat.commands.channel;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simpletags.tags.ChannelTag;
import com.gmail.trentech.simplechat.utils.Help;

public class CMDCreate implements CommandExecutor {

	public CMDCreate(){
		Help help = new Help("ccreate", "create", " Create a new channel");
		help.setSyntax(" /channel create <channel>\n /c c <channel>");
		help.setExample(" /channel create ch1");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("channel")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/channel create <channel>"));
			return CommandResult.empty();
		}	
		String channel = args.<String>getOne("channel").get();
		
    	if(ChannelTag.get(channel).isPresent()){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Channel already exists"));
			return CommandResult.empty();
    	}
    	new ChannelTag(channel, "&a[" + channel + "]");
        
        src.sendMessage(Text.of(TextColors.DARK_GREEN, "channel created!"));
        
		return CommandResult.success();
	}
}
