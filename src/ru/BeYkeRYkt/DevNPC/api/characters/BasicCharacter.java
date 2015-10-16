package ru.BeYkeRYkt.DevNPC.api.characters;

import org.bukkit.Sound;

import ru.BeYkeRYkt.DevNPC.api.utils.SoundReplacer;

public abstract class BasicCharacter implements ICharacter {

	private String id;
	private String displayName;
	private double walkSpeed;
	private String damageSound;
	private String deathSound;
	private String saySound;
	private double viewDistance;
	private double damageAttack;

	public BasicCharacter(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
		this.walkSpeed = 0.1F;
		this.damageSound = "";
		this.deathSound = "";
		this.saySound = "";
		setViewDistance(16);
		setDamageAttack(1);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getDisplayName() {
		return displayName;
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
	public String getDamageSound() {
		return damageSound;
	}

	@Override
	public void setDamageSound(String name) {
		this.damageSound = name;
	}

	@Override
	public void setDamageSound(Sound sound) {
		this.damageSound = SoundReplacer.getSound(sound);
	}

	@Override
	public String getDeathSound() {
		return deathSound;
	}

	@Override
	public void setDeathSound(String name) {
		this.deathSound = name;
	}

	@Override
	public void setDeathSound(Sound sound) {
		this.deathSound = SoundReplacer.getSound(sound);
	}

	@Override
	public String getSaySound() {
		return saySound;
	}

	@Override
	public void setSaySound(String name) {
		this.saySound = name;
	}

	@Override
	public void setSaySound(Sound sound) {
		this.saySound = SoundReplacer.getSound(sound);
	}

	@Override
	public double getViewDistance() {
		return viewDistance;
	}

	@Override
	public void setViewDistance(double viewDistance) {
		this.viewDistance = viewDistance;
	}

	@Override
	public double getDamageAttack() {
		return damageAttack;
	}

	@Override
	public void setDamageAttack(double damage) {
		this.damageAttack = damage;
	}

	@Override
	public String toString() {
		return "NPCCharacter [id=" + id + "]";
	}

}
