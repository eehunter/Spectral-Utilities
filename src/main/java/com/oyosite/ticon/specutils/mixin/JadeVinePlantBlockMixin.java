package com.oyosite.ticon.specutils.mixin;


import com.oyosite.ticon.specutils.config.CommonConfig;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVinePlantBlock;
import de.dafuqs.spectrum.registries.SpectrumItems;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JadeVinePlantBlock.class)
public class JadeVinePlantBlockMixin {

    @Inject(
            at = @At(value = "INVOKE", target = "Lde/dafuqs/spectrum/blocks/jade_vines/JadeVineRootsBlock;onNaturesStaffUse(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Z"),
            method = "onNaturesStaffUse(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Z"
    )
    void dropJadeJelly(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<Boolean> cir){
        if(!AutoConfig.getConfigHolder(CommonConfig.class).getConfig().getJadeVinesDropJadeJellyWhenRevived())return;
        Vec3d c = pos.toCenterPos();
        ItemScatterer.spawn(world, c.x, c.y, c.z, new ItemStack(SpectrumItems.JADE_JELLY, world.random.nextInt(3)+3));
    }

}
