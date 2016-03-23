package com.gmail.trentech.simplechat.commands.broadcast;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.utils.Help;

public class CMDList implements CommandExecutor {

	public CMDList(){
		Help help = new Help("blist", "list", " List all broadcast messages");
		help.setSyntax(" /broadcast list\n /b l");
		help.setExample(" /broadcast list");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();
		
		pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Messages")).build());
		
		List<Text> list = new ArrayList<>();
		
		int i = 0;
		for(String message : Main.getBroadcasts()){
			list.add(Text.of(TextColors.GREEN,"[",i,"] ", TextColors.RESET, TextSerializers.FORMATTING_CODE.deserialize(message)));
			i++;
		}
		
		pages.contents(list);
		
		pages.sendTo(src);
		
		return CommandResult.success();
	}

}
