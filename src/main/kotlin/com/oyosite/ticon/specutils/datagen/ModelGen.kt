package com.oyosite.ticon.specutils.datagen

import com.oyosite.ticon.specutils.SpectralUtilities
import com.oyosite.ticon.specutils.block.BlockRegistry
import com.oyosite.ticon.specutils.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models

class ModelGen(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        SpectralUtilities.info("Generating Block Models...")
        //blockStateModelGenerator.registerSimpleCubeAll(BlockRegistry.CALCITE_AUXILIARY_INK_SUPPLIER)
        blockStateModelGenerator.registerSimpleState(BlockRegistry.CALCITE_AUXILIARY_INK_SUPPLIER)
        blockStateModelGenerator.registerSimpleState(BlockRegistry.BASALT_AUXILIARY_INK_SUPPLIER)
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        SpectralUtilities.info("Generating Item Models...")
        ItemRegistry.enderFlasks.forEach { itemModelGenerator.register(it, Models.GENERATED) }
        itemModelGenerator.register(ItemRegistry.BINDING_TOOL, Models.GENERATED)
    }
}