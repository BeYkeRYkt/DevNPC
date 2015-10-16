package ru.BeYkeRYkt.DevNPC.api.entity.bukkit;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

import ru.BeYkeRYkt.DevNPC.api.characters.ICharacter;
import ru.BeYkeRYkt.DevNPC.api.entity.INPC;
import ru.BeYkeRYkt.DevNPC.api.path.AIManager;

public interface LivingEntityNPC extends Creature, INPC {

	public void moveTo(Location loc, double speed);

	public void moveTo(double x, double y, double z, double speed);
	
	public void stopPathfinding();

	public void jump();

	public void lookAt(Location location);

	public void lookAt(double x, double y, double z);

	public AIManager getGoalAIManager();

	public AIManager getTargetAIManager();

	public ICharacter getCharacter();

	public void setCharacter(ICharacter character);

	public void refreshStats();

	public double getWalkSpeed();

	public void setWalkSpeed(double speed);

	public double getViewDistance();

	public void setViewDistance(double view);

	public double getDamageAttack();

	public void setDamageAttack(double damage);
	
	public void attackEntity(LivingEntity entity);
	
	public void applyCharacterEquipment();
}
