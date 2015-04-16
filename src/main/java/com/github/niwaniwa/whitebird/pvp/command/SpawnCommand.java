package com.github.niwaniwa.whitebird.pvp.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.niwaniwa.whitebird.pvp.util.Util;

public class SpawnCommand implements CommandExecutor {

	public final String pre  = "[§9WBC§r]";

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(pre+"ゲーム内から実行してください");
			return true;
		}

		Player player = (Player) sender;

		if(Util.getArena(player)!=null){
			sender.sendMessage(pre+"試合中は使用できません。");
			return true;
		}

		player.teleport(Bukkit.getWorld("world").getSpawnLocation());
		sender.sendMessage(pre+"§6Teleport to spawn.");

		return true;
	}

}
