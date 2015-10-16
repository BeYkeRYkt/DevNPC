package ru.BeYkeRYkt.DevNPC.api.characters;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;
import ru.BeYkeRYkt.DevNPC.api.path.AIManager;

public interface ICharacter {

	public String getId();

	public String getDisplayName();

	public double getWalkSpeed();

	public void setWalkSpeed(double speed);

	public String getDamageSound();

	public void setDamageSound(String name);

	public void setDamageSound(Sound sound);

	public String getDeathSound();

	public void setDeathSound(String name);

	public void setDeathSound(Sound sound);

	public String getSaySound();

	public void setSaySound(String name);

	public void setSaySound(Sound sound);

	public double getViewDistance();

	public void setViewDistance(double distance);

	public double getDamageAttack();

	public void setDamageAttack(double damage);

	public void initGoalTasks(LivingEntityNPC entity, AIManager manager);

	public void initTargetTasks(LivingEntityNPC entity, AIManager fightAIManager);
	
	public ItemStack getHelmet();
	
	public ItemStack getChestplate();
	
	public ItemStack getLeggings();
	
	public ItemStack getBoots();
	
	public ItemStack getItemInHand();
}
