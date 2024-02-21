package com.oyosite.ticon.specutils.datagen

import com.oyosite.ticon.specutils.SpectralUtilities
import com.oyosite.ticon.specutils.block.BlockRegistry
import com.oyosite.ticon.specutils.block.moonstone_grow_lamp.MoonstoneGrowLampBlock
import com.oyosite.ticon.specutils.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.*
import java.util.*

class ModelGen(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        SpectralUtilities.info("Generating Block Models...")
        //blockStateModelGenerator.registerSimpleCubeAll(BlockRegistry.CALCITE_AUXILIARY_INK_SUPPLIER)
        blockStateModelGenerator.registerSimpleState(BlockRegistry.CALCITE_AUXILIARY_INK_SUPPLIER)
        blockStateModelGenerator.registerSimpleState(BlockRegistry.BASALT_AUXILIARY_INK_SUPPLIER)

        blockStateModelGenerator.registerMoonstoneGrowLampBlock()



    }

    fun BlockStateModelGenerator.registerMoonstoneGrowLampBlock(){
        val root = SpectralUtilities.id("block/moonstone_grow_lamp/")
        val off = root.withSuffixedPath("off")
        val on = root.withSuffixedPath("on")
        val overcharged = root.withSuffixedPath("overcharged")
        Models.CUBE_ALL.upload(off, TextureMap.all(off), this.modelCollector)
        Models.CUBE_ALL.upload(on, TextureMap.all(on), this.modelCollector)
        Models.CUBE_ALL.upload(overcharged, TextureMap.all(overcharged), this.modelCollector)

        blockStateCollector.accept(moonstoneGrowLampBlockState)
    }

    val moonstoneGrowLampBlockState: BlockStateSupplier = MoonstoneGrowLampBlock.run{

        //val off = BlockStateVariant.create().put(MoonstoneGrowLampBlock.BRIGHTNESS, 0)

        VariantsBlockStateSupplier.create(BlockRegistry.MOONSTONE_GROW_LAMP).coordinate(
            BlockStateVariantMap.create(BRIGHTNESS, OVERCHARGE).register{brightness, overcharge ->
                BlockStateVariant.create().put(VariantSettings.MODEL, SpectralUtilities.id("block/moonstone_grow_lamp/"+(
                    if(overcharge)"overcharged"
                    else if (brightness==0)"off"
                    else "on"
                )))
            }
        )
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        SpectralUtilities.info("Generating Item Models...")
        ItemRegistry.enderFlasks.forEach { itemModelGenerator.register(it, Models.GENERATED) }
        itemModelGenerator.register(ItemRegistry.BINDING_TOOL, Models.GENERATED)
        itemModelGenerator.register(BlockRegistry.MOONSTONE_GROW_LAMP.asItem(), Model(Optional.of(SpectralUtilities.id("block/moonstone_grow_lamp/off")), Optional.empty()))
    }
}