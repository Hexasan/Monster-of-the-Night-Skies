package dev.cammiescorner.mob_b.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.mob_b.common.spawners.CustomPhantomSpawner;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@ModifyExpressionValue(method = "createLevels", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;"), remap = false)
	private ImmutableList<Object> noInsomniaPhantoms(ImmutableList<Object> original) {
		return ImmutableList.copyOf(Stream.concat(Stream.of(new CustomPhantomSpawner()), original.stream().filter(customSpawner -> !(customSpawner instanceof PhantomSpawner))).toArray());
	}
}
