package us.aaronweiss.weisscraft;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import us.aaronweiss.weisscraft.constants.ModConstants;

/**
 * The main Bukkit plugin for WeissCraft.
 * @author Aaron Weiss
 * @version 1.0
 */
public class WeissCraft extends JavaPlugin {
	public final Logger logger = Logger.getLogger("Minecraft");
 
	@Override
	public void onEnable(){
		if (ModConstants.WORLD_MOD_PREVENTION) {
			this.getServer().getPluginManager().registerEvents(new WorldModPreventionListener(), this);
			this.logger.info("[" + this.getDescription().getFullName() + "] World Mod Prevention enabled.");
		}
		if (ModConstants.MUTANT_MOD) {
			MutantModListener mm = new MutantModListener();
			this.getServer().addRecipe(mm.getMutantRecipe());
			this.getServer().getPluginManager().registerEvents(mm, this);
			this.logger.info("[" + this.getDescription().getFullName() + "] Mutant Mod enabled.");
		}
		this.logger.info("[" + this.getDescription().getFullName() + "] WeissCraft loaded.");
	}
	
	@Override
	public void onDisable(){
		if (ModConstants.WORLD_MOD_PREVENTION) {
			this.logger.info("[" + this.getDescription().getFullName() + "] World Mod Prevention disabled.");
		}
	}
}
