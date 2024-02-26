package com.oyosite.ticon.specutils.block

import com.oyosite.ticon.specutils.SpectralUtilities
import de.dafuqs.spectrum.blocks.furniture.FlexLanternBlock
import de.dafuqs.spectrum.registries.SpectrumBlocks
import de.dafuqs.spectrum.registries.SpectrumItems.IS
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object NoxwoodDeco {
    enum class NoxwoodType{
        CHESTNUT, SLATE, EBONY, IVORY;

        companion object{
            fun forEachCombination(block: (Pair<NoxwoodType, NoxwoodType>)->Unit) { for (type1 in entries) for (type2 in entries) block(type1 to type2) }
            val combinations = mutableListOf<Pair<NoxwoodType, NoxwoodType>>().also{ forEachCombination(it::add) }
            val nonDuplicateCombinations = combinations.filter(nonDuplicate)
            fun <T> mapCombinations(transform: (Pair<NoxwoodType, NoxwoodType>)->T) = combinations.map(transform)
        }

        override fun toString(): String = name.lowercase()
    }

    private val nonDuplicate: (Pair<*,*>)->Boolean = {it.first!=it.second}


    val LAMPS = NoxwoodType.nonDuplicateCombinations.map { idFor(it, "lamp") to noxwoodLampBlock() }.toTypedArray()
    val LANTERNS = NoxwoodType.nonDuplicateCombinations.map { idFor(it, "lantern") to noxwoodLanternBlock() }.toTypedArray()
    val LIGHTS = NoxwoodType.nonDuplicateCombinations.map { idFor(it, "light") to noxwoodLightBlock() }.toTypedArray()

    val ALL_DECO = mutableListOf(*LIGHTS, *LAMPS, *LANTERNS)

    operator fun invoke() = ALL_DECO.forEach {(id, block)->
        Registry.register(Registries.BLOCK, id, block)
        Registry.register(Registries.ITEM, id, BlockItem(block, IS.of()))
    }



    fun idFor(pair: Pair<NoxwoodType, NoxwoodType>, blockName: String) = SpectralUtilities.id("${pair.first.name.lowercase()}_${pair.second.name.lowercase()}_noxwood_$blockName")

    //Why are their names swapped? I have no idea.
    fun noxwoodLanternBlock() = RedstoneLampBlock(SpectrumBlocks.noxcap(MapColor.DULL_RED).luminance(SpectrumBlocks.LANTERN_LIGHT_PROVIDER))
    fun noxwoodLampBlock() = FlexLanternBlock(FabricBlockSettings.copyOf(Blocks.LANTERN).luminance(13).pistonBehavior(PistonBehavior.DESTROY))
    fun noxwoodLightBlock() = PillarBlock(SpectrumBlocks.noxcap(MapColor.DULL_RED).luminance { 15 })
}