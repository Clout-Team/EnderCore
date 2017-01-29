package uk.endercraft.endercore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class StringUtils {

	public static boolean equalsIgnoreCase(String str1, String... str2) {
		for (String s : str2)
			if (s.equalsIgnoreCase(str1))
				return true;
		return false;
	}

	public static Location getLocFromString(String code) {
		String[] a = code.split(";");
		World w = Bukkit.getWorld(a[0]);
		double x = Double.parseDouble(a[1]);
		double y = Double.parseDouble(a[2]);
		double z = Double.parseDouble(a[3]);
		float yaw = Float.parseFloat(a[4]);
		float pitch = Float.parseFloat(a[5]);
		return new Location(w, x, y, z, yaw, pitch);
	}

	public static String getStringFromLoc(Location loc) {
		return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw()
				+ ";" + loc.getPitch();
	}

}
