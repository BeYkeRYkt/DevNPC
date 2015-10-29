package ru.BeYkeRYkt.DevNPC.api.path.tasks;

import org.bukkit.entity.Entity;

import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;

public class UniversalAttackOtherNPCsTask extends UniversalAttackTask {

	private String id;

	public UniversalAttackOtherNPCsTask(LivingEntityNPC npc, String id, int meleeDistance, int meleeAttackTick) {
		super(npc, LivingEntityNPC.class, meleeDistance, meleeAttackTick);
		this.id = id;
	}

	@Override
	public boolean shouldExecute() {
		Entity entity = getEntity().getTarget();
		if (entity == null) {
			return false;
		}

		if (entity.isDead()) {
			return false;
		}
		if ((this.targetClass != null) && (!(this.targetClass.isAssignableFrom(entity.getClass())))) {
			return false;
		}

		LivingEntityNPC target = (LivingEntityNPC) entity;
		if (!target.getCharacter().getId().equals(id)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean canContinue() {
		Entity entity = getEntity().getTarget();
		if (entity == null) {
			return false;
		}

		if (entity.isDead()) {
			return false;
		}

		if ((this.targetClass != null) && (!(this.targetClass.isAssignableFrom(entity.getClass())))) {
			return false;
		}

		LivingEntityNPC target = (LivingEntityNPC) entity;
		if (!target.getCharacter().getId().equals(id)) {
			return false;
		}
		return true;
	}

	@Override
	public int getMutexBits() {
		return 3;
	}

}
