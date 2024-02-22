package com.oyosite.ticon.specutils.block

import com.oyosite.ticon.specutils.SpectralUtilities
import com.oyosite.ticon.specutils.block.auxilary_ink_supplier.AuxiliaryInkSupplierBlock
import com.oyosite.ticon.specutils.block.auxilary_ink_supplier.AuxiliaryInkSupplierBlockEntity
//import com.oyosite.ticon.specutils.block.create_compat.CreateCompatBlocks
import com.oyosite.ticon.specutils.block.moonstone_grow_lamp.MoonstoneGrowLampBlock
import com.oyosite.ticon.specutils.util.ModRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
//import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import kotlin.reflect.full.declaredMemberProperties


/**
 * NOTE: DO NOT DEFINE NON-BLOCK FIELDS IN THIS CLASS.
 * Doing so may cause really annoying classloading issues.
 * */
object BlockRegistry: ModRegistry<Block> {

    val CALCITE_AUXILIARY_INK_SUPPLIER = AuxiliaryInkSupplierBlock{}
    val BASALT_AUXILIARY_INK_SUPPLIER = AuxiliaryInkSupplierBlock{}

    val MOONSTONE_GROW_LAMP = MoonstoneGrowLampBlock{}


    object BlockEntities{
        val AUXILIARY_INK_SUPPLIER_TYPE: BlockEntityType<AuxiliaryInkSupplierBlockEntity> =
            Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                SpectralUtilities.id("auxiliary_ink_supplier"),
                FabricBlockEntityTypeBuilder.create(
                    ::AuxiliaryInkSupplierBlockEntity,
                    CALCITE_AUXILIARY_INK_SUPPLIER,
                    BASALT_AUXILIARY_INK_SUPPLIER
                ).build()
            )
    }





    operator fun invoke(){
        //if(FabricLoader.getInstance().isModLoaded("create")) CreateCompatBlocks()

        BlockRegistry::class.declaredMemberProperties.filter { it.get(BlockRegistry) is Block }.forEach {
            val id = SpectralUtilities.id(it.name.lowercase())
            val block = it.get(BlockRegistry) as Block
            Registry.register(Registries.BLOCK, id, block)
            Registry.register(Registries.ITEM, id, BlockItem(block, block.itemSettings))
        }
    }

    private val Block.itemSettings: FabricItemSettings get() = (this as? BlockWithItemSettings)?.itemSettings ?: FabricItemSettings()
}