package com.gmail.trentech.simplechat.commands.chat;

import java.util.Set;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplechat.Main;
import com.gmail.trentech.simplechat.utils.Broadcast;
import com.gmail.trentech.simplechat.utils.ConfigManager;
import com.gmail.trentech.simpletags.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDBroadcast implements CommandExecutor {

	public CMDBroadcast(){
		Help help = new Help("broadcast", "broadcast", " Toggle on and off auto broadcasts");
		help.setSyntax(" /chat broadcast <boolean> [time]\n /c b <boolean> [value]");
		help.setExample(" /chat broadcast false\n /chat broadcast true 1");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("boolean")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/chat broadcast <boolean> [time]"));
			return CommandResult.empty();
		}
		String bool = args.<String>getOne("boolean").get();
		
		ConfigManager configManager = new ConfigManager();
		ConfigurationNode config = configManager.getConfig();
		
		if(bool.equalsIgnoreCase("true")){
			config.getNode("Broadcast", "Enable").setValue(true);
			
			if(args.hasAny("value")){
				String value = args.<String>getOne("value").get();
				try{
					config.getNode("Broadcast", "Minutes").setValue(value);
				}catch(Exception e){
					src.sendMessage(Text.of(TextColors.DARK_RED, value, " is not a valid value"));
					return CommandResult.empty();
				}
			}
			
			configManager.save();

			Set<Task> tasks = Main.getGame().getScheduler().getScheduledTasks();
			
			for(Task task : tasks){
				if(task.getName().equalsIgnoreCase("broadcast")){
					configManager.save();
					src.sendMessage(Text.of(TextColors.DARK_GREEN, "Auto Broadcast Enabled"));
					return CommandResult.success();
				}		
			}
			
			new Broadcast().start(config);
			
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Auto Broadcast Enabled"));
			
			return CommandResult.success();
		}else if(bool.equalsIgnoreCase("false")){
			configManager.getConfig().getNode("Broadcast", "Enable").setValue(false);			
			configManager.save();

			Set<Task> tasks = Main.getGame().getScheduler().getScheduledTasks();
			for(Task task : tasks){
				if(task.getName().equalsIgnoreCase("broadcast")){
					task.cancel();
					break;
				}	
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Auto Broadcast Disabled"));

			return CommandResult.success();
		}
		
		src.sendMessage(Text.of(TextColors.DARK_RED, "true or false"));
		
		return CommandResult.empty();
	}

}
