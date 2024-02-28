package com.oyosite.ticon.specutils.datagen

import com.oyosite.ticon.specutils.SpectralUtilities
import com.oyosite.ticon.specutils.block.BlockRegistry
import com.oyosite.ticon.specutils.block.CrystallarieumMaterial
import com.oyosite.ticon.specutils.block.NoxwoodDeco
import com.oyosite.ticon.specutils.block.moonstone_grow_lamp.MoonstoneGrowLampBlock
import com.oyosite.ticon.specutils.item.ItemRegistry
import de.dafuqs.spectrum.blocks.furniture.FlexLanternBlock
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.data.client.*
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import java.util.*
import kotlin.reflect.full.declaredMemberProperties


@Suppress("MemberVisibilityCanBePrivate", "PropertyName")
class ModelGen(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
        SpectralUtilities.info("Generating Block Models...")

        blockStateModelGenerator.registerSimpleState(BlockRegistry.CALCITE_AUXILIARY_INK_SUPPLIER)
        blockStateModelGenerator.registerSimpleState(BlockRegistry.BASALT_AUXILIARY_INK_SUPPLIER)

        blockStateModelGenerator.registerMoonstoneGrowLampBlock()

        blockStateModelGenerator.registerCrystallarieumMaterial(BlockRegistry.DRAGONBONE)

        NoxwoodDeco.NoxwoodType.nonDuplicateCombinations.forEachIndexed { index, pair ->
            blockStateModelGenerator.registerNoxwoodLamp(NoxwoodDeco.LAMPS[index].second, pair)
        }

    }

    val NOXWOOD_LAMP = Model(Optional.of(Identifier("spectrum:templates/noxwood_lamp")), Optional.empty(), TextureKey.ALL)
    val NOXWOOD_LAMP_ALT = Model(Optional.of(Identifier("spectrum:templates/noxwood_lamp_alt")), Optional.empty(), TextureKey.ALL)
    val NOXWOOD_LAMP_DIAGONAL = Model(Optional.of(Identifier("spectrum:templates/noxwood_lamp_diagonal")), Optional.empty(), TextureKey.ALL)
    val NOXWOOD_LAMP_DIAGONAL_ALT = Model(Optional.of(Identifier("spectrum:templates/noxwood_lamp_diagonal_alt")), Optional.empty(), TextureKey.ALL)
    val NOXWOOD_LAMP_HANGING = Model(Optional.of(Identifier("spectrum:templates/noxwood_lamp_hanging")), Optional.empty(), TextureKey.ALL)
    val NOXWOOD_LAMP_HANGING_ALT = Model(Optional.of(Identifier("spectrum:templates/noxwood_lamp_hanging_alt")), Optional.empty(), TextureKey.ALL)
    val NOXWOOD_LAMP_HANGING_DIAGONAL = Model(Optional.of(Identifier("spectrum:templates/noxwood_lamp_hanging_diagonal")), Optional.empty(), TextureKey.ALL)
    val NOXWOOD_LAMP_HANGING_DIAGONAL_ALT = Model(Optional.of(Identifier("spectrum:templates/noxwood_lamp_hanging_diagonal_alt")), Optional.empty(), TextureKey.ALL)

    val NOXWOOD_LAMP_MODELS = listOf(
        NOXWOOD_LAMP, NOXWOOD_LAMP_ALT, NOXWOOD_LAMP_DIAGONAL, NOXWOOD_LAMP_DIAGONAL_ALT,
        NOXWOOD_LAMP_HANGING, NOXWOOD_LAMP_HANGING_ALT, NOXWOOD_LAMP_HANGING_DIAGONAL, NOXWOOD_LAMP_HANGING_DIAGONAL_ALT,
    )

    fun BlockStateModelGenerator.registerNoxwoodLamp(lamp: Block, materials: Pair<NoxwoodDeco.NoxwoodType, NoxwoodDeco.NoxwoodType>){

        blockStateCollector.accept(noxwoodLampBlockState(lamp, materials))

        ModelGen::class.declaredMemberProperties.filter { it.name.contains("NOXWOOD_LAMP") }.mapNotNull { p -> (p.get(this@ModelGen) as? Model)?.let { p.name.lowercase() to it } }.forEach { (variant, model)->
            model.upload(SpectralUtilities.id("block/noxwood_deco/${materials.first}_${materials.second}_$variant"), TextureMap.all(
                SpectralUtilities.id("block/noxwood_deco/${materials.first}_${materials.second}_noxwood_lamp")
            ), this.modelCollector)
        }

        registerParentedItemModel(lamp, SpectralUtilities.id("block/noxwood_deco/${materials.first}_${materials.second}_noxwood_lamp"))
    }

    fun noxwoodLampBlockState(lamp: Block, materials: Pair<NoxwoodDeco.NoxwoodType, NoxwoodDeco.NoxwoodType>)=
        VariantsBlockStateSupplier.create(lamp).coordinate(
            BlockStateVariantMap.create(FlexLanternBlock.ALT, FlexLanternBlock.HANGING, FlexLanternBlock.DIAGONAL).register { alt, hanging, diagonal ->
                var suffix = ""
                if(hanging)suffix+="_hanging"
                if(diagonal)suffix+="_diagonal"
                if(alt)suffix+="_alt"

                BlockStateVariant.create().put(VariantSettings.MODEL,SpectralUtilities.id("block/noxwood_deco/${materials.first}_${materials.second}_noxwood_lamp${suffix}"))
            }
        )

    val CRYSTALLARIEUM_FARMABLE = Model(Optional.of(Identifier("spectrum:block/crystallarieum_farmable")), Optional.empty(), TextureKey.CROSS)

    fun BlockStateModelGenerator.registerCrystallarieumMaterial(mat: CrystallarieumMaterial){
        listOf(mat.smallBud, mat.largeBud, mat.cluster).forEach {
            val id = Registries.BLOCK.getId(it).withPrefixedPath("block/crystallarieum/")
            blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(it, id))
            CRYSTALLARIEUM_FARMABLE.upload(id, TextureMap.cross(id), modelCollector)
            Models.GENERATED.upload(ModelIds.getItemModelId(it.asItem()), TextureMap.layer0(id), this.modelCollector)
        }
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