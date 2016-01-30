package com.gmail.trentech.simplechat;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;

import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simplechat.utils.Mute;

import ninja.leaping.configurate.ConfigurationNode;

public class EventListener {

	@Listener
	public void onClientConnectionEventJoin(ClientConnectionEvent.Join event) {
	    Player player = event.getTargetEntity();
	    new Mute(player);
	}
	
	@Listener
	public void MessageChannelEventChat(MessageChannelEvent.Chat event, @First Player player){
		MessageChannel channel = MessageChannel.TO_ALL;

		ConfigurationNode config = new ConfigManager().getConfig();	
		
		Set<CommandSource> recipients = new HashSet<CommandSource>();

    	if(config.getNode("Options", "World-Chat").getBoolean()){
    		Collection<Entity> entities = player.getWorld().getEntities();
    		for(Entity entity : entities){
    			if(entity instanceof Player){
    				Player recipient = (Player) entity;
    				recipients.add(recipient);
    			}
    		}  		
    	}
    	
    	if(config.getNode("Options", "Ranged-Chat", "Enable").getBoolean()){
    		Iterable<MessageReceiver> oldRecip = channel.getMembers();
    		for(MessageReceiver src : oldRecip){
    			if(src instanceof Player){
    				int range = config.getNode("Options", "Ranged-Chat", "Range").getInt();
			
    				double playerX = player.getLocation().getX();
    				double playerZ = player.getLocation().getZ();

    				double recipientX = player.getLocation().getX();
    				double recipientZ = player.getLocation().getZ();
    				
    				double distance = Math.sqrt((recipientX-playerX)*(recipientX-playerX) + (recipientZ-playerZ)*(recipientZ-playerZ));

    				if(distance > range){
    					recipients.remove(src);
    				}
    			}
    		}
    	}
   	
    	Iterable<MessageReceiver> oldRecip = channel.getMembers();
    	
    	for(MessageReceiver src : oldRecip){
			if(src instanceof Player){
				Player recipient = (Player) src;
				
				if(Mute.get(recipient).get().getPlayers().contains(player.getUniqueId().toString())){
					recipients.remove(src);
				}
			}
    	}
    	
    	if(!recipients.isEmpty()){
    		channel = MessageChannel.fixed(recipients);
    	}

    	event.setChannel(channel);
	}
}
