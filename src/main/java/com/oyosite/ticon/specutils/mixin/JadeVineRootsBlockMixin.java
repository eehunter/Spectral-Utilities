package com.oyosite.ticon.specutils.mixin;

import com.oyosite.ticon.specutils.block.moonstone_grow_lamp.MoonstoneGrowLampBlock;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVineRootsBlock;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVineRootsBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(JadeVineRootsBlock.class)
public class JadeVineRootsBlockMixin {

    @Inject(at=@At("RETURN"),method = "canGrow(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    void modifyCanGrow(World world, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir, BlockEntity be){
        if(cir.getReturnValue() || !(be instanceof JadeVineRootsBlockEntity jv))return;
        if(!jv.isLaterNight(world))return;

        /*BlockPos pos = blockPos.up();
        BlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof MoonstoneGrowLampBlock)
            cir.setReturnValue(state.get(MoonstoneGrowLampBlock.Companion.getBRIGHTNESS()) > 7);
        */

        for(int i = 0; i < 8; i++){
            BlockPos pos = blockPos.up(1+i);
            BlockState state = world.getBlockState(pos);
            if(state.getBlock() instanceof MoonstoneGrowLampBlock) {
                cir.setReturnValue(state.get(MoonstoneGrowLampBlock.Companion.getBRIGHTNESS()) > 7 + i);
                return;
            }
            if(state.isOpaque())break;
        }
    }
}
