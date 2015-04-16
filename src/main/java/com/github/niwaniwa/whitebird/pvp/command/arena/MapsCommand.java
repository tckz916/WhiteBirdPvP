package com.github.niwaniwa.whitebird.pvp.command.arena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.niwaniwa.whitebird.pvp.arena.Arena;
import com.github.niwaniwa.whitebird.pvp.arena.ArenaType;

public class MapsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender,
			Command command, String lavel,
			String[] args) {
		sender.sendMessage("§6----- map list -----");
		int i = 1;
		for(Arena arena : Arena.getArenas()){
			if(arena.getArenaType().equals(ArenaType.STARTING)){
				sender.sendMessage("§2"+i+" : §c"+arena.getArenaName() + "§6by §c"+arena.getAuthor());
				i++;
			} else {
				sender.sendMessage("§2"+i+" : §2"+arena.getArenaName() + "§6by §c"+arena.getAuthor());
				i++;
			}
		}
		return false;
	}

}
