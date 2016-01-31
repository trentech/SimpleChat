package com.gmail.trentech.simplechat.commands.channel;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simpletags.tags.ChannelTag;
import com.gmail.trentech.simplechat.utils.Help;

public class CMDList implements CommandExecutor {

	public CMDList(){
		Help help = new Help("clist", "list", " List all channels");
		help.setSyntax(" /channel list\n /c l");
		help.setExample(" /channel list");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

		pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Channels")).build());
			
		List<Text> list = new ArrayList<>();

		for(ChannelTag channel : ChannelTag.all()){
			list.add(Text.of(channel.getTag()));
		}
		
		if(list.isEmpty()){
			list.add(Text.of(TextColors.YELLOW, "No channels created"));
		}

		pages.contents(list);
		
		pages.sendTo(src);
		
		return CommandResult.empty();
	}
}
