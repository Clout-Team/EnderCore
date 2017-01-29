package uk.endercraft.endercore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.endercraft.endercore.EnderPlayer;
import uk.endercraft.endercore.managers.PlayerManager;

public class TellCMD implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (!(s instanceof Player)) {
			s.sendMessage("Only players!");
			return true;
		}
		Player p = (Player) s;
		EnderPlayer ep = PlayerManager.getData(p);
		if (args.length < 2) {
			ep.sendMessage("command.tell.noargs", label);
			return true;
		}
		Player t = Bukkit.getPlayer(args[0]);
		if (t == null || !t.isOnline()) {
			ep.sendMessage("command.tell.playernotfound", args[0]);
			return true;
		}
		String message = "";
		for (int i = 1; i < args.length; i++)
			message += args[i] + " ";
		message = message.substring(0, message.length() - 1);
		EnderPlayer tep = PlayerManager.getData(t);
		ep.sendMessage("command.tell.success.sender",
				tep.getRank().getColoredDisplayName() + " " + ChatColor.RESET + t.getName(), message);
		tep.sendMessage("command.tell.success.receiver",
				ep.getRank().getColoredDisplayName() + " " + ChatColor.RESET + p.getName(), message);
		return true;
	}

}
