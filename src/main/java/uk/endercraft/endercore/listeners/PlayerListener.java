package uk.endercraft.endercore.listeners;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.endercraft.endercore.EnderCore;
import uk.endercraft.endercore.EnderPlayer;
import uk.endercraft.endercore.language.LanguageMain;
import uk.endercraft.endercore.managers.PlayerManager;
import uk.endercraft.endercore.utils.PacketUtils;
import uk.endercraft.endercore.utils.StringUtils;

public class PlayerListener implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if (PlayerManager.registerPlayer(e.getPlayer()).getRankID() == 0) {
			e.disallow(Result.KICK_OTHER,
					"We've got an error while connecting to the database... Try again in a few seconds.");
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		PacketUtils.setTabHeaderFooter(e.getPlayer(), "&dEnderCraft &5Network", "&5endercraft.uk");
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		PlayerManager.unregisterPlayer(e.getPlayer());
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		EnderPlayer ep = PlayerManager.getData(e.getPlayer());
		if (EnderCore.get().isMinigame()) {
			e.setFormat(ChatColor.GRAY + "[" + ChatColor.BLUE + ChatColor.BOLD
					+ ep.getMinigameData(EnderCore.get().getMinigameName()).getXp() + ChatColor.GRAY + "] "
					+ ep.getRank().getColoredDisplayName() + ChatColor.RESET + " %1$s " + ChatColor.RED + "\u25BA"
					+ ChatColor.RESET + " %2$s");
		} else
			e.setFormat(ep.getRank().getColoredDisplayName() + ChatColor.RESET + " %1$s " + ChatColor.RED + "\u25BA"
					+ ChatColor.RESET + " %2$s");
	}
	
	@EventHandler
	public void disablePluginsCmd(PlayerCommandPreprocessEvent e) {
		String[] args = e.getMessage().toLowerCase().split(" ");
		String cmd = args[0].substring(1);
		if (args.length > 1)
			args = Arrays.copyOfRange(args, 1, args.length - 1);
		else
			args = new String[0];
		if (StringUtils.equalsIgnoreCase(cmd, "plugins", "pl", "bukkit:plugins", "bukkit:pl")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(LanguageMain.get(e.getPlayer(), "filter.plugins"));
		} else if (StringUtils.equalsIgnoreCase(cmd, "help", "?", "bukkit:help", "bukkit:?", "minecraft:help",
				"minecraft:?")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(LanguageMain.get(e.getPlayer(), "filter.help"));
		}
	}

}
