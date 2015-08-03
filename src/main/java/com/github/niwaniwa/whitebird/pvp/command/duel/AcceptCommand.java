package com.github.niwaniwa.whitebird.pvp.command.duel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;

import com.github.niwaniwa.whitebird.pvp.arena.Arena;
import com.github.niwaniwa.whitebird.pvp.arena.ArenaType;
import com.github.niwaniwa.whitebird.pvp.raito.Ratio;
import com.github.niwaniwa.whitebird.pvp.util.Util;
import com.github.niwaniwa.whitebird.pvp.util.message.MessageManager;

public class AcceptCommand implements CommandExecutor {

	public final String pre  = "[§9WBC§r]";

	@Override
	public boolean onCommand(CommandSender sender,
			Command command, String label,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(MessageManager.getMessage(sender, pre, "Commnads.Console"));
			return true;
		} else if(args.length==0){sender.sendMessage(MessageManager.getMessage(sender, pre,"Commands.notPlayer")); return true;}

		Player player = (Player) sender;
		Player target = Util.getPlayer(args[0]);

		if(target == null){sender.sendMessage(MessageManager.getMessage(player, pre, "Commands.notFoundPlayer")); return true;}

		if(Util.getArena(player)!=null){
			sender.sendMessage(MessageManager.getMessage(player, pre, "Duel.inGame"));
			return true;
		} else if(Util.getArena(target)!=null){
			sender.sendMessage(MessageManager.getMessage(sender, pre, "Duel.targetInGame")
					.replaceAll("%p", target.getName()));
			return true;
		}

		HashMap<Player,Player> map = DuelCommand.getDuelPlayers();

		if(!Util.contains(map, target)
				|| map.get(target)==null){
			sender.sendMessage(MessageManager.getMessage(sender, pre, "Duel.playerNotDuelReqested"));
			return true;
		}

		if(map.get(target).getName() != player.getName()){
			sender.sendMessage("§c"+target.getName()+"から申請は受けていません。");
			sender.sendMessage("§cもう一度申請し直してください。");
			return true;
		}

		Arena gameArena = randomArena();
		if(gameArena == null){
			sender.sendMessage(MessageManager.getMessage(sender, pre, "arena.full"));
			target.sendMessage(MessageManager.getMessage(target, pre, "arena.full"));
			return true;
		}

		setMode(player);
		setMode(target);

		if(args.length == 2){
			if(args[1].equalsIgnoreCase("-r")){
				sendRatio(player, target);
				gameArena.setRatioMode(true);
			}
		}

		settingArena(gameArena, player, target);

		DuelCommand.getDuelPlayers().remove(target);

		return true;
	}

	/**
	 * ランダムにアリーナを設定 ※選択方式を変更
	 * @return Arena
	 */
	public Arena randomArena(){
		List<Arena> arenaList = Arena.getWaitArenas();
		if(arenaList.isEmpty()){
			return null;
		}
		Collections.shuffle(arenaList);
		return arenaList.get(0);
	}

	public void setMode(Player player){
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setGameMode(GameMode.SURVIVAL);
		removeEffects(player);
	}

	private void removeEffects(Player player){
		for(PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
		player.setFireTicks(1);
	}

	public void settingArena(Arena arena, Player player1, Player player2){
		int i = 0;
		try{
		for(Location loc : arena.getLocations()){
			loc.setWorld(arena.getWorld());
			if(i==0){
				player1.teleport(loc);
				i++;
			} else if(i==1){
				player2.teleport(loc);
				i++;
			}
		}
		}catch(NullPointerException e){
			System.out.println("Location is null");
			errorMsg(player1);
			errorMsg(player2);
//			arena.stopArena();
//			arena.reload();
			player1.teleport(Bukkit.getWorld("world").getSpawnLocation(),TeleportCause.ENDER_PEARL);
			player2.teleport(Bukkit.getWorld("world").getSpawnLocation(),TeleportCause.ENDER_PEARL);
			DuelCommand.getDuelPlayers().remove(player2);
			return;
		}

		String temp = "§6： Map : §e"+arena.getArenaName();

		player1.sendMessage(temp);
		player2.sendMessage(temp);

		String temp2 = "§6： "+MessageManager.getMessage(player1, pre, "Arena.opponent")+" ： §d";

		player1.sendMessage(temp2+player2.getName());
		player2.sendMessage(temp2+player1.getName());

		arena.getArenaPlayers().clear();
		arena.addPlayer(player1);
		arena.addPlayer(player2);
		arena.setType(ArenaType.STARTING);

	}

	private void sendRatio(Player player,Player target){
		Ratio r1 = Ratio.getRatio(player);
		Ratio r2 = Ratio.getRatio(target);
		player.sendMessage("§6"+target.getName()+"'s koke : §d"+r1.getRatio() + " ");
		target.sendMessage("§6"+player.getName()+"'s koke : §d"+r2.getRatio() + " ");
	}

	private void errorMsg(Player p){
		p.sendMessage("§c問題が発生したため試合を開始することはできませんでした");
		p.sendMessage("§c申請は破棄されるため再度申請ください");
		p.sendMessage("§c/report <内容>で報告");
	}
}
