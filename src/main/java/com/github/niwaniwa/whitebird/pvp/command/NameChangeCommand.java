package com.github.niwaniwa.whitebird.pvp.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class NameChangeCommand implements CommandExecutor {

	public final String pre  = "[§9WBC§r]";

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(!(sender instanceof Player)){
			sender.sendMessage(pre+"ゲーム内から実行してください");
			return true;
		} else if(args.length==0){sender.sendMessage("§c/namechange <name>"); return true;}

		Player player = (Player) sender;

		ItemMeta meta = player.getItemInHand().getItemMeta();
		StringBuilder name = new StringBuilder();
		for(String str : args){
			name.append(str+" ");
		}

		String Iname =  name.toString();

		for(String source : getNGWord()){
			if(Iname.indexOf(source)!=-1){
				sender.sendMessage("§cNGwordです");
				return true;
			}
		}

		meta.setDisplayName(name.toString());
		player.getItemInHand().setItemMeta(meta);
		player.updateInventory();
		sender.sendMessage("§9武器名を ["+player.getItemInHand().getItemMeta().getDisplayName()+"]"
				+ "に変更しました。");
		return true;
	}

	private List<String> getNGWord(){
		List<String> word = new ArrayList<String>();
		word.add("0ppai");
		word.add("ti-nko");
		word.add("ti-nnko");
		word.add("sine");
		word.add("hideshine");
		word.add("hidesine");
		return word;
	}
}
