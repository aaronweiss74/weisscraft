package us.aaronweiss.weisscraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

/**
 * WeissCraft Listener to make compasses smarter by pointing to your bed.
 * @author Aaron Weiss
 * @version 1.0
 */
public class SmartCompassListener implements Listener {
	@EventHandler
	public void smartCompass(PlayerBedLeaveEvent e) {
		e.getPlayer().setCompassTarget(e.getBed().getLocation());
	}
}
