package com.github.niwaniwa.whitebird.pvp.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.niwaniwa.whitebird.core.player.WhiteBirdPlayer;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class InventoryCommand implements CommandExecutor {

	public final String pre  = "[§9WBC§r]";

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(pre+"ゲーム内から実行してください");
		} else if(args.length==0){sender.sendMessage(pre+"§cプレイヤーを指定してください。"); return true;}

		Player player = (Player) sender;
		Player target = Util.getPlayer(args[0]);

		if(target == null){
			sender.sendMessage(pre+"§cプレイヤーを指定してください");
			return true;
		}

		String title = "§6Last "+target.getName()+"'s Inventory";

		if(title.length() >= 32){
			title = "§6"+target.getName()+"'s Inventory";
		}

		Inventory inv = Bukkit.createInventory(null, player.getInventory().getContents().length, title);

		WhiteBirdPlayer white = Util.toWhiteBird(target);

		inv.setContents(white.getItems());

		player.openInventory(inv);

		return true;
	}

}
