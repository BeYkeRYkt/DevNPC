package ru.BeYkeRYkt.DevNPC.implementation.path;

import net.minecraft.server.v1_8_R3.PathfinderGoal;
import ru.BeYkeRYkt.DevNPC.api.path.AIGoal;

public class PathfinderGoalCustomAITask extends PathfinderGoal {

	private AIGoal task;

	public PathfinderGoalCustomAITask(AIGoal task) {
		this.task = task;
		a(task.getMutexBits());
	}

	@Override
	public boolean a() {
		return task.shouldExecute();
	}

	@Override
	public boolean b() {
		return task.canContinue();
	}

	@Override
	public void c() {
		task.startExecuting();
	}

	@Override
	public void d() {
		task.resetGoal();
	}

	@Override
	public void e() {
		task.updateGoal();
	}
}
