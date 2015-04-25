package com.github.niwaniwa.whitebird.pvp.listener;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.github.niwaniwa.whitebird.core.event.WhiteBirdPlayerDamageEvent;
import com.github.niwaniwa.whitebird.core.event.WhiteBirdPlayerDeathEvent;
import com.github.niwaniwa.whitebird.core.player.WhiteBirdPlayer;
import com.github.niwaniwa.whitebird.core.util.Title;
import com.github.niwaniwa.whitebird.core.util.WhiteTell;
import com.github.niwaniwa.whitebird.pvp.WhiteBirdPvP;
import com.github.niwaniwa.whitebird.pvp.arena.Arena;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class ArenaListener implements Listener {

	public final String pre = "§r[§91vs1§r]";

	/**
	 * アリーナでの死亡判定
	 * @param event WhiteBirdPlayerDeathEvent
	 */
	@EventHandler
	public void onDeath(WhiteBirdPlayerDeathEvent event){
		Player death = event.getDamager();
		Player killer = event.getKiller();
		Arena arena1 = Util.getArena(death);
		Arena arena2 = Util.getArena(killer);
		// null --> return
		if(arena1 == null){return;}
		event.setMessage(false);
		if(killer==null){
			try{
			Util.broadcastMessage(event.getDeathMessage());

			for(Player p : Util.getArena(death).getArenaPlayers()){
				if(!p.equals(death)){
					Util.teleportSpawn(p);
					//title(p, ": Win :", ChatColor.RED);

					WhiteBirdPlayer white = Util.toWhiteBird(p);

					white.setItems(p.getInventory().getContents());

					tell(death, p);
					tell(p, death);
				}
			}

//			title(death, " : Lose :", ChatColor.RED);

			arena1.stopArena();

			}catch(NullPointerException e){
				System.out.println(e.getMessage());
			}
			return;
		}

		if(arena2 == null
				|| arena1 != arena2){return;}

		potSendMessage(killer, death);
		potSendMessage(death, killer);
		healthSendMessage(killer, death);
		healthSendMessage(death, killer);
		arena1.stopArena();
		String message = deathMessage(death);
		message = message.replaceAll("%k", Util.toWhiteBird(killer).getFullName());
		if(Util.getPing(killer)!=null){
			message = message.replaceAll("%p", String.valueOf(Util.getPing(killer)));
		}

		Util.broadcastMessage(message);
		Util.teleportSpawn(killer);

		WhiteBirdPlayer white = Util.toWhiteBird(killer);

		white.setItems(killer.getInventory().getContents());

//		title(killer, ": Win :", ChatColor.RED);

//		title(death, " : Lose :", ChatColor.RED);

		tell(death, killer);

		tell(killer, death);

	}

	/**
	 * ダメージ判定
	 * @param event
	 */
	@EventHandler
	public void onDamage(WhiteBirdPlayerDamageEvent event){
		Player damager = event.getDamagePlayer();
		event.setDamage(event.getDamage()-1);
		if(Util.getArena(damager)==null){

			if(!damager.getWorld().equals(Bukkit.getWorld("world_the_end"))){
				event.setCancelled(true);
			}
			return;
		}
	}

	private void healthSendMessage(Player player,Player target){
		player.sendMessage(WhiteBirdPvP.prefix + " §r" + target.getName()+" §dHealth(❤) : "
				+ getHealth(target));
	}

	private void potSendMessage(Player player,Player target){
		player.sendMessage(WhiteBirdPvP.prefix + " §r" + target.getName()+" §6Pots : "
				+ getPots(target));
	}

	private Integer getPots(Player player){
		ItemStack[] itemStacks = player.getInventory().getContents();
		int pots = 0;
		for(ItemStack is : itemStacks){
			if(is == null){continue;}
			if(is.getType().equals(org.bukkit.Material.POTION)){
				if(is.getDurability()==16421){
					pots++;
				}
			}
		}
		return pots;
	}

	private double getHealth(Player player){
		BigDecimal bd = new BigDecimal(player.getPlayer().getHealth());
		BigDecimal two = new BigDecimal("2");
		double temp = bd.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		bd = new BigDecimal(temp);
		double temp2 = bd.divide(two,BigDecimal.ROUND_HALF_UP).doubleValue();
		return temp2;
	}

	private String deathMessage(Player player){
		DamageCause damage = player.getLastDamageCause().getCause();
		String killMessage = null;
		switch(damage){
		case PROJECTILE:
			killMessage = "§c"+Util.toWhiteBird(player).getFullName()
			+" §eは §c%k §eから撃たれた。";
			break;
		case SUFFOCATION:
			break;
		case FALL:
			killMessage = "§c"+Util.toWhiteBird(player).getFullName()
				+" §eは §c%k §eに突き落とされた。";
			break;
		case FIRE:
			killMessage = "§c"+Util.toWhiteBird(player).getFullName()
					+" §eは §c%k §eの魔法で燃え尽きた。";
			break;
		case FIRE_TICK:
			killMessage = "§c"+Util.toWhiteBird(player).getFullName()
			+" §eは §c%k §eの魔法で燃え尽きた。";
			break;
		default:
			killMessage = "§c"+Util.toWhiteBird(player).getFullName()
			+" §eは §c%k §eに殺害された。";
			break;
		}
//		String str = "\n %kPing: %p";
		return killMessage;
	}

	public void title(Player player, String message, ChatColor color){
		Title title = new Title(message);
		title.setTitleColor(color);
		title.send(player);
	}

	private void tell(Player player, Player target){
		WhiteTell tell = new WhiteTell(pre+"§a"+target.getName()+" §6のインベントリを見る", "：クリック : インベントリを見る",
				"/getInventory "+target.getName(), "run_command", ChatColor.GOLD, ChatColor.LIGHT_PURPLE);
		tell.sand(player);
	}

}
