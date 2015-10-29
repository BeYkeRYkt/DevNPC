package ru.BeYkeRYkt.DevNPC.api.entity.bukkit;

public interface HumanNPC extends LivingEntityNPC {

	public boolean isSneaking();

	public void setSneaking(boolean flag);

	public boolean isSprinting();

	public void setSprinting(boolean flag);

	public boolean canRegenerationHealth();

	public void setRegenerationHealth(boolean flag);

	public void playSwingArm();

	public void playEatAnimation(int particleCount);
}
