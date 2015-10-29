package ru.BeYkeRYkt.NPCTest;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import ru.BeYkeRYkt.DevNPC.DevNPC;
import ru.BeYkeRYkt.DevNPC.api.Animations;
import ru.BeYkeRYkt.DevNPC.api.characters.ICharacter;
import ru.BeYkeRYkt.DevNPC.api.entity.INPC;
import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.HumanNPC;
import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;

public class NPCListener implements Listener {

	private LivingEntityNPC npc;

	@EventHandler
	public void onPlayerJoin(PlayerInteractEvent event) {
		if (event.getItem() != null) {
			if (event.getItem().getType() == Material.STICK) {
				Location location = event.getPlayer().getLocation();
				HumanNPC npc = (HumanNPC) DevNPC.getNPCManager().spawnNPC("Human", location);
				this.npc = npc;
				if (event.getAction() == Action.LEFT_CLICK_AIR) {
					ICharacter character = DevNPC.getNPCManager().getCharacterManager().getCharacter("dev_attacker");
					npc.setCharacter(character);
					npc.setCustomName(character.getDisplayName());
				} else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
					ICharacter character = DevNPC.getNPCManager().getCharacterManager().getCharacter("dev_guardian");
					npc.setCharacter(character);
					npc.setCustomName(character.getDisplayName());
				}
				npc.applyCharacterEquipment();
			} else if (event.getItem().getType() == Material.GLOWSTONE_DUST) {
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					event.setCancelled(true);
					npc.moveTo(event.getPlayer().getLocation(), npc.getWalkSpeed());
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		if (DevNPC.getNPCManager().isNPC(entity)) {
			INPC inpc = (INPC) entity;
			if (inpc.isLiving()) {
				LivingEntityNPC leNPC = (LivingEntityNPC) inpc;
				leNPC.moveTo(event.getPlayer().getLocation(), leNPC.getWalkSpeed());
				leNPC.playAnimation(Animations.HURT);
				leNPC.playAnimation(Animations.EAT_FOOD);
				leNPC.playAnimation(Animations.EXPLOSION_PARTICLE);
				leNPC.playAnimation(Animations.SWING_ARM);
				leNPC.playAnimation(Animations.CRITICAL_EFFECT);
			}
		}
	}
}
