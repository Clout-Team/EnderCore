package uk.endercraft.endercore.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Lists;

import uk.endercraft.endercore.EnderCore;
import uk.endercraft.endercore.Rank;
import uk.endercraft.endercore.managers.PlayerManager;

public class TabRanksTask extends BukkitRunnable {

	public void run() {
		List<Scoreboard> alreadyUpdated = Lists.newArrayList();
		for (Player p : Bukkit.getOnlinePlayers()) {
			Scoreboard s = p.getScoreboard();
			if (alreadyUpdated.contains(s))
				continue;
			for (Rank r : EnderCore.get().getAllRanks()) {
				if (r.getId() == 1)
					continue;
				Team t = s.getTeam(r.getName() + "." + r.getId());
				if (t == null)
					t = s.registerNewTeam(r.getName() + "." + r.getId());
				for (String str : t.getEntries())
					t.removeEntry(str);
				for (Player p2 : Bukkit.getOnlinePlayers())
					if (PlayerManager.getData(p2).getRankID() == r.getId())
						t.addEntry(p2.getName());

				t.setPrefix(ChatColor.translateAlternateColorCodes('&', r.getDisplayName().replace("&7[", "").replace("&7]", "")) + " §r");
			}
			alreadyUpdated.add(s);
		}
	}

}
