package mod.azure.darkwaters.mixin;

import java.util.SplittableRandom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mod.azure.darkwaters.util.DarkWatersSounds;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Category;

@SuppressWarnings("deprecation")
@Mixin(PlayerEntity.class)
public abstract class StormMixin extends LivingEntity {

	protected int cooldown = 0;
	protected BiomeSelectionContext biome;

	protected StormMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void stormMixin(CallbackInfo ci) {
		SplittableRandom random = new SplittableRandom();
		int r = random.nextInt(0, 4);
		if (world.isThundering()
				&& world.getBiomeAccess().getBiome(getBlockPos()).getCategory().equals(Category.OCEAN)) {
			cooldown++;
			this.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 600, 1));
			if (this.cooldown == 5) {
				if (!this.isSilent()) {
					this.world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(),
							r == 1 ? DarkWatersSounds.STORM_ABIENT1
									: r == 2 ? DarkWatersSounds.STORM_ABIENT2 : DarkWatersSounds.STORM_ABIENT3,
							SoundCategory.MUSIC, 0.75F, 1.0F);
				}
				this.cooldown = -500;
			}
		}
	}
}
