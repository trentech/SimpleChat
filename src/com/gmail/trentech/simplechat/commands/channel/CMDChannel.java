package com.gmail.trentech.simplechat.commands.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationBuilder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.utils.Help;
import com.gmail.trentech.simplechat.utils.Mute;
import com.gmail.trentech.simpletags.tags.ChannelTag;
import com.gmail.trentech.simpletags.tags.DefaultTag;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.google.common.collect.Lists;

public class CMDChannel implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof ConsoleSource){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Must be a player"));
			return CommandResult.empty();
		}
		Player player = (Player) src;
		
		if(args.hasAny("channel") && args.hasAny("message")) {
			String channel = args.<String>getOne("channel").get();
			String message = args.<String>getOne("message").get();
			
			if(!src.hasPermission("simplechat.channel." + channel)){
				src.sendMessage(Text.of(TextColors.DARK_RED, "You do not have permission to use this channel"));
				return CommandResult.empty();
			}

			Optional<ChannelTag> optionalChannelTag = ChannelTag.get(channel);
			
			if(!optionalChannelTag.isPresent()){
				src.sendMessage(Text.of(TextColors.DARK_RED, channel, " does not exist"));
				return CommandResult.empty();
			}
			
			MutableMessageChannel messageChannel = MessageChannel.permission("simplechat.channel." + channel).asMutable();

	    	List<MessageReceiver> recipients = Lists.newArrayList(messageChannel.getMembers());
	    	
	    	for(MessageReceiver msgReceiver : recipients){
				if(msgReceiver instanceof Player){
					Player recipient = (Player) msgReceiver;
					
					if(Mute.get(recipient).get().getPlayers().contains(player.getUniqueId().toString())){
						messageChannel.removeMember(msgReceiver);
					}
				}
	    	}
	    	
			Builder playerTag = Text.builder().onHover(TextActions.showText(Text.of(player.getName())));

			Optional<PlayerTag> optionalPlayerTag = PlayerTag.get(player);
			
			if(!optionalPlayerTag.isPresent()){
				playerTag.append(DefaultTag.get(player).get().getTag());
			}else{
				playerTag.append(PlayerTag.get(player).get().getTag());
			}

			messageChannel.send(Text.of(optionalChannelTag.get().getTag(), playerTag.build(), TextColors.WHITE, ": ", TextSerializers.FORMATTING_CODE.deserialize(message)));
			
			return CommandResult.success();
		}
		
		PaginationBuilder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();
		
		pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Command List")).build());
		
		List<Text> list = new ArrayList<>();

		list.add(Text.of(TextColors.YELLOW, " /channel <channel> <message>\n"));

		if(src.hasPermission("simplechat.cmd.channel.create")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("ccreate"))).append(Text.of(" /channel create")).build());
		}
		if(src.hasPermission("simplechat.cmd.channel.remove")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("cremove"))).append(Text.of(" /channel remove")).build());
		}
		if(src.hasPermission("simplechat.cmd.channel.list")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("clist"))).append(Text.of(" /channel list")).build());
		}
		pages.contents(list);
		
		pages.sendTo(src);

		return CommandResult.success();
	}
}
