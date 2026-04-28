package com.boyonk.smokesignals.client;

import com.boyonk.smokesignals.SmokeSignals;
import com.boyonk.smokesignals.network.s2c.play.SyncParticlesS2CPacket;
import com.boyonk.smokesignals.particle.ColoredSmokeParticleEffect;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SmokeSignalsClient implements ClientModInitializer {

	private static @Nullable Map<Block, ParticleOptions> clientBlockToSmoke;

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SyncParticlesS2CPacket.ID, (payload, context) -> {
			SmokeSignals.LOGGER.info("Received smoke data from server!");
			if (clientBlockToSmoke == null) clientBlockToSmoke = SmokeSignals.blockToSmoke;
			SmokeSignals.blockToSmoke = payload.map();
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			if (clientBlockToSmoke == null) return;
			SmokeSignals.blockToSmoke = clientBlockToSmoke;
			clientBlockToSmoke = null;
		});

		ParticleFactoryRegistry.getInstance().register(SmokeSignals.COLORED_CAMPFIRE_SMOKE, Factory::new);
	}

	public static class Factory implements ParticleProvider<ColoredSmokeParticleEffect> {

		private final SpriteSet spriteProvider;

		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}


		@Nullable
		@Override
		public Particle createParticle(ColoredSmokeParticleEffect parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
			CampfireSmokeParticle particle = new CampfireSmokeParticle(world, x, y, z, velocityX, velocityY, velocityZ, true, this.spriteProvider.get(random));
			particle.setAlpha(0.95f);
			particle.setLifetime(random.nextInt(50) + parameters.maxAge());
			particle.setColor(parameters.color().x(), parameters.color().y(), parameters.color().z());
			return particle;
		}
	}
}