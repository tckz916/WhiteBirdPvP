package com.github.niwaniwa.whitebird.pvp.conf;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.niwaniwa.whitebird.core.util.Util;
import com.github.niwaniwa.whitebird.pvp.WhiteBirdPvP;

public class MessageManager {

	private static WhiteBirdPvP instance = WhiteBirdPvP.getInstance();

	public MessageManager(){

	}

	public static void copyLangFile(){
//		File data = instance.getDataFolder();
		File lang = new File("plugins/"+instance.getName()+"/lang");
		lang.mkdirs();

		Util.extractResource("/plugin.yml", new File("plugins/"+instance.getName()+"/lang"), true, true);
		Util.extractResource("/default.yml", new File("plugins/"+instance.getName()+"/lang"), true, true);
		Util.extractResource("/lang/ja_JP.yml", new File("plugins/"+instance.getName()+"/lang"), true, true);
		Util.extractResource("/lang/default.yml", new File("plugins/"+instance.getName()+"/lang"), true, false);

	}

	public static Configuration loadLangFile(String lang){

		File data = new File(getLandDir(), lang+".yml");

		if(!data.exists()){
			return null;
		}

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(data);

		return conf;

	}

	public static String getString(CommandSender sender, String key){
		if(!(sender instanceof Player)){
			return getString(sender, "Commnads.Console");
		}
		return getString((Player) sender, key);
	}

	public static String getString(Player player, String key){

		String l = com.github.niwaniwa.whitebird.pvp.util.Util.getPlayerLanguage(player);
		switch(l){
		case "ja_JP":
			break;
		default:
			l = "default";
			break;
		}

		return getString(l, key);
	}

	private static String getString(String lang, String key){
		Configuration c = loadLangFile(lang);
		if(c.get(key) == null){
			c = loadLangFile("ja_JP");
		}
		return (String) c.get(key);
	}

	private static File getLandDir(){
		return new File("plugins/WhiteBirdPvP/lang");
	}


}
