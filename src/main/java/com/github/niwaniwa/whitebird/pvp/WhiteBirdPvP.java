package com.github.niwaniwa.whitebird.pvp;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.niwaniwa.whitebird.pvp.arena.Arena;
import com.github.niwaniwa.whitebird.pvp.command.InventoryCommand;
import com.github.niwaniwa.whitebird.pvp.command.RatioCommand;
import com.github.niwaniwa.whitebird.pvp.command.SpawnCommand;
import com.github.niwaniwa.whitebird.pvp.command.WhiteBirdPvPCommand;
import com.github.niwaniwa.whitebird.pvp.command.arena.ArenaCommand;
import com.github.niwaniwa.whitebird.pvp.command.arena.ArenaLeaveCommand;
import com.github.niwaniwa.whitebird.pvp.command.arena.MapsCommand;
import com.github.niwaniwa.whitebird.pvp.command.arena.RandomMatch;
import com.github.niwaniwa.whitebird.pvp.command.duel.AcceptCommand;
import com.github.niwaniwa.whitebird.pvp.command.duel.DuelCommand;
import com.github.niwaniwa.whitebird.pvp.item.EnderPearle;
import com.github.niwaniwa.whitebird.pvp.item.PotionListener;
import com.github.niwaniwa.whitebird.pvp.listener.ArenaListener;
import com.github.niwaniwa.whitebird.pvp.listener.PlayerListener;
import com.github.niwaniwa.whitebird.pvp.util.message.MessageManager;

public class WhiteBirdPvP extends JavaPlugin {

	private static WhiteBirdPvP instance;
	public static final String prefix = "§r[§91vs1§r]";

	RandomMatch rand;

	@Override
	public void onEnable(){
		instance = this;

		rand = new RandomMatch();

		registerCommands();
		regiterListeners();
		registersArena();

//		rand.runTaskTimer(this, 60, 40);

		MessageManager.loadLangFiles(false);

	}

	@Override
	public void onDisable(){
		Arena.disableArenas();
	}

	public static WhiteBirdPvP getInstance(){
		return instance;
	}

	public static void registerListener(Listener listener){
		Bukkit.getPluginManager().registerEvents(listener, WhiteBirdPvP.getInstance());
	}

	private void registerCommands(){
		getCommand("whitebirdpvp").setExecutor(new WhiteBirdPvPCommand());
		getCommand("arena").setExecutor(new ArenaCommand());
		getCommand("duel").setExecutor(new DuelCommand());
		getCommand("accept").setExecutor(new AcceptCommand());
		getCommand("match").setExecutor(new RandomMatch());
		getCommand("maps").setExecutor(new MapsCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("leave").setExecutor(new ArenaLeaveCommand());
		getCommand("getInventory").setExecutor(new InventoryCommand());
		getCommand("ratio").setExecutor(new RatioCommand());
//		getCommand("namechange").setExecutor(new NameChangeCommand());
	}

	private void regiterListeners(){
		registerListener(new ArenaListener());
		registerListener(new PlayerListener());
		registerListener(new EnderPearle());
		registerListener(new PotionListener());
	}

	private void registersArena(){
		File toPath = new File("arenas/");
		File source = new File("maps/");

		if(!toPath.exists()){
			toPath.mkdirs();
		}

		if(!source.exists()){
			System.out.println("THIS SERVER IS PLUGIN TEST MODE !!");
//			disable();
			return;
		}
		if(source.isFile()){return;}
		File[] files = source.listFiles();
		for(File temp : files){
			Arena.registerArena(temp);
		}
		if(Arena.getArenas().size()==0){
			disable();
			return;
		}
		getLogger().info("load map : "+Arena.getArenas().size());
		try {
			new File("arenas.txt").createNewFile();
		} catch (IOException e) {}
	}

	private void disable(){
		getLogger().warning("Map is Empty! This plugin is Disable!");
//		Bukkit.getPluginManager().disablePlugin(this);
	}

	public static File getPluginJarFile() {
        return getInstance().getFile();
    }

}
