package com.github.niwaniwa.whitebird.pvp.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.whitebird.pvp.WhiteBirdPvP;
import com.github.niwaniwa.whitebird.pvp.util.message.MessageManager;

public class EnderPearle implements Listener {

	private ArrayList<Player> player = new ArrayList<Player>();
	private Map<Player,Integer> time = new HashMap<Player,Integer>();

	@EventHandler
	public void onTeleprot(PlayerInteractEvent event){
		int i1 = 0;
		if(!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){return;}
		if(event.getItem()==null){return;}
		if(!event.getItem().getType().equals(Material.ENDER_PEARL)){
			return;
		}
		if(player.contains(event.getPlayer())){
			i1 = time.get(event.getPlayer());
			event.getPlayer().sendMessage(
					MessageManager.getMessage(event.getPlayer(), "Item.enderPearle.notUse")
					.replaceAll("%t", String.valueOf(i1)));
			event.setCancelled(true);
			return;
		}
		int wait = 13;
		player.add(event.getPlayer());
		event.getPlayer().sendMessage(
				MessageManager.getMessage(event.getPlayer(), "Item.enderPearle.coolTimeEnable")
				.replaceAll("%t", String.valueOf(wait)));
		time.put(event.getPlayer(), wait);
		new BukkitRunnable() {
			int i = 0;
			int i2 = wait;
			@Override
			public void run() {
				if(i == wait){
					player.remove(event.getPlayer());
					event.getPlayer().sendMessage(MessageManager.getMessage(event.getPlayer(), "Item.enderPearle.coolTimeDisable"));
					this.cancel();
					return;
				} else {
					i++;
					i2--;
					time.put(event.getPlayer(), i2);
				}
			}
		}.runTaskTimer(WhiteBirdPvP.getInstance(), 20 , 20);

	}
}
