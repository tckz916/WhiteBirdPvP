package com.github.niwaniwa.whitebird.pvp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.niwaniwa.whitebird.pvp.WhiteBirdPvP;

public class WhiteBirdPvPCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(args.length == 0){return true;}
		switch(args[0]){
		case "reload":
			WhiteBirdPvP.getInstance().getPluginLoader().disablePlugin(WhiteBirdPvP.getInstance());
			WhiteBirdPvP.getInstance().getPluginLoader().enablePlugin(WhiteBirdPvP.getInstance());
			return true;
		case "history":
			sender.sendMessage("§6---- v 2.0.0 ----");
			sender.sendMessage("§6- Arena 変更");
			sender.sendMessage("§6- ");
		}
		return false;
	}

}
