package com.gmail.trentech.simplechat.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simpletags.tags.BroadcastTag;

import ninja.leaping.configurate.ConfigurationNode;

public class Broadcast {

	private static List<Text> broadcasts = new ArrayList<>();
	
    public Task task;

    public void start(ConfigurationNode config) {
    	List<Integer> played = new ArrayList<>();
    	
        int minutes = config.getNode("Broadcast", "Minutes").getInt();
        
        task = Main.getGame().getScheduler().createTaskBuilder().interval(minutes, TimeUnit.MINUTES).name("broadcast").execute(new Runnable() {
        	
			@Override
            public void run() {
				ThreadLocalRandom random = ThreadLocalRandom.current();
				int size = getBroadcasts().size();
				
            	int number = random.nextInt(size);
            	
            	if(played.size() >= size){
            		played.clear();
            	}
            	
            	while(played.contains(number)){
            		number = random.nextInt(size);
            	}

            	Main.getGame().getServer().getBroadcastChannel().send(Text.of(BroadcastTag.get().get().getTag(), TextColors.WHITE, ": ", getBroadcasts().get(number)));
            	
            	played.add(number);
            }
			
        }).submit(Main.getPlugin());
	}
    

    
    public static void init(){
		ConfigurationNode config = new ConfigManager().getConfig();

		for(String broadcast : config.getNode("Broadcast", "Messages").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList())){
			getBroadcasts().add(Main.processText(broadcast));
		}
		
		if(config.getNode("Broadcast", "Enable").getBoolean()){
			new Broadcast().start(config);
		}
    }
    
    public static List<Text> getBroadcasts(){
    	return broadcasts;
    }

}
