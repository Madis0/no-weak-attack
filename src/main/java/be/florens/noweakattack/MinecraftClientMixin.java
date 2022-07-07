package be.florens.noweakattack;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow public ClientPlayerEntity player;
	@Shadow public HitResult crosshairTarget;
	@Shadow public ClientPlayerInteractionManager interactionManager;

	@Inject(method = "doAttack", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V"))
	private void cancelAttack(CallbackInfoReturnable<Boolean> info) {
		if (!this.hasFinishedCooldown()) {
			info.setReturnValue(false);
		}
	}

	@Unique
	private boolean hasFinishedCooldown() {
		return this.player.getAttackCooldownProgress(0f) >= 1f;
	}
}
