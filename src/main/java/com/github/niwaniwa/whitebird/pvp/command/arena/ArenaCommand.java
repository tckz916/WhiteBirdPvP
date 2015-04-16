package com.github.niwaniwa.whitebird.pvp.command.arena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.niwaniwa.whitebird.pvp.arena.Arena;

public class ArenaCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(args.length==0){return false;}
		if(args[0].equalsIgnoreCase("all-stop")){
			for(Arena arena : Arena.getArenas()){
				arena.stopArena();
			}
			sender.sendMessage("処理が終了しました");
			return true;
		} else if(args[0].equalsIgnoreCase("reload")){
			for(Arena arena : Arena.getArenas()){
				arena.reload();
			}
			sender.sendMessage("§2reload complete");
			return true;
		} else {
			sender.sendMessage("§6----- help -----");
			sender.sendMessage("/arena all-stop");
		}
		return false;
	}

}
