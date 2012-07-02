package us.aaronweiss.weisscraft;

import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main Bukkit plugin for WeissCraft.
 * @author Aaron Weiss
 * @version 1.0
 */
public class WeissCraft extends JavaPlugin {
	public final Logger logger = Logger.getLogger("Minecraft");
 
	@Override
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(new BlockEventListener(), this);
		this.logger.info("[" + this.getDescription().getFullName() + "] Block Control enabled.");
	}
	
	@Override
	public void onDisable(){
		this.logger.info("[" + this.getDescription().getFullName() + "] Block Control disabled.");
	}
}
