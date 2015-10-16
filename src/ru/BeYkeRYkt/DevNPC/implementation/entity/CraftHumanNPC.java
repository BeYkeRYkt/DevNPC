package ru.BeYkeRYkt.DevNPC.implementation.entity;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import ru.BeYkeRYkt.DevNPC.DevNPC;
import ru.BeYkeRYkt.DevNPC.api.Animations;
import ru.BeYkeRYkt.DevNPC.api.characters.ICharacter;
import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.HumanNPC;
import ru.BeYkeRYkt.DevNPC.implementation.entity.nms.EntityHumanNPC;

public class CraftHumanNPC extends CraftEntityNPC implements HumanNPC {

	private boolean regeneration;
	private double oldSpeed;

	public CraftHumanNPC(CraftServer server, EntityHumanNPC entity) {
		super(server, entity);
		setMaxHealth(20);
		setHealth(20);
	}

	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}

	@Override
	public boolean isSneaking() {
		return getHandle().isSneaking();
	}

	@Override
	public void setSneaking(boolean flag) {
		if (flag && !isSneaking()) {
			oldSpeed = getWalkSpeed();
			setWalkSpeed(getWalkSpeed() * 0.3D);
		} else if (!flag && isSneaking()) {
			setWalkSpeed(oldSpeed);
		}
		getHandle().setSneaking(flag);
	}

	@Override
	public boolean isSprinting() {
		return getHandle().isSneaking();
	}

	@Override
	public void setSprinting(boolean flag) {
		getHandle().setSprinting(flag);
	}

	@Override
	public boolean canRegenerationHealth() {
		return regeneration;
	}

	@Override
	public void setRegenerationHealth(boolean flag) {
		this.regeneration = flag;
	}

	@Override
	public void playSwingArm() {
		playAnimation(Animations.SWING_ARM);
	}

	@Override
	public void playEatAnimation(int particleCount) {
		playAnimation(Animations.EAT_FOOD);
	}

	@Override
	public void readExtraData(NBTTagCompound nbttagcompound) {
		try {
			String id = nbttagcompound.getString("characterId");
			ICharacter character = DevNPC.getNPCManager().getCharacterManager().getCharacter(id);
			setCharacter(character);
			setWalkSpeed(nbttagcompound.getDouble("walkSpeed"));
			setViewDistance(nbttagcompound.getDouble("viewDistance"));
			setDamageAttack(nbttagcompound.getDouble("damageAttack"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setExtraData(NBTTagCompound nbttagcompound) {
		try {
			nbttagcompound.setString("characterId", getCharacter().getId());
			nbttagcompound.setDouble("walkSpeed", getWalkSpeed());
			nbttagcompound.setDouble("viewDistance", getViewDistance());
			nbttagcompound.setDouble("damageAttack", getDamageAttack());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
