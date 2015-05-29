package com.github.niwaniwa.whitebird.pvp.command.duel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.whitebird.pvp.WhiteBirdPvP;
import com.github.niwaniwa.whitebird.pvp.conf.MessageManager;
import com.github.niwaniwa.whitebird.pvp.util.Util;

public class DuelCommand implements CommandExecutor {

	private static HashMap<Player,Player> duelList = new HashMap<Player,Player>();

	public final String pre  = "[§9WBC§r]";

	public static HashMap<Player, Player> getDuelPlayers(){
		return duelList;
	}

	@Override
	public boolean onCommand(CommandSender sender,
			Command command, String label,
			String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(pre+MessageManager.getString(sender, "Commnads.Console"));
		} else if(args.length==0){sender.sendMessage(pre+MessageManager.getString(sender, "Commands.notPlayer")); return true;}

		Player player = (Player) sender;
		Player target = Util.getPlayer(args[0]);

		if(target == null){sender.sendMessage(pre+MessageManager.getString(player, "Commands.notFoundPlayer")); return true;}

		if(Util.getArena(player)!=null){
			sender.sendMessage(pre+MessageManager.getString(player, "Duel.inGame"));
			return true;
		} else if(Util.getArena(target)!=null){
			sender.sendMessage(pre+MessageManager.getString(sender, "Duel.targetInGame")
					.replaceAll("%p", target.getName()));
			return true;
		}

		if(duelList.containsKey(player)){
			sender.sendMessage(pre+"§c"+duelList.get(player).getName()+"に申請した内容は破棄されました。");
			duelList.remove(player);
		}

		duelList.put(player, target);
		duelMessage(target, player);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), build(target, player));
		target.sendMessage(pre+"§640秒経つと申請は破棄されます");
		run(player);
		return true;
	}

	private void run(Player player){
		new BukkitRunnable() {
			@Override
			public void run() {
				if(duelList.containsKey(player)){
					player.sendMessage(pre+MessageManager.getString(player, "Duel.duelTimeOut"));
					duelList.remove(player);
					this.cancel();
					return;
				}
				this.cancel();
				return;
			}
		}.runTaskLater(WhiteBirdPvP.getInstance(), 40*20);
	}

	private void duelMessage(Player target,Player player){
		duelMessage_P(target, player);
		duelMessage_T(target, player);
	}

	private void duelMessage_P(Player target,Player player){
		player.sendMessage(pre+MessageManager.getString(target, "Duel.duelRequestedTo")
				.replaceAll("%p", Util.toWhiteBird(target).getPlayer().getName()));
	}

	private void duelMessage_T(Player target,Player player){
		target.sendMessage(pre+MessageManager.getString(target, "Duel.duelRequestedFrom")
				.replaceAll("%p", Util.toWhiteBird(player).getPlayer().getName()));
	}

	/**
	 * accept commandの送信
	 * @param target target player
	 * @param player player
	 * @return String
	 */
	private String tellrawMessage(Player target,Player player){
		ArrayList<String> items = new ArrayList<String>();
		items.add("\"text\":\"" + "§r"+pre+MessageManager.getString(target, "Duel.duelAcceptTellraw")
				.replaceAll("%p", Util.toWhiteBird(player).getPlayer().getName())
				+ "\"");
		items.add("\"color\":\"" + ChatColor.LIGHT_PURPLE.name().toLowerCase() + "\"");
		items.add("\"" + "underlined" + "\":\"true\"");
		items.add("\"clickEvent\":"
                + "{\"action\":\"" + "run_command" + "\","
                        + "\"value\":\"" + "/accept "+player.getName() + "\"}");
		String temp = "{\"text\":\"" + "クリック : 申請を受理" + "\","
                + "\"color\":\"" + ChatColor.LIGHT_PURPLE.name().toLowerCase() + "\"}";
		items.add("\"hoverEvent\":"
                + "{\"action\":\"show_text\","
                        + "\"value\":{\"text\":\"\",\"extra\":["
                        + temp + "]}}");
		return "{" + join(items) + "}";
	}

	private static String join(List<String> arr) {
        StringBuffer buffer = new StringBuffer();
        for ( String s : arr ) {
            if ( buffer.length() > 0 ) {
                buffer.append(",");
            }
            buffer.append(s);
        }
        return buffer.toString();
    }

	private String build(Player target,Player player) {
        return "tellraw "
                + target.getName()
                + " {\"text\":\"\",\"extra\":["
                + tellrawMessage(target, player)
                + "]}";
    }

}
