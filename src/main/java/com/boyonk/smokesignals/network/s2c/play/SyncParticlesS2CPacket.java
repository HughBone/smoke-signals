package com.boyonk.smokesignals.network.s2c.play;

import com.boyonk.smokesignals.SmokeSignals;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public record SyncParticlesS2CPacket(Map<Block, ParticleOptions> map) implements CustomPacketPayload {

	public static final Type<SyncParticlesS2CPacket> ID = new Type<>(Identifier.fromNamespaceAndPath(SmokeSignals.NAMESPACE, "sync_particles"));
	private static final StreamCodec<RegistryFriendlyByteBuf, Map<Block, ParticleOptions>> MAP_PACKET_CODEC = ByteBufCodecs.map(Object2ObjectArrayMap::new, ByteBufCodecs.registry(Registries.BLOCK), ParticleTypes.STREAM_CODEC);
	public static final StreamCodec<RegistryFriendlyByteBuf, SyncParticlesS2CPacket> CODEC = MAP_PACKET_CODEC.map(SyncParticlesS2CPacket::new, SyncParticlesS2CPacket::map);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
