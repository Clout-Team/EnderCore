package uk.endercraft.endercore.language.langs;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import uk.endercraft.endercore.language.Language;

public class enUK extends Language {

	public enUK() {
		setName("en_GB");
		ItemStack a = new ItemStack(Material.BANNER);
		BannerMeta banner = (BannerMeta) a.getItemMeta();
		banner.setBaseColor(DyeColor.BLUE);
		banner.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
		banner.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
		banner.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
		banner.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
		banner.addPattern(new Pattern(DyeColor.RED, PatternType.CROSS));
		banner.addPattern(new Pattern(DyeColor.RED, PatternType.STRAIGHT_CROSS));
		banner.setDisplayName(ChatColor.GREEN + "English");
		a.setItemMeta(banner);
		setMeta(banner);
		setStack(a);
		loadMessages();
	}

}
