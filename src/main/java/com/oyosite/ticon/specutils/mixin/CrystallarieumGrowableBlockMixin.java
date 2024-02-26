package com.oyosite.ticon.specutils.mixin;

import de.dafuqs.spectrum.blocks.crystallarieum.CrystallarieumGrowableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CrystallarieumGrowableBlock.class)
public abstract class CrystallarieumGrowableBlockMixin extends Block {
    @Shadow @Final public CrystallarieumGrowableBlock.GrowthStage growthStage;

    public CrystallarieumGrowableBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        switch (growthStage){
            case SMALL -> {return 1;}
            case LARGE -> {return 8;}
            case CLUSTER -> {return 15;}
        }
        return 0;
    }
}
