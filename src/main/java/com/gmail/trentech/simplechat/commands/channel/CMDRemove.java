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

public class CMDRemove implements CommandExecutor {

	public CMDRemove(){
		Help help = new Help("cremove", "remove", " Remove channel from server");
		help.setSyntax(" /channel remove <channel>\n /c r <channel>");
		help.setExample(" /channel remove ch1");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("channel")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/channel remove <channel>"));
			return CommandResult.empty();
		}	
		String channel = args.<String>getOne("channel").get();
		
    	if(!ChannelTag.get(channel).isPresent()){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Channel does not exist"));
			return CommandResult.empty();
    	}
    	ChannelTag.get(channel).get().delete();
        
        src.sendMessage(Text.of(TextColors.DARK_GREEN, "channel created!"));
        
		return CommandResult.success();
	}
}
