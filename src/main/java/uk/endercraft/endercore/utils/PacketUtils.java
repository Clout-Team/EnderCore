package uk.endercraft.endercore.utils;

import java.lang.reflect.Field;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.EnumParticle;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_10_R1.PacketPlayOutWorldParticles;

public class PacketUtils {

	public static void spawnParticle(EnumParticle particle, Location l, float xd, float yd, float zd, float speed,
			int count, int... params) {
		PacketPlayOutWorldParticles pc = new PacketPlayOutWorldParticles(particle, true, (float) l.getX(),
				(float) l.getY(), (float) l.getZ(), xd, yd, zd, speed, count, params);
		for (Player p : l.getWorld().getPlayers())
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(pc);
	}

	public static void setTabHeaderFooter(Player p, String header, String footer) {
		PacketPlayOutPlayerListHeaderFooter headerfooter = new PacketPlayOutPlayerListHeaderFooter();
		try {
			Field headerF = headerfooter.getClass().getDeclaredField("a");
			Field footerF = headerfooter.getClass().getDeclaredField("b");
			headerF.setAccessible(true);
			footerF.setAccessible(true);
			headerF.set(headerfooter, toJSON(ChatColor.translateAlternateColorCodes('&', header)));
			footerF.set(headerfooter, toJSON(ChatColor.translateAlternateColorCodes('&', footer)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(headerfooter);
	}

	private static IChatBaseComponent toJSON(String text) {
		return ChatSerializer.a("{\"text\": \"" + text + "\"}");
	}

}
