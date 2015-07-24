package com.github.niwaniwa.whitebird.pvp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.niwaniwa.whitebird.pvp.raito.Ratio;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class RatioCommand implements CommandExecutor {

	public final String pre  = "[§9WBC§r]";

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(args.length == 0){
			sender.sendMessage(pre+"§6プレイヤーを指定してください");
			return true;
		}
		Player player = Util.getPlayer(args[0]);
		if(player == null){
			sender.sendMessage(pre+"プレイヤーが見つかりません");
			return true;
		}
		sender.sendMessage(pre+"§6"+player.getName()+"'s koke : "+Ratio.getRatio(player).getRatio());
		return true;
	}

}
