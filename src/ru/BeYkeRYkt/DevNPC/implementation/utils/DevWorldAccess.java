package ru.BeYkeRYkt.DevNPC.implementation.utils;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.IWorldAccess;
import ru.BeYkeRYkt.DevNPC.api.entity.ICustomPacketsEntity;

public class DevWorldAccess implements IWorldAccess {

	@Override
	// markBlockForUpdate
	public void a(BlockPosition position) {
	}

	@Override
	// markBlockForRenderUpdate
	public void b(BlockPosition position) {
	}

	@Override
	// destroyBlockPartially
	public void b(int arg0, BlockPosition arg1, int arg2) {
	}

	@Override
	// playAuxSFX
	public void a(EntityHuman arg0, int arg1, BlockPosition arg2, int arg3) {
	}

	@Override
	// markBlockRangeForRenderUpdate
	public void a(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
	}

	@Override
	// broadcastSound
	public void a(int arg0, BlockPosition arg1, int arg2) {
	}

	@Override
	// playSound
	public void a(String arg0, double arg1, double arg2, double arg3, float arg4, float arg5) {
	}

	@Override
	// playSoundToNearExcept
	public void a(EntityHuman arg0, String arg1, double arg2, double arg3, double arg4, float arg5, float arg6) {
	}

	@Override
	// spawnParticle
	public void a(int arg0, boolean arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7, int... arg8) {
	}

	@Override
	// playRecord
	public void a(String arg0, BlockPosition arg1) {
	}

	@Override
	// onEntityCreate
	public void a(Entity arg0) {
		if (ICustomPacketsEntity.class.isAssignableFrom(arg0.getClass())) {
			ICustomPacketsEntity nms = (ICustomPacketsEntity) arg0;
			nms.initEntityTracker();
		}
	}

	@Override
	// onEntityDestroy (probably)
	public void b(Entity arg0) {
	}
}
