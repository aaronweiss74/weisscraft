package us.aaronweiss.weisscraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * WeissCraft Listener to cause mutations when smelting diamonds.
 * @author Aaron Weiss
 * @version 1.4
 */
public class MutantModListener implements Listener {
	private final ArrayList<PotionEffect> mutations = new ArrayList<PotionEffect>();
	private JavaPlugin plugin;
	private Material source;
	private Material blindnessSource;
	private ItemStack yield;
	private ItemStack blindnessYield;
	private FurnaceRecipe blindnessRecipe;
	private FurnaceRecipe mutantRecipe;
	
	public MutantModListener(JavaPlugin plugin) {
		this.plugin = plugin;
		yield = new ItemStack(Material.COAL);
		source = Material.DIAMOND;
		mutantRecipe = new FurnaceRecipe(yield, source);
		blindnessYield = new ItemStack(Material.GHAST_TEAR);
		blindnessSource = Material.GHAST_TEAR;
		blindnessRecipe = new FurnaceRecipe(blindnessYield, blindnessSource);
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
		mutations.add(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
		for (int i = 0; i < 50; i++) {
			Collections.shuffle(mutations);
		}
	}
	
	public FurnaceRecipe getBlindnessRecipe() {
		return blindnessRecipe;
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
					PotionEffect mutation = mutations.get(0);
					if (mutation.getType().equals(PotionEffectType.SPEED) && le.hasPotionEffect(PotionEffectType.SLOW)) {
						le.removePotionEffect(PotionEffectType.SLOW);
					} else if (mutation.getType().equals(PotionEffectType.SLOW) && le.hasPotionEffect(PotionEffectType.SPEED)) {
						le.removePotionEffect(PotionEffectType.SPEED);
					}
					le.addPotionEffect(mutation);
				}
			}
			for (int i = 0; i < 50; i++) {
				Collections.shuffle(mutations);
			}
		} else if (e.getSource().getType().equals(Material.GHAST_TEAR)) {
			Location loc = e.getBlock().getLocation();
			for (LivingEntity le : e.getBlock().getWorld().getLivingEntities()) {
				Location lec = le.getLocation();
				if (lec.distance(loc) < 8 && Math.abs(lec.subtract(loc).getY()) < 2) {
					for (PotionEffect effect : le.getActivePotionEffects()) {
						le.removePotionEffect(effect.getType());
					}
					le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 6000, 4));
				}
			}
		}
	}
	
	@EventHandler
	public void fixFallDamage(EntityDamageEvent e) {
		LivingEntity le;
		try {
			le = (LivingEntity) e.getEntity();
		} catch (ClassCastException ex) {
			return;
		}
		if (le.hasPotionEffect(PotionEffectType.JUMP)) {
			if (e.getCause().equals(DamageCause.FALL)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void regenerationHungerFix(FoodLevelChangeEvent e) {
		Collection<PotionEffect> effects = e.getEntity().getActivePotionEffects();
		for (PotionEffect pe : effects) {
			if (pe.getType().equals(PotionEffectType.REGENERATION) && pe.getAmplifier() >= 4) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void teleportationMutation(PlayerInteractEvent e) {
		Collection<PotionEffect> effects = e.getPlayer().getActivePotionEffects();
		for (PotionEffect pe : effects) {
			if (e.getPlayer().getItemInHand().getType().equals(Material.AIR) && pe.getType().equals(PotionEffectType.SLOW) && pe.getAmplifier() >= 4) {
				Location adjust = new Location(e.getPlayer().getWorld(), 0, 2, 0);
				PlayerTeleportEvent te = null;
				switch (e.getAction()) {
					default:
						break;
					case RIGHT_CLICK_BLOCK:
						te = new PlayerTeleportEvent(e.getPlayer(), e.getPlayer().getLocation(), e.getClickedBlock().getLocation().add(adjust), TeleportCause.PLUGIN);
					case RIGHT_CLICK_AIR:
						te = new PlayerTeleportEvent(e.getPlayer(), e.getPlayer().getLocation(), e.getClickedBlock().getLocation(), TeleportCause.PLUGIN);
				}
				if (te != null) {
					plugin.getServer().getPluginManager().callEvent(te);
				}
			}
		}
	}
}
