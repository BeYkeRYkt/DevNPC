package ru.BeYkeRYkt.DevNPC.implementation.characters;

import org.bukkit.inventory.ItemStack;

import ru.BeYkeRYkt.DevNPC.api.characters.BasicCharacter;
import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;
import ru.BeYkeRYkt.DevNPC.api.path.AIManager;

public class NullCharacter extends BasicCharacter {

	public NullCharacter(String name) {
		super("dev_null", name);
	}

	@Override
	public void initGoalTasks(LivingEntityNPC entity, AIManager manager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTargetTasks(LivingEntityNPC entity, AIManager fightAIManager) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack getHelmet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getChestplate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getLeggings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getBoots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getItemInHand() {
		// TODO Auto-generated method stub
		return null;
	}

}
