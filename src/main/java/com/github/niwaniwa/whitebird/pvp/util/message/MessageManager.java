package com.github.niwaniwa.whitebird.pvp.util.message;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.niwaniwa.whitebird.core.player.WhiteBirdPlayer;
import com.github.niwaniwa.whitebird.core.util.Util;
import com.github.niwaniwa.whitebird.pvp.WhiteBirdPvP;

public class MessageManager {

	private MessageManager(){}

	protected static Map<LanguageType, YamlConfiguration> lang = new HashMap<LanguageType, YamlConfiguration>();

	public static LanguageType getLanguage(Player player){
		try{

			Object o = getEntityPlayer(player);
			String s = (String) getValue(o, "locale");
			return LanguageType.valueOf(s);

		} catch(Exception e) {}

		return LanguageType.en_US;
	}

	public static <T extends WhiteBirdPlayer> LanguageType getLanguage(T player){
		return getLanguage(player.getPlayer());
	}

	/**
	 * 言語ファイルからkeyに保存されているメッセージを返します
	 * @param type 言語コード
	 * @param prefix prefix
	 * @param key key
	 * @param codeReplace カラーコードを置換するか
	 * @return String messages
	 */
	public static String getMessage(LanguageType type, String prefix, String key, boolean colorcodeReplace){

		YamlConfiguration lang = getLangFile(type);

		String message = prefix + lang.getString(key);

		if(lang.getString(key) == null){
			return getMessage(LanguageType.defaultLang, prefix, key, colorcodeReplace);
		}

		if(colorcodeReplace){
			message = ChatColor.translateAlternateColorCodes('&', message);
		}

		return message;

	}

	public static String getMessage(LanguageType type, String prefix, String key){
		return getMessage(type, prefix, key, false);
	}

	public static String getMessage(Player player, String prefix, String key, boolean colorcodeReplace){
		return getMessage(getLanguage(player), prefix, key, colorcodeReplace);
	}

	public static <T extends WhiteBirdPlayer> String getMessage(T player, String prefix, String key){
		return getMessage(player.getPlayer(), "", key, true);
	}

	public static String getMessage(Player player, String prefix, String key){
		return getMessage(getLanguage(player), prefix, key, true);
	}

	public static String getMessage(Player player, String key){
		return getMessage(getLanguage(player), "", key, true);
	}

	public static <T extends WhiteBirdPlayer> String getMessage(T player, String key){
		return getMessage(getLanguage(player), "", key, true);
	}

	public static String getMessage(CommandSender sender, String msgprefix, String key, boolean colorcodeReplace) {

		if (sender instanceof Player) {
			return getMessage((Player) sender, msgprefix, key, colorcodeReplace);
		}
		return getMessage(LanguageType.defaultLang, msgprefix, key, colorcodeReplace);
	}

	public static String getMessage(CommandSender sender, String msgPrefix, String key) {
		return getMessage(sender, msgPrefix, key, true);
	}

	private static Object getValue(Object instance, String fieldName) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}

	private static Object getEntityPlayer(Player player) throws Exception {
		Method getHandle = player.getClass().getMethod("getHandle");
		Object entityPlayer = getHandle.invoke(player);
		return entityPlayer;
	}

	/**
	 * 指定した言語の言語ファイルを返します
	 * @param type 言語コード
	 * @return YamlConfiguration 言語ファイル(nullの場合"Ja_JP"を返す)
	 */
	protected static YamlConfiguration getLangFile(LanguageType type){

//		if(lang.isEmpty()){loadLangFiles(false);}

		if(lang.get(type) == null){
			return lang.get(LanguageType.defaultLang);
		}

		return lang.get(type);

	}

	public static Map<LanguageType, YamlConfiguration> loadLangFiles(boolean send){

		Map<LanguageType, YamlConfiguration> map = new HashMap<LanguageType, YamlConfiguration>();

		File file = new File(WhiteBirdPvP.getInstance().getDataFolder(), "/lang/");

		if(!file.exists()){
			file.mkdirs();

			Util.copyFileFromJar(
					file,
					WhiteBirdPvP.getPluginJarFile(),
					"lang/ja_JP.yml");
/*			Util.copyFileFromJar(
					new File(WhiteBirdPvP.getInstance().getDataFolder()+"/lang/"),
					WhiteBirdPvP.getPluginJarFile(),
					"lang/ja_JP.yml");
*/		}

		for(File f : file.listFiles()){

			if(!f.getName().endsWith(".yml")){ continue;}

			String[] name = f.getName().split("/");

			map.put(LanguageType.valueOf(name[0].split(".yml")[0]), YamlConfiguration.loadConfiguration(f));

		}

		lang = map;

		if(send){ System.out.println("Lang loading... : "+map.size());}

		return map;

	}

}
