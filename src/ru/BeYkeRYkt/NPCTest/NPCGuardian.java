package ru.BeYkeRYkt.NPCTest;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityMonster;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveIndoors;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalOpenDoor;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalRestrictOpenDoor;
import ru.BeYkeRYkt.DevNPC.api.characters.BasicCharacter;
import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;
import ru.BeYkeRYkt.DevNPC.api.path.AIManager;
import ru.BeYkeRYkt.DevNPC.api.path.tasks.SearchGoalTargetTask;
import ru.BeYkeRYkt.DevNPC.api.path.tasks.UniversalAttackOtherNPCsTask;
import ru.BeYkeRYkt.DevNPC.implementation.path.CraftAIManager;

public class NPCGuardian extends BasicCharacter {

	public NPCGuardian() {
		super("dev_guardian", "Guardian");
		setDamageAttack(2);
		setWalkSpeed(1);
		setViewDistance(20);
	}

	@Override
	public void initGoalTasks(LivingEntityNPC entity, AIManager manager) {
		CraftAIManager cmanager = (CraftAIManager) manager;
		EntityCreature handle = ((CraftCreature) entity).getHandle();

		// cmanager.addTask(2, new PathfinderGoalMeleeAttack(handle, EntityHuman.class, 1.0D, false));
		cmanager.addTask(0, new PathfinderGoalFloat(handle));
		cmanager.addTask(1, new UniversalAttackOtherNPCsTask(entity, "dev_attacker", 3, 10));
		cmanager.addTask(2, new PathfinderGoalMeleeAttack(handle, EntityMonster.class, entity.getWalkSpeed(), false));
		// cmanager.addTask(1, new MovePlayerTask(entity));
		cmanager.addTask(2, new PathfinderGoalMoveIndoors(handle));
		cmanager.addTask(3, new PathfinderGoalRestrictOpenDoor(handle));
		cmanager.addTask(4, new PathfinderGoalOpenDoor(handle, true));
		cmanager.addTask(5, new PathfinderGoalMoveTowardsRestriction(handle, 1.0D));
		cmanager.addTask(7, new PathfinderGoalRandomStroll(handle, 1.0D));
		cmanager.addTask(8, new PathfinderGoalLookAtPlayer(handle, EntityCreature.class, 8.0F));
		cmanager.addTask(8, new PathfinderGoalRandomLookaround(handle));
	}

	@Override
	public void initTargetTasks(LivingEntityNPC entity, AIManager fightAIManager) {
		CraftAIManager cmanager = (CraftAIManager) fightAIManager;
		EntityCreature handle = ((CraftCreature) entity).getHandle();
		cmanager.addTask(0, new PathfinderGoalHurtByTarget(handle, true, new Class[0]));
		// cmanager.addTask(1, new PathfinderGoalNearestAttackableTarget(handle, EntityHumanNPC.class, true));
		cmanager.addTask(1, new SearchGoalTargetTask(entity, LivingEntityNPC.class));
		// cmanager.addTask(1, new PathfinderGoalNearestAttackableTarget<EntityMonster>(handle, EntityMonster.class, true));
	}

	@Override
	public ItemStack getHelmet() {
		return new ItemStack(Material.DIAMOND_HELMET);
	}

	@Override
	public ItemStack getChestplate() {
		return new ItemStack(Material.LEATHER_CHESTPLATE);
	}

	@Override
	public ItemStack getLeggings() {
		return new ItemStack(Material.LEATHER_LEGGINGS);
	}

	@Override
	public ItemStack getBoots() {
		return new ItemStack(Material.LEATHER_BOOTS);
	}

	@Override
	public ItemStack getItemInHand() {
		// return new ItemStack(Material.GLOWSTONE);
		return new ItemStack(Material.IRON_SWORD);
	}
}
