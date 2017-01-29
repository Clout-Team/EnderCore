package uk.endercraft.endercore.language.langs;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import uk.endercraft.endercore.language.Language;

public class ptPT extends Language {

	public ptPT() {
		setName("pt_PT");
		ItemStack a = new ItemStack(Material.BANNER);
		BannerMeta banner = (BannerMeta) a.getItemMeta();
		banner.setBaseColor(DyeColor.RED);
		banner.addPattern(new Pattern(DyeColor.GREEN, PatternType.HALF_HORIZONTAL_MIRROR));
		banner.addPattern(new Pattern(DyeColor.YELLOW, PatternType.CIRCLE_MIDDLE));
		banner.setDisplayName(ChatColor.GREEN + "Portuguese (Portugal)");
		a.setItemMeta(banner);
		setMeta(banner);
		setStack(a);
		loadMessages();
	}

}
