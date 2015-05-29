package com.github.niwaniwa.whitebird.pvp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.whitebird.core.player.WhiteBirdPlayer;
import com.github.niwaniwa.whitebird.pvp.WhiteBirdPvP;
import com.github.niwaniwa.whitebird.pvp.arena.Arena;

public class Util {

	private Util(){}

	public static void broadcastMessage(String message){
		for(Player player : Bukkit.getOnlinePlayers()){
			WhiteBirdPlayer p = Util.toWhiteBird(player);
			if(p.isChat()){
				player.sendMessage(message);
			}
		}
	}

	public static Player getPlayer(String name){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getName().equals(name)){
				return player;
			}
		}
		return null;
	}

	public static Arena getArena(Player player){
		for(Arena arena : Arena.getArenas()){
			for(Player p : arena.getArenaPlayers()){
				if(p.equals(player)){
					return arena;
				}
				continue;
			}
		}
		return null;
	}

	public static Integer getPing(Player player){
		if(Bukkit.getVersion().indexOf("1.7")==-1){
			return com.github.niwaniwa.whitebird.core.util.Util.getPing(player);
		}
		return null;
	}

	public static WhiteBirdPlayer toWhiteBird(Player player){
		return WhiteBirdPlayer.getPlayer(player);
	}

	public static boolean contains(Map<?, ?> map,Object obj){
		if(map.containsKey(obj)){
			return true;
		}
		for(Object temp : map.values()){
			if(temp.equals(obj)){
				return true;
			}
		}
		return false;
	}

	public static Object get(Map<?, ?> map,Object obj1){
		for(Object key : map.keySet()){
			if(map.get(key).equals(obj1)){
				return key;
			}
		}
		return map.get(obj1);
	}

	public static void teleportSpawn(Player player){
		new BukkitRunnable() {
			@Override
			public void run() {
				player.teleport(Bukkit.getWorld("world").getSpawnLocation());
			}
		}.runTaskLater(WhiteBirdPvP.getInstance(), 5*20);
	}

	public static void copyTransfer(final String srcPath, final String destPath)
			  throws IOException {
		copyTransfer(new File(srcPath), new File(destPath));
	}

	public static void deleteFile(File f){
		if(f.exists() == false){return;}
		if(f.isFile()){
			f.delete();
		} else if(f.isDirectory()){
			File[] files = f.listFiles();
			for(int i=0; i<files.length; i++){
				deleteFile(files[i]);
			}
			f.delete();
		}
	}

	public static void copyTransfer(final File src, final File dest)
			  throws IOException {
		if (src.isDirectory()) {
			if(!dest.exists()){
				dest.mkdir();
			}
			String[] files = src.list();

			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				copyTransfer(srcFile, destFile);
			}
		} else {
			try (FileChannel srcChannel = new FileInputStream(src).getChannel();
					FileChannel destChannel = new FileOutputStream(dest).getChannel();) {
				srcChannel.transferTo(0, srcChannel.size(), destChannel);
			}
		}
	}

	public static String getPlayerLanguage(Player player){
		String s = null;
		try{
			Method getHandle = player.getClass().getMethod("getHandle");
			Object entityPlayer = getHandle.invoke(player);
			Field field = entityPlayer.getClass().getDeclaredField("locale");
			field.setAccessible(true);
			s = (String) field.get(entityPlayer);
		} catch (Exception e){}

		return s;

	}



}
