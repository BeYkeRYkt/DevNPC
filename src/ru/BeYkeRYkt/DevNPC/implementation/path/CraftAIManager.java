package ru.BeYkeRYkt.DevNPC.implementation.path;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

import net.minecraft.server.v1_8_R3.PathfinderGoal;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import ru.BeYkeRYkt.DevNPC.api.path.AIGoal;
import ru.BeYkeRYkt.DevNPC.api.path.AIManager;

public class CraftAIManager implements AIManager {

	private PathfinderGoalSelector selector;
	private Map<AIGoal, PathfinderGoal> tasks;

	public CraftAIManager(PathfinderGoalSelector selector) {
		this.tasks = new HashMap<>();
		this.selector = selector;
	}

	@Override
	public void addTask(int i, AIGoal ai) {
		PathfinderGoal goal = new PathfinderGoalCustomAITask(ai);
		addTask(i, goal);
		tasks.put(ai, goal);
	}

	public void addTask(int i, PathfinderGoal goal) {
		this.selector.a(i, goal);
	}

	@Override
	public void removeTask(AIGoal ai) {
		// TODO: JUST DO IT!
		if (tasks.containsKey(ai)) {
			PathfinderGoal goal = tasks.get(ai);
			this.selector.a(goal);
		}
	}
	
	public void removeTask(PathfinderGoal goal) {
		this.selector.a(goal);
	}

	@Override
	public void clear() {
		try {
			Field bField = selector.getClass().getDeclaredField("b");
			if (!bField.isAccessible()) {
				bField.setAccessible(true);
			}
			Field cField = selector.getClass().getDeclaredField("c");
			if (!cField.isAccessible()) {
				cField.setAccessible(true);
			}
			bField.set(selector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(selector, new UnsafeList<PathfinderGoalSelector>());
			tasks.clear();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}
