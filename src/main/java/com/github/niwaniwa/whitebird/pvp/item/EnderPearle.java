package com.github.niwaniwa.whitebird.pvp.item;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.whitebird.pvp.WhiteBirdPvP;

public class EnderPearle implements Listener {

	private ArrayList<Player> player = new ArrayList<Player>();

	@EventHandler
	public void onTeleprot(PlayerInteractEvent event){
		if(!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){return;}
		if(event.getItem()==null){return;}
		if(!event.getItem().getType().equals(Material.ENDER_PEARL)){
			return;
		}
		if(player.contains(event.getPlayer())){
			event.getPlayer().sendMessage("§7現在クールタイムのため使用できません");
			event.setCancelled(true);
			return;
		}
		int wait =13;
		player.add(event.getPlayer());
		event.getPlayer().sendMessage("§7クールタイムが有効になりました");
		event.getPlayer().sendMessage("§7クールタイムは§6 " + wait + " §7秒です");
		new BukkitRunnable() {
			@Override
			public void run() {
				player.remove(event.getPlayer());
				event.getPlayer().sendMessage("§7クールタイムが解除されました");
			}
		}.runTaskLater(WhiteBirdPvP.getInstance(), wait*20);
	}
}
