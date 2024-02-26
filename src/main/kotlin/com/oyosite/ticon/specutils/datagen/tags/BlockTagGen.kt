package com.oyosite.ticon.specutils.datagen.tags

import com.oyosite.ticon.specutils.block.BlockRegistry
import com.oyosite.ticon.specutils.block.auxilary_ink_supplier.AuxiliaryInkSupplierBlockEntity
import com.oyosite.ticon.specutils.block.moonstone_grow_lamp.MoonstoneGrowLampBlock
import de.dafuqs.spectrum.registries.SpectrumBlockTags
import de.dafuqs.spectrum.registries.SpectrumBlocks
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

class BlockTagGen(output: FabricDataOutput, registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>) : FabricTagProvider.BlockTagProvider(output, registriesFuture), SpectralUtilitiesTagGenerator<Block> {

    val CRYSTALLARIEUM_GROWABLE_BUDS = TagKey.of(RegistryKeys.BLOCK, Identifier("spectrum:crystallarieum_growable_buds"))
    val CRYSTALLARIEUM_GROWABLE_CLUSTERS = TagKey.of(RegistryKeys.BLOCK, Identifier("spectrum:crystallarieum_growable_clusters"))

    override fun configure(arg: RegistryWrapper.WrapperLookup) {
        AuxiliaryInkSupplierBlockEntity.INK_PROVIDERS{
            +SpectrumBlocks.COLOR_PICKER
        }
        AuxiliaryInkSupplierBlockEntity.INK_RECEIVERS{
            +SpectrumBlocks.CRYSTALLARIEUM
            +SpectrumBlocks.CINDERHEARTH
        }

        MoonstoneGrowLampBlock.OVERCHARGE_BLOCKS{
            +SpectrumBlocks.SHIMMERSTONE_BLOCK
        }

        SpectrumBlockTags.CRYSTAL_APOTHECARY_HARVESTABLE{
            +BlockRegistry.DRAGONBONE.cluster
        }

        CRYSTALLARIEUM_GROWABLE_BUDS{
            +BlockRegistry.DRAGONBONE.largeBud
            +BlockRegistry.DRAGONBONE.smallBud
        }

        CRYSTALLARIEUM_GROWABLE_CLUSTERS{
            +BlockRegistry.DRAGONBONE.cluster
        }


    }


    override fun getTagBuilderForExternalUse(tag: TagKey<Block>): FabricTagBuilder = getOrCreateTagBuilder(tag)



}