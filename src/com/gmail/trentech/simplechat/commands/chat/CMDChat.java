package com.gmail.trentech.simplechat.commands.chat;

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
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.utils.Help;

public class CMDChat implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

		pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.AQUA, "Commands")).build());
		
		List<Text> list = new ArrayList<>();
		
		if(src.hasPermission("simplechat.cmd.chat.broadcast")){
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("broadcast"))).append(Text.of(" /chat broadcast")).build());
		}
		if(src.hasPermission("simplechat.cmd.chat.range")){
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("range"))).append(Text.of(" /chat range")).build());
		}
		if(src.hasPermission("simplechat.cmd.chat.snoop")){
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("snoop"))).append(Text.of(" /chat snoop")).build());
		}
		if(src.hasPermission("simplechat.cmd.chat.worldchat")){
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("worldchat"))).append(Text.of(" /chat worldchat")).build());
		}
		
		pages.contents(list);	
		pages.sendTo(src);

		return CommandResult.success();		
	}

}
