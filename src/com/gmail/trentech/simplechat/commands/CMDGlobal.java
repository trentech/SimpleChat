package com.gmail.trentech.simplechat.commands;

import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.utils.Help;
import com.gmail.trentech.simplechat.utils.Mute;
import com.gmail.trentech.simpletags.tags.DefaultTag;
import com.gmail.trentech.simpletags.tags.GroupTag;
import com.gmail.trentech.simpletags.tags.PlayerTag;
import com.gmail.trentech.simpletags.tags.WorldTag;

import com.google.common.collect.Lists;

public class CMDGlobal implements CommandExecutor {

	public CMDGlobal(){
		Help help = new Help("global", "global", " Send chat message to all players");
		help.setSyntax(" /global <message>\n /g <message>");
		help.setExample(" /global Hello people");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof ConsoleSource){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Player only command. use /say"));
			return CommandResult.empty();
		}
		Player player = (Player) src;
		
		if(!args.hasAny("message")){
			src.sendMessage(Text.of(TextColors.YELLOW, "/global <message>"));
			return CommandResult.empty();
		}
		
		Text message = TextSerializers.FORMATTING_CODE.deserialize(": " + args.<String>getOne("message").get());
		
		MutableMessageChannel messageChannel = MessageChannel.TO_ALL.asMutable();

    	List<MessageReceiver> recipients = Lists.newArrayList(messageChannel.getMembers());
    	
    	for(MessageReceiver msgReceiver : recipients){
			if(msgReceiver instanceof Player){
				Player recipient = (Player) msgReceiver;
				
				if(Mute.get(recipient).get().getPlayers().contains(player.getUniqueId().toString())){
					messageChannel.removeMember(msgReceiver);
				}
			}
    	}

		Text worldTag = Text.EMPTY;
		Builder groupTagBuilder = Text.builder();
		
		Builder playerTag = Text.builder().onHover(TextActions.showText(Text.of(player.getName())));

		Optional<PlayerTag> optionalPlayerTag = PlayerTag.get(player);
		
		if(!optionalPlayerTag.isPresent()){
			playerTag.append(DefaultTag.get(player).get().getTag());
		}else{
			playerTag.append(PlayerTag.get(player).get().getTag());
		}
				
		worldTag = WorldTag.get(player.getWorld()).get().getTag();

		for(GroupTag groupTag : GroupTag.all()){
			String group = groupTag.getName();
			
			if(!player.hasPermission("simpletags.group." + group)){
				continue;
			}
			
			groupTagBuilder.append(groupTag.getTag());
		}
		
		messageChannel.send(Text.of(worldTag, groupTagBuilder.build(), playerTag.build(), message));

		return CommandResult.success();
	}

}
