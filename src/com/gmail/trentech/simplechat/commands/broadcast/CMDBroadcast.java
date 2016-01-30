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
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.utils.Help;
import com.gmail.trentech.simpletags.tags.BroadcastTag;

public class CMDBroadcast implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(args.hasAny("message")) {
			String message = args.<String>getOne("message").get();

		    Main.getGame().getServer().getBroadcastChannel().send(Text.of(BroadcastTag.get().get().getTag(), TextColors.WHITE, ": ",
		    			TextSerializers.FORMATTING_CODE.deserialize(message)));

			return CommandResult.success();
		}
		
		PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();
		
		pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Command List")).build());
		
		List<Text> list = new ArrayList<>();

		list.add(Text.of(TextColors.YELLOW, " /broadcast <message>\n"));

		if(src.hasPermission("simplechat.cmd.broadcast.add")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("add"))).append(Text.of(" /broadcast add")).build());
		}
		if(src.hasPermission("simplechat.cmd.broadcast.remove")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("bremove"))).append(Text.of(" /broadcast remove")).build());
		}
		if(src.hasPermission("simplechat.cmd.broadcast.list")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("blist"))).append(Text.of(" /broadcast list")).build());
		}
		pages.contents(list);
		
		pages.sendTo(src);

		return CommandResult.success();
	}

}
