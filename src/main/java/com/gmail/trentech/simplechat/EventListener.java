package com.gmail.trentech.simplechat;

import java.util.LinkedList;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageEvent.MessageFormatter;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Message;
import com.gmail.trentech.simplechat.utils.Mute;
import com.gmail.trentech.simplechat.utils.SQLUtils;
import com.google.common.collect.Lists;

import ninja.leaping.configurate.ConfigurationNode;

public class EventListener {

	@Listener
	public void onClientConnectionEventJoin(ClientConnectionEvent.Join event) {
	    Player player = event.getTargetEntity();
	    new Mute(player);
	    
    	SQLUtils.createPlayerTable(player);

        LinkedList<Message> messages = Message.all(player);

    	boolean unread = false;
    	
    	for(Message message : messages){
    		if(message.isRead()){
    			continue;
    		}
    		
    		unread = true;

			break;
    	}
    	
    	if(unread){
			Builder builder = Text.builder().color(TextColors.GREEN).append(Text.of("You have unread messages! /mail"));
			builder.onClick(TextActions.runCommand("/mail"));
            builder.onHover(TextActions.showText(Text.of("Click to open mailbox")));

            player.sendMessage(builder.build());
    	}
	}
	
	@Listener
	public void MessageChannelEventChat(MessageChannelEvent.Chat event, @First Player player){
		MutableMessageChannel channel = MessageChannel.TO_ALL.asMutable();

		ConfigurationNode config = new ConfigManager().getConfig();	

    	if(config.getNode("Options", "World-Chat").getBoolean()){
    		List<MessageReceiver> recipients = Lists.newArrayList(channel.getMembers());

    		for(MessageReceiver src : recipients){
    			if(src instanceof Player){
    				Player recipient = (Player) src;
    				
    				if(!recipient.getWorld().equals(player.getWorld())){
    					channel.removeMember(src);
    				}
    			}
    		}  		
    	}
    	
    	if(config.getNode("Options", "Ranged-Chat", "Enable").getBoolean()){ 		
    		List<MessageReceiver> recipients = Lists.newArrayList(channel.getMembers());
    		
    		for(MessageReceiver src : recipients){
    			if(src instanceof Player){
    				Player recipient = (Player) src;
    				
    				int range = config.getNode("Options", "Ranged-Chat", "Range").getInt();
			
    				double playerX = player.getLocation().getX();
    				double playerZ = player.getLocation().getZ();

    				double recipientX = recipient.getLocation().getX();
    				double recipientZ = recipient.getLocation().getZ();
    				
    				double distance = Math.sqrt((recipientX-playerX)*(recipientX-playerX) + (recipientZ-playerZ)*(recipientZ-playerZ));

    				if(distance > range){
    					channel.removeMember(src);
    				}
    			}
    		}
    	}
   	
    	List<MessageReceiver> recipients = Lists.newArrayList(channel.getMembers());
    	
    	for(MessageReceiver src : recipients){
			if(src instanceof Player){
				Player recipient = (Player) src;
				
				if(Mute.get(recipient).get().getPlayers().contains(player.getUniqueId().toString())){
					channel.removeMember(src);
				}
			}
    	}

    	event.setChannel(channel);
    	
		String messageOrig = TextSerializers.FORMATTING_CODE.serialize(event.getFormatter().getBody().toText());

		Text message = Main.processText(messageOrig);
		
		MessageFormatter formatter = event.getFormatter();
		
		formatter.setBody(TextTemplate.of(message));
	}
}
