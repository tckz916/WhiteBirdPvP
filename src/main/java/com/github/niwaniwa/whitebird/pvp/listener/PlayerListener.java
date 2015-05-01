package com.github.niwaniwa.whitebird.pvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.niwaniwa.whitebird.core.rank.Rank;
import com.github.niwaniwa.whitebird.pvp.arena.Arena;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class PlayerListener implements Listener {

	@EventHandler
	public void block(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(Util.getArena(player)!=null){
			event.setCancelled(true);
			return;
		}
		if(!player.hasPermission(Rank.getRank("Moderator").getPermission())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void block(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if(Util.getArena(player)!=null){
			event.setCancelled(true);
			return;
		}
		if(!player.hasPermission(Rank.getRank("Moderator").getPermission())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void item(ItemSpawnEvent event){
		event.setCancelled(true);
	}

	@EventHandler
	public void food(FoodLevelChangeEvent event){
		if(!(event.getEntity() instanceof Player)){return;}
		if(Util.getArena((Player) event.getEntity())==null){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onleft(PlayerQuitEvent event){
		Player player = event.getPlayer();
		Arena arena = Util.getArena(player);
		if(arena!=null){
			arena.stopArena();
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		String command = event.getMessage();//プレイヤーがコマンドとして入力した"/"で始まるメッセージ
		Player player = event.getPlayer();
		if(command.equalsIgnoreCase("/kill")){
			Arena arena = Util.getArena(player);
			if(arena!=null){
				player.sendMessage("§c試合中は実行できません。");
				player.sendMessage("§c試合中は/leaveコマンドを使用してください。");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		if(loc.getBlockY() <= -100){
			player.teleport(Bukkit.getWorld("world").getSpawnLocation());
		}
		return;
	}

}
