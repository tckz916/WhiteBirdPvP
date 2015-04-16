package com.github.niwaniwa.whitebird.pvp.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.xml.sax.SAXException;

import com.github.niwaniwa.whitebird.core.player.WhiteBirdPlayer;
import com.github.niwaniwa.whitebird.pvp.WhiteBirdPvP;
import com.github.niwaniwa.whitebird.pvp.util.MapXmlReader;
import com.github.niwaniwa.whitebird.pvp.util.NullChunk;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class Arena {
	private static List<Arena> arenas = new ArrayList<Arena>();

	public static List<Arena> getArenas(){
		return arenas;
	}

	public static Arena getArena(String mapName){
		for(Arena arena : Arena.getArenas()){
			if(arena.getArenaName().equals(mapName)){
				return arena;
			}
		}
		return null;
	}

	public static List<Arena> getWaitArenas(){
		List<Arena> waitArena = new ArrayList<Arena>();
		for(Arena arena : Arena.getArenas()){
			if(arena.getArenaType().equals(ArenaType.WAITING)){
				waitArena.add(arena);
			}
		}
		return waitArena;
	}

	public static void registerArena(File source){
		if(!source.exists()){return;}
		if(source.isFile()){return;}
		File[] files = source.listFiles();
		try {
			Util.copyTransfer(source, new File(source.getName()));
		} catch (IOException e) {}
		for(File file : files){
			if(file.getName().equals("map.xml")){
				new Arena(source);
			}
		}
	}

	int arenaID;
	String MapName;
	String version;
	List<String> author;
	Location spawn;
	Location spawn2;
	World world;
	ArenaType type;
	List<Player> players;

	public Arena(File file){
		arenaID = arenas.size();
		MapXmlReader xml = new MapXmlReader(file+"/map.xml");
		try {

		xml.domRead();
		this.spawn = xml.getLocation("spawn1");
		this.spawn2 = xml.getLocation("spawn2");
		this.MapName = xml.getMapName();
		this.author = xml.getAuthor();
		this.version = xml.getVersion();
		setType(ArenaType.WAITING);
		load();
		System.out.println(world);
		players = new ArrayList<Player>();
		arenas.add(this);
		this.spawn.setWorld(world);
		this.spawn2.setWorld(world);

		}catch (SAXException | IOException | ParserConfigurationException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	public int getArenaID(){
		return arenaID;
	}

	public String getArenaName(){
		return MapName;
	}

	public String getVersion(){
		return version;
	}

	public World getWorld(){
		return world;
	}

	public ArenaType getArenaType(){
		return type;
	}

	public List<Player> getArenaPlayers(){
		return players;
	}

	public List<Location> getLocations(){
		List<Location> locs = new ArrayList<Location>();
		locs.add(spawn);
		locs.add(spawn2);
		return locs;
	}

	public List<String> getAuthors(){
		return this.author;
	}

	public String getAuthor(){
		StringBuffer buffer = new StringBuffer();
		for(String str : author){
			buffer.append(str+",");
		}
		return buffer.toString();
	}

	public boolean addPlayer(Player player){
		return players.add(player);
	}

	public boolean removePlayer(Player player){
		return players.remove(player);
	}

	public void setType(ArenaType type){
		this.type = type;
	}

	public boolean stopArena(){
		if(getArenaType().equals(ArenaType.WAITING)){return false;}
		setType(ArenaType.WAITING);
		players.clear();
		return true;
	}

	public boolean load(){
		WorldCreator creator = new WorldCreator(getArenaName());
		creator.generator(new NullChunk());
		this.world = creator.createWorld();
		this.world.setSpawnFlags(false, false);
		this.world.setPVP(true);
		return true;
	}

	public boolean unload(){
		stopArena();
		WhiteBirdPvP.getInstance().getServer().unloadWorld(this.getWorld(), false);
		arenas.remove(this);
		return true;
	}

	public void reload(){
		stopArena();
		WhiteBirdPvP.getInstance().getServer().unloadWorld(this.getWorld(), false);
		load();
	}

	public boolean remove(){
		stopArena();
		WhiteBirdPvP.getInstance().getServer().unloadWorld(this.getWorld(), false);
		File uid = new File(this.getWorld().getWorldFolder()+"/uid.dat");
		uid.delete();
		File playerdata = new File(this.getWorld().getWorldFolder()+"/playerdata");
		playerdata.delete();
		File session = new File(this.getWorld().getWorldFolder()+"/session.lock");
		session.delete();
		File data = new File(this.getWorld().getWorldFolder()+"/data");
		data.delete();
		return true;
	}

	public List<WhiteBirdPlayer> toWhiteBirdPlayer(){
		List<WhiteBirdPlayer> temp = new ArrayList<WhiteBirdPlayer>();
		for(Player player : this.getArenaPlayers()){
			temp.add(WhiteBirdPlayer.getPlayer(player));
		}
		return temp;
	}
}