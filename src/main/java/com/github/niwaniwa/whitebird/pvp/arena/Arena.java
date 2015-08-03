package com.github.niwaniwa.whitebird.pvp.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.xml.sax.SAXException;

import com.github.niwaniwa.whitebird.core.player.WhiteBirdPlayer;
import com.github.niwaniwa.whitebird.pvp.util.MapXmlReader;
import com.github.niwaniwa.whitebird.pvp.util.NullChunk;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class Arena {
	private static List<Arena> arenas = new ArrayList<Arena>();

	public final static String path = "arenas/";

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

	public static List<Arena> registerArena(File source){
		List<Arena> arenas = new ArrayList<Arena>();
		if(!source.exists()){return arenas;}
		if(source.isFile()){return arenas;}
		File[] files = source.listFiles();
		UUID uuid = UUID.randomUUID();
		for(File file : files){
			if(file.getName().equals("map.xml")){
				try {
					Util.copyTransfer(source, new File(path + uuid.toString()));
				} catch (IOException e) {}
				Arena arena = new Arena(source,uuid);
				arenas.add(arena);
			}
		}
		return arenas;
	}

	public static void disableArenas() {

		for (Arena arena : arenas) {
			arena.remove(true);
		}

		arenas.clear();

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
	UUID uuid;
	boolean ratio;

	public Arena(File file, UUID uuid){
		arenaID = arenas.size();
		System.out.println(file);
		this.uuid = uuid;
		MapXmlReader xml = new MapXmlReader(file+"/map.xml");
		try {

		xml.domRead();
		this.spawn = xml.getLocation("spawn1");
		this.spawn2 = xml.getLocation("spawn2");
		this.MapName = xml.getMapName();
		this.author = xml.getAuthor();
		this.version = xml.getVersion();
		this.ratio = false;
		setType(ArenaType.WAITING);
		load();
//		System.out.println(world);
		players = new ArrayList<Player>();
		arenas.add(this);
		this.spawn.setWorld(world);
		this.spawn2.setWorld(world);

		}catch (SAXException | IOException | ParserConfigurationException e) {
			System.out.println(e.getLocalizedMessage());
			for(int i = 0; i < Arena.getArenas().size(); i++){
				if(Arena.getArenas().get(i).equals(this))
					this.remove(true);
			}
			this.remove(false);
		} finally {

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

	public UUID getArenaUUID(){
		return uuid;
	}

	public boolean getRatioMode(){
		return ratio;
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

	public void setRatioMode(boolean bool){
		this.ratio = bool;
	}

	public boolean stopArena(){
		if(getArenaType().equals(ArenaType.WAITING)){return false;}
		setType(ArenaType.WAITING);
		setRatioMode(false);
		players.clear();
		return true;
	}

	public boolean load(){
		WorldCreator creator = new WorldCreator(path + getArenaUUID().toString());
		creator.generator(new NullChunk());
		System.out.println(creator);
		deleteFile(new File(getArenaName()));
		this.world = creator.createWorld();
		System.out.println(world);
		this.world.setSpawnFlags(false, false);
		this.world.setPVP(true);
		return true;
	}

	public boolean unload() {
        stopArena();
        if (Bukkit.unloadWorld(this.getWorld(), false)) {
            System.out.println("- " + this.getArenaName() + " unloaded -");
        } else {
            System.out.println("Could not unload world (players on it or main world?)");
            return false;
        }
        return true;
    }

	public void reload(){
		stopArena();
		load();
	}

	public boolean remove(boolean check) throws NullPointerException {
        if (check) {
            unload();
        }

        File mapFile = new File(path + uuid.toString());
        Util.deleteFile(mapFile);
        return true;
    }

	public List<WhiteBirdPlayer> toWhiteBirdPlayer(){
		List<WhiteBirdPlayer> temp = new ArrayList<WhiteBirdPlayer>();
		for(Player player : this.getArenaPlayers()){
			temp.add(WhiteBirdPlayer.getPlayer(player));
		}
		return temp;
	}

	private void deleteFile(File path){
		File session = new File(path+"/session.lock");
		File stats = new File(path+"/stats");
		File playerdata = new File(path+"/playerdata");
		Util.deleteFile(session);
		Util.deleteFile(stats);
		Util.deleteFile(playerdata);
	}

}