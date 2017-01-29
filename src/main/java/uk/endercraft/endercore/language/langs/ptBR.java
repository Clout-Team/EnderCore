package uk.endercraft.endercore.language.langs;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import uk.endercraft.endercore.language.Language;

public class ptBR extends Language {

	public ptBR() {
		setName("pt_BR");
		ItemStack a = new ItemStack(Material.BANNER);
		BannerMeta banner = (BannerMeta) a.getItemMeta();
		banner.setBaseColor(DyeColor.BLACK);
		banner.addPattern(new Pattern(DyeColor.GREEN, PatternType.GRADIENT));
		banner.addPattern(new Pattern(DyeColor.GREEN, PatternType.GRADIENT_UP));
		banner.addPattern(new Pattern(DyeColor.YELLOW,
				PatternType.RHOMBUS_MIDDLE));
		banner.addPattern(new Pattern(DyeColor.BLUE, PatternType.CIRCLE_MIDDLE));
		banner.setDisplayName(ChatColor.GREEN + "Portuguese (Brazil)");
		a.setItemMeta(banner);
		setMeta(banner);
		setStack(a);
		loadMessages();
	}

}
