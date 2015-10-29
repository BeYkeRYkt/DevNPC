package ru.BeYkeRYkt.DevNPC.api.entity;

import ru.BeYkeRYkt.DevNPC.api.Animations;

public interface INPC {

	public boolean isLiving();

	public boolean isDamageable();

	public void setDamageable(boolean flag);

	public boolean isGravity();

	public void setGravity(boolean flag);

	public boolean isFreezing();

	public void setFreezing(boolean flag);

	// public void moveTo(Location loc, double d);

	// public void moveTo(double x, double y, double z, double speed);

	// public void stopPathfinding();

	// public void jump();

	// public void lookAt(Location location);

	// public void lookAt(double x, double y, double z);

	public void setYaw(float yaw);

	public void setPitch(float pitch);

	public float getYaw();

	public float getPitch();

	// public AIManager getGoalAIManager();

	// public AIManager getTargetAIManager();

	public float getHeight();

	// public ICharacter getCharacter();

	// public void setCharacter(ICharacter character);

	public void playAnimation(int animationId);

	public void playAnimation(Animations id);

	public void playAnimation(Animations anim, int particalCount);

	// public void refreshStats();

	// public double getWalkSpeed();

	// public void setWalkSpeed(double speed);

	// public double getViewDistance();

	// public void setViewDistance(double view);

	// public double getDamageAttack();

	// public void setDamageAttack(double damage);

	// public void attackEntity(LivingEntity entity);

	// public void applyCharacterEquipment();
}
