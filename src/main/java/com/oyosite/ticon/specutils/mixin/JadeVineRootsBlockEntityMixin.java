package com.oyosite.ticon.specutils.mixin;

import de.dafuqs.spectrum.blocks.jade_vines.JadeVineRootsBlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JadeVineRootsBlockEntity.class)
public class JadeVineRootsBlockEntityMixin {

    /*@Redirect(method = "isLaterNight(Lnet/minecraft/world/World;)Z", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/World;getTimeOfDay()J"))
    long modifyTime(World world){
        return world.getTime();
    }*/
}
