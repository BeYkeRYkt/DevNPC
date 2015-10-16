package ru.BeYkeRYkt.DevNPC.api.path.tasks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;
import ru.BeYkeRYkt.DevNPC.api.path.AIGoal;

public class MovePlayerTask implements AIGoal {

	private LivingEntityNPC npc;
	private LivingEntity target;

	public MovePlayerTask(LivingEntityNPC npc) {
		this.npc = npc;
	}

	@Override
	public boolean shouldExecute() {
		for (Entity entity : getEntity().getNearbyEntities(20, 20, 20)) {
			if (entity.getType() == EntityType.PLAYER) {
				target = (LivingEntity) entity;
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateGoal() {
		if (target != null) {
			getEntity().moveTo(target.getLocation(), getEntity().getWalkSpeed());
		}
	}

	@Override
	public void startExecuting() {
		if (target != null) {
			getEntity().moveTo(target.getLocation(), getEntity().getWalkSpeed());
		}
	}

	@Override
	public void resetGoal() {
		getEntity().stopPathfinding();
	}

	@Override
	public boolean canContinue() {
		if (target != null) {
			return !target.isDead();
		}
		return false;
	}

	@Override
	public LivingEntityNPC getEntity() {
		return npc;
	}

}
