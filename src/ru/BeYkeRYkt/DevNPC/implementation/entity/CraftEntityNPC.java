package ru.BeYkeRYkt.DevNPC.implementation.entity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import ru.BeYkeRYkt.DevNPC.api.Animations;
import ru.BeYkeRYkt.DevNPC.api.Animations.PacketType;
import ru.BeYkeRYkt.DevNPC.api.characters.ICharacter;
import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;
import ru.BeYkeRYkt.DevNPC.api.path.AIManager;
import ru.BeYkeRYkt.DevNPC.implementation.characters.NullCharacter;
import ru.BeYkeRYkt.DevNPC.implementation.path.CraftAIManager;
import ru.BeYkeRYkt.DevNPC.implementation.utils.NMSHelper;

public abstract class CraftEntityNPC extends CraftCreature implements LivingEntityNPC {

	private boolean damageable;
	private boolean gravity;
	private boolean freezing;
	private AIManager goalManager;
	private AIManager targetManager;
	private ICharacter character;
	private double walkSpeed;

	public CraftEntityNPC(CraftServer server, EntityCreature entity) {
		super(server, entity);
		setCharacter(null);
		setGravity(true);
		setDamageable(true);
		setWalkSpeed(1);
	}

	@Override
	public boolean isLiving() {
		return true;
	}
	
	@Override
	public boolean isDamageable() {
		return damageable;
	}

	@Override
	public void setDamageable(boolean flag) {
		this.damageable = flag;
	}
	
	@Override
	public boolean isGravity() {
		return gravity;
	}

	@Override
	public void setGravity(boolean flag) {
		this.gravity = flag;
	}

	@Override
	public boolean isFreezing() {
		return freezing;
	}

	@Override
	public void setFreezing(boolean flag) {
		this.freezing = flag;
	}

	@Override
	public void moveTo(Location loc, double speed) {
		moveTo(loc.getX(), loc.getY(), loc.getZ(), speed);
	}

	@Override
	public void moveTo(double x, double y, double z, double speed) {
		getHandle().getNavigation().a(x, y, z, speed);
	}

	@Override
	public void jump() {
		getHandle().getControllerJump().a();
	}

	@Override
	public void lookAt(Location location) {
		lookAt(location.getX(), location.getY(), location.getZ());
	}

	@Override
	public void lookAt(double x, double y, double z) {
		getHandle().getControllerLook().a(x, y, z, 10.0F, getHandle().bQ());
	}

	@Override
	public void setYaw(float yaw) {
		getHandle().yaw = yaw;
	}

	@Override
	public void setPitch(float pitch) {
		getHandle().pitch = pitch;
	}

	@Override
	public float getYaw() {
		return getHandle().yaw;
	}

	@Override
	public float getPitch() {
		return getHandle().pitch;
	}

	@Override
	public AIManager getGoalAIManager() {
		if (goalManager == null) {
			PathfinderGoalSelector selector = getHandle().goalSelector;
			goalManager = new CraftAIManager(selector);
		}
		return goalManager;
	}

	@Override
	public AIManager getTargetAIManager() {
		if (targetManager == null) {
			PathfinderGoalSelector selector = getHandle().targetSelector;
			targetManager = new CraftAIManager(selector);
		}
		return targetManager;
	}

	@Override
	public float getHeight() {
		return getHandle().getHeadHeight();
	}

	@Override
	public ICharacter getCharacter() {
		return character;
	}

	@Override
	public void setCharacter(ICharacter character) {
		if (character == null) {
			// set basic
			character = new NullCharacter("null");
		}
		getGoalAIManager().clear();
		getTargetAIManager().clear();

		this.character = character;
		refreshStats();
		character.initGoalTasks(this, getGoalAIManager());
		character.initTargetTasks(this, getTargetAIManager());
	}

	@Override
	public void playAnimation(int animationId) {
		NMSHelper.playAnimation(getHandle(), PacketType.ENTITY_STATUS, animationId, 10);
	}

	@Override
	public void playAnimation(Animations id) {
		NMSHelper.playAnimation(getHandle(), id);
	}

	@Override
	public void playAnimation(Animations anim, int particalCount) {
		NMSHelper.playAnimation(getHandle(), anim, particalCount);
	}

	@Override
	public void refreshStats() {
		setWalkSpeed(getCharacter().getWalkSpeed());
		setViewDistance(getCharacter().getViewDistance());
		setDamageAttack(getCharacter().getDamageAttack());
	}

	@Override
	public double getWalkSpeed() {
		return walkSpeed;
	}

	@Override
	public void setWalkSpeed(double speed) {
		this.walkSpeed = speed;
	}

	@Override
	public double getViewDistance() {
		return getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE).getValue();
	}

	@Override
	public void setViewDistance(double view) {
		getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(view);
	}

	@Override
	public double getDamageAttack() {
		return getHandle().getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).getValue();
	}

	@Override
	public void setDamageAttack(double damage) {
		getHandle().getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(damage);
	}

	@Override
	public String toString() {
		return "CraftEntityNPC[type=" + getType().getName() + "; " + getCharacter().toString() + "]";
	}

	@Override
	public void attackEntity(LivingEntity entity) {
		CraftLivingEntity ce = (CraftLivingEntity) entity;
		getHandle().r(ce.getHandle());
	}

	@Override
	public void stopPathfinding() {
		getHandle().getNavigation().n();
	}

	@Override
	public void applyCharacterEquipment() {
		getEquipment().setHelmet(getCharacter().getHelmet());
		getEquipment().setChestplate(getCharacter().getChestplate());
		getEquipment().setLeggings(getCharacter().getLeggings());
		getEquipment().setBoots(getCharacter().getBoots());
		getEquipment().setItemInHand(getCharacter().getItemInHand());
	}
	
	public abstract void readExtraData(NBTTagCompound nbttagcompound);

	public abstract void setExtraData(NBTTagCompound nbttagcompound);
}
