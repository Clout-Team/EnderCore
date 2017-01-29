package uk.endercraft.endercore.language;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BannerMeta;

import com.google.common.collect.Lists;

import uk.endercraft.endercore.EnderPlayer;
import uk.endercraft.endercore.language.langs.enUK;
import uk.endercraft.endercore.language.langs.ptBR;
import uk.endercraft.endercore.language.langs.ptPT;
import uk.endercraft.endercore.managers.PlayerManager;

public class LanguageMain {

	private static List<Language> langs = Lists.newArrayList();
	private static Language mainLang;
	private static boolean settedup = false;

	public static String get(String lang, String message, Object... complements) {
		setup();

		for (Language l : langs) {
			if (lang.equalsIgnoreCase(l.getName())) {
				if (l.getMap().get(message) == null)
					return getFromMain(lang, message, complements);
				if (complements.length == 0) {
					return ChatColor.translateAlternateColorCodes('&', l.getMap().get(message));
				} else {
					String a = l.getMap().get(message);
					ChatColor b = null;
					if (a.substring(0, 1).equalsIgnoreCase("§")) {
						ChatColor.getByChar(a.substring(1, 2));
					}
					for (int i = 0; i < complements.length; i++) {
						String replacement = "%" + Integer.toString(i + 1);
						a = a.replaceAll(replacement, complements[i].toString() + (b != null ? b : ""));
					}
					return ChatColor.translateAlternateColorCodes('&', a);
				}
			}
		}

		return getFromMain(lang, message, complements);

	}

	private static String getFromMain(String lang, String message, Object... complements) {
		if (mainLang.getMap().get(message) == null)
			return "ERROR 404: Message not found: '" + message + "'! Please notify the staff!";
		if (complements.length == 0) {
			return ChatColor.translateAlternateColorCodes('&', mainLang.getMap().get(message));
		} else {
			String a = mainLang.getMap().get(message);
			ChatColor b = null;
			if (a.substring(0, 1).equalsIgnoreCase("§")) {
				ChatColor.getByChar(a.substring(1, 2));
			}
			for (int i = 0; i < complements.length; i++) {
				String replacement = "%" + Integer.toString(i + 1);
				a = a.replaceAll(replacement, complements[i].toString() + (b != null ? b : ""));
			}
			return ChatColor.translateAlternateColorCodes('&', a);
		}
	}

	public static String get(EnderPlayer ep, String message, Object... complements) {
		return get(ep.getLang(), message, complements);
	}

	public static String get(Player p, String message, Object... complements) {
		return get(PlayerManager.getData(p), message, complements);
	}

	public static String getCountry(Player p) {
		try {
			Object ep = getMethod("getHandle", p.getClass()).invoke(p, (Class<?>) null);
			Field f = ep.getClass().getDeclaredField("locale");
			f.setAccessible(true);
			return (String) f.get(ep);
		} catch (Exception localException) {
		}
		return "en_UK";
	}

	private static Method getMethod(String name, Class<?> clazz) {
		Method[] arrayOfMethod;
		int j = (arrayOfMethod = clazz.getDeclaredMethods()).length;
		for (int i = 0; i < j; i++) {
			Method m = arrayOfMethod[i];
			if (m.getName().equals(name))
				return m;
		}
		return null;
	}

	public static boolean isLangMessage(String name, String langCode) {
		for (Language selected : getAllLangs()) {
			if (selected.getMap().get(langCode).equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public static List<Language> getAllLangs() {
		return langs;
	}

	public static BannerMeta getFlag(Player p) {
		setup();
		return getFlag(PlayerManager.getData(p));
	}

	public static BannerMeta getFlag(EnderPlayer p) {
		for (Language lang : getAllLangs()) {
			if (lang.getName().equalsIgnoreCase(p.getLang())) {
				return lang.getMeta();
			}
		}
		return mainLang.getMeta();
	}

	public static void setup() {
		if (settedup)
			return;
		settedup = true;

		langs.add(setMainLanguage(new enUK()));
		langs.add(new ptPT());
		langs.add(new ptBR());
	}

	public static void resetup() {
		for (Language l : langs)
			l.loadMessages();
	}

	private static Language setMainLanguage(Language main) {
		mainLang = main;
		return mainLang;
	}

}
