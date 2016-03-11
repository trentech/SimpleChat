package com.gmail.trentech.simplechat.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simpletags.tags.BroadcastTag;

import ninja.leaping.configurate.ConfigurationNode;

public class Broadcast {

    public Task task;

    public void start(ConfigurationNode config) {
    	List<Integer> played = new ArrayList<>();
    	
        int minutes = config.getNode("Broadcast", "Minutes").getInt();
        
        task = Main.getGame().getScheduler().createTaskBuilder().interval(minutes, TimeUnit.MINUTES).name("broadcast").execute(new Runnable() {
        	
			@Override
            public void run() {
				ThreadLocalRandom random = ThreadLocalRandom.current();
				int size = Main.getBroadcasts().size();
				
            	int number = random.nextInt(size);
            	
            	if(played.size() >= size){
            		played.clear();
            	}
            	
            	while(played.contains(number)){
            		number = random.nextInt(size);
            	}
            	
            	Text message = TextSerializers.FORMATTING_CODE.deserialize(Main.getBroadcasts().get(number));

            	Main.getGame().getServer().getBroadcastChannel().send(Text.of(BroadcastTag.get().get().getTag(), TextColors.WHITE, ": ", message));
            	
            	played.add(number);
            }
			
        }).submit(Main.getPlugin());
	}    

}
