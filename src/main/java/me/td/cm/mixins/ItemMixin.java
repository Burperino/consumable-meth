package me.td.cm.mixins;

import me.td.cm.ConsumableMeth;
import me.td.cm.types.MethType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow public abstract Item asItem();

    @Inject(method = "isFood", at = @At("HEAD"), cancellable = true)
    public void onIsFood(CallbackInfoReturnable<Boolean> cir) {
        Item thisItem = asItem();
        String itemID = Registries.ITEM.getId(thisItem).toString();

        if(itemID.equals("createbb:blue_meth") || itemID.equals("createbb:white_meth")) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method="getFoodComponent", at = @At("HEAD"), cancellable = true)
    public void onGetFoodComponent(CallbackInfoReturnable<FoodComponent> cir) {
        Item thisItem = asItem();
        String itemID = Registries.ITEM.getId(thisItem).toString();

        if(itemID.equals("createbb:blue_meth")) {
            FoodComponent component = new FoodComponent.Builder()
                    .alwaysEdible()
                    .hunger(8)
                    .saturationModifier(0.4f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 0), 1.f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 1), 1.f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1800, 0), 1.f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 1), 1.f)
                    .build();

            cir.setReturnValue(component);
        }

        if(itemID.equals("createbb:white_meth")) {
            FoodComponent component = new FoodComponent.Builder()
                    .alwaysEdible()
                    .hunger(5)
                    .saturationModifier(0.2f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0), 1.f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0), 1.f)
                    .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 1), 1.f)
                    .build();

            cir.setReturnValue(component);
        }
    }

    @Inject(method = "getEatSound", at = @At("HEAD"), cancellable = true)
    public void onGetEatSound(CallbackInfoReturnable<SoundEvent> cir) {
        Item thisItem = asItem();
        String itemID = Registries.ITEM.getId(thisItem).toString();

        if(itemID.equals("createbb:blue_meth") || itemID.equals("createbb:white_meth")) {

            cir.setReturnValue(ConsumableMeth.SNIFFING_METH);

        }
    }

    @Inject(method="finishUsing", at = @At("HEAD"))
    public void onFinishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        Item thisItem = stack.getItem();
        String itemID = Registries.ITEM.getId(thisItem).toString();

        if (itemID.equals("createbb:blue_meth")) {
            ConsumableMeth.executor.schedule(() -> {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 2));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 400, 0));
            }, 90, TimeUnit.SECONDS);
        }

        if (itemID.equals("createbb:white_meth")) {
            ConsumableMeth.executor.schedule(() -> {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 400, 1));
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 400, 0));
            }, 45, TimeUnit.SECONDS);
        }
    }
}
