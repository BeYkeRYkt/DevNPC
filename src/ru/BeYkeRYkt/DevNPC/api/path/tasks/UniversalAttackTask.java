package ru.BeYkeRYkt.DevNPC.api.path.tasks;

import org.bukkit.Material;
import org.bukkit.entity.Entity;

import ru.BeYkeRYkt.DevNPC.api.entity.bukkit.LivingEntityNPC;
import ru.BeYkeRYkt.DevNPC.api.path.AIGoal;

public class UniversalAttackTask implements AIGoal {

	private LivingEntityNPC npc;
	protected Class<? extends Entity> targetClass;
	protected int meleeAttackDistance;
	protected int attackTicks;
	private int i;

	public UniversalAttackTask(LivingEntityNPC npc, Class<? extends Entity> target, int meleeDistance, int meleeAttackTick) {
		this.npc = npc;
		this.targetClass = target;
		this.meleeAttackDistance = meleeDistance;
		this.attackTicks = meleeAttackTick;
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
		return true;
	}

	@Override
	public void updateGoal() {
		if (getEntity().getTarget() != null) {
			if (i < (attackTicks * 3)) {
				i++;
			}
			getEntity().lookAt(getEntity().getTarget().getLocation().clone().add(0, getEntity().getTarget().getEyeHeight(), 0));
			if (getEntity().getLocation().distance(getEntity().getTarget().getLocation()) > meleeAttackDistance) {
				if (getEntity().getEquipment().getItemInHand().getType() == Material.BOW) {
					if (i == (attackTicks * 3)) {
						getEntity().shootArrow();
						getEntity().moveTo(getEntity().getTarget().getLocation(), getEntity().getWalkSpeed() * 0.6);
						i = 0;
					}
				} else {
					getEntity().moveTo(getEntity().getTarget().getLocation(), getEntity().getWalkSpeed());
				}
			} else if (getEntity().getLocation().distance(getEntity().getTarget().getLocation()) <= meleeAttackDistance) {
				if (i >= attackTicks) {
					getEntity().attackEntity(getEntity().getTarget());
					getEntity().moveTo(getEntity().getTarget().getLocation(), getEntity().getWalkSpeed());
					i = 0;
				}
			}
		}
	}

	@Override
	public void startExecuting() {
		// getEntity().moveTo(getEntity().getTarget().getLocation(), getEntity().getWalkSpeed());
		getEntity().lookAt(getEntity().getTarget().getLocation().clone().add(0, getEntity().getTarget().getEyeHeight(), 0));
	}

	@Override
	public void resetGoal() {
		getEntity().setTarget(null);
		i = 0;
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

		return true;
	}

	@Override
	public LivingEntityNPC getEntity() {
		return npc;
	}

	@Override
	public int getMutexBits() {
		return 3;
	}

}
