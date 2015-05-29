package com.github.niwaniwa.whitebird.pvp.command.arena;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.niwaniwa.whitebird.pvp.arena.Arena;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class ArenaLeaveCommand implements CommandExecutor {

	public final String pre  = "[§9WBC§r]";

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(pre+"ゲーム内から実行してください");
			return true;
		}
		Player player = (Player) sender;
		if(Util.getArena(player)==null){
			sender.sendMessage("§c試合中ではありません");
			return true;
		}

		if(args.length == 0){
			sender.sendMessage("/leave <reason> : 理由");
			return true;
		}

		StringBuffer sb = new StringBuffer();
		for(String str : args){
			sb.append(str+" ");
		}

		Arena arena = Util.getArena(player);
		for(Player temp : arena.getArenaPlayers()){
			temp.sendMessage("§c"+player.getName()+"が "+sb.toString()+" のため試合を中断しました");
			Util.teleportSpawn(temp);
		}
		arena.stopArena();
		return false;
	}

}
