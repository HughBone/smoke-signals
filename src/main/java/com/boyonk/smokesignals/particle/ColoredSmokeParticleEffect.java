package com.boyonk.smokesignals.particle;

import com.boyonk.smokesignals.SmokeSignals;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.DyeColor;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record ColoredSmokeParticleEffect(Vector3fc color, int maxAge) implements ParticleOptions {

	public ColoredSmokeParticleEffect(DyeColor color, int maxAge) {
		this(unpackRgb(color.getTextureDiffuseColor()), maxAge);
	}


	public static final MapCodec<ColoredSmokeParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
					ExtraCodecs.VECTOR3F
							.fieldOf("color")
							.forGetter(ColoredSmokeParticleEffect::color),
					Codec.INT
							.fieldOf("max_age")
							.forGetter(ColoredSmokeParticleEffect::maxAge)
			).apply(instance, ColoredSmokeParticleEffect::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, ColoredSmokeParticleEffect> PACKET_CODEC = StreamCodec.composite(
			ByteBufCodecs.VECTOR3F, ColoredSmokeParticleEffect::color,
			ByteBufCodecs.INT, ColoredSmokeParticleEffect::maxAge,
			ColoredSmokeParticleEffect::new
	);

	@Override
	public ParticleType<?> getType() {
		return SmokeSignals.COLORED_CAMPFIRE_SMOKE;
	}

	private static Vector3fc unpackRgb(int rgb) {
		return new Vector3f(((rgb >> 16) & 0xFF) / 255.0f, ((rgb >> 8) & 0xFF) / 255.0f, (rgb & 0xFF) / 255.0f);
	}
}
