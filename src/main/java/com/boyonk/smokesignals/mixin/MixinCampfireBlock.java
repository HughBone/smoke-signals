package com.boyonk.smokesignals.mixin;

import com.boyonk.smokesignals.SmokeSignals;
import com.boyonk.smokesignals.particle.ColoredSmokeParticleEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class MixinCampfireBlock {


	@Inject(method = "spawnSmokeParticle", at = @At(value = "HEAD"), cancellable = true)
	private static void smokesignals$spawnSmokeParticle(Level world, BlockPos pos, boolean isSignal, boolean lotsOfSmoke, CallbackInfo ci) {
		BlockState below = world.getBlockState(pos.relative(Direction.DOWN));
		ParticleOptions particle = SmokeSignals.getSmoke(below);
		if (particle != null) {
			RandomSource random = world.getRandom();
			world.addAlwaysVisibleParticle(particle, true, (double) pos.getX() + 0.5 + random.nextDouble() / 3.0 * (double) (random.nextBoolean() ? 1 : -1), (double) pos.getY() + random.nextDouble() + random.nextDouble(), (double) pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (double) (random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
			if (lotsOfSmoke) {
				world.addParticle(ParticleTypes.SMOKE, (double) pos.getX() + 0.5 + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1), (double) pos.getY() + 0.4, (double) pos.getZ() + 0.5 + random.nextDouble() / 4.0 * (double) (random.nextBoolean() ? 1 : -1), 0.0, 0.005, 0.0);
			}
			ci.cancel();
		}
	}

	@Inject(method = "isSignalFireBaseBlock", at = @At("HEAD"), cancellable = true)
	void smokesignals$isSignalFireBaseBlock(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		ParticleOptions particle = SmokeSignals.getSmoke(state);
		cir.setReturnValue(particle != null && ((particle instanceof ColoredSmokeParticleEffect coloredSmoke && coloredSmoke.maxAge() >= 280) || particle == ParticleTypes.CAMPFIRE_SIGNAL_SMOKE));
	}
}
