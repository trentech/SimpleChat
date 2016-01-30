package com.gmail.trentech.simplechat.commands.broadcast;

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
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.Main;

public class CMDList implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();
		
		pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Messages")).build());
		
		List<Text> list = new ArrayList<>();
		
		int i = 0;
		for(String message : Main.getBroadcasts()){
			list.add(Text.of(TextColors.GREEN,"[",i,"]", TextSerializers.FORMATTING_CODE.deserialize(message)));
			i++;
		}
		
		pages.sendTo(src);
		
		return CommandResult.success();
	}

}
