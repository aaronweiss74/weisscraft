package us.aaronweiss.weisscraft;

import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * WeissCraft Listener to cause mutations when smelting diamonds.
 * @author Aaron Weiss
 * @version 1.0
 */
public class MutantModListener implements Listener {
	private final ArrayList<PotionEffect> mutations = new ArrayList<PotionEffect>();
	private Material source;
	private ItemStack yield;
	private FurnaceRecipe mutantRecipe;
	
	public MutantModListener() {
		yield = new ItemStack(Material.COAL);
		source = Material.DIAMOND;
		mutantRecipe = new FurnaceRecipe(yield, source);
		int duration = 72000;
		int negativeDuration = 600;
		int amplifier = 4;
		mutations.add(new PotionEffect(PotionEffectType.WATER_BREATHING, duration, amplifier));
		mutations.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, duration, amplifier));
		mutations.add(new PotionEffect(PotionEffectType.BLINDNESS, negativeDuration, amplifier));
		mutations.add(new PotionEffect(PotionEffectType.POISON, negativeDuration, amplifier));
		mutations.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, duration, amplifier));
		mutations.add(new PotionEffect(PotionEffectType.REGENERATION, duration, amplifier));
		mutations.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration, amplifier));
		mutations.add(new PotionEffect(PotionEffectType.JUMP, duration, amplifier));
		mutations.add(new PotionEffect(PotionEffectType.SPEED, duration, amplifier));
		for (int i = 0; i < 50; i++) {
			Collections.shuffle(mutations);
		}
	}
	
	public FurnaceRecipe getMutantRecipe() {
		return mutantRecipe;
	}
	
	@EventHandler
	public void mutateEvent(FurnaceSmeltEvent e) {
		if (e.getSource().getType().equals(source)) {
			Location loc = e.getBlock().getLocation();
			for (LivingEntity le : e.getBlock().getWorld().getLivingEntities()) {
				Location lec = le.getLocation();
				if (lec.distance(loc) < 8 && Math.abs(lec.subtract(loc).getY()) < 2) {
					le.addPotionEffect(mutations.get(0));
				}
			}
			for (int i = 0; i < 50; i++) {
				Collections.shuffle(mutations);
			}
		}
	}
}
