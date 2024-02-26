package com.oyosite.ticon.specutils.block

//import com.oyosite.ticon.specutils.block.create_compat.CreateCompatBlocks
//import net.fabricmc.loader.api.FabricLoader
import com.oyosite.ticon.specutils.SpectralUtilities
import com.oyosite.ticon.specutils.block.CrystallarieumMaterial.Companion.register
import com.oyosite.ticon.specutils.block.auxilary_ink_supplier.AuxiliaryInkSupplierBlock
import com.oyosite.ticon.specutils.block.auxilary_ink_supplier.AuxiliaryInkSupplierBlockEntity
import com.oyosite.ticon.specutils.block.moonstone_grow_lamp.MoonstoneGrowLampBlock
import com.oyosite.ticon.specutils.util.ModRegistry
import de.dafuqs.spectrum.registries.SpectrumBlocks
import de.dafuqs.spectrum.registries.SpectrumItems
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties


/**
 * NOTE: DO NOT DEFINE NON-BLOCK FIELDS IN THIS CLASS.
 * Doing so may cause really annoying classloading issues.
 * */
object BlockRegistry: ModRegistry<Block> {

    val CALCITE_AUXILIARY_INK_SUPPLIER = AuxiliaryInkSupplierBlock{}
    val BASALT_AUXILIARY_INK_SUPPLIER = AuxiliaryInkSupplierBlock{}

    val MOONSTONE_GROW_LAMP = MoonstoneGrowLampBlock{strength(0.6f)}

    val DRAGONBONE: CrystallarieumMaterial = CrystallarieumMaterial{input(SpectrumItems.DRAGONBONE_CHUNK).createBuds(SpectrumBlocks.CRACKED_DRAGONBONE).createPureItem {  }}


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


    @Environment(EnvType.CLIENT)
    fun registerClient(){
        val dmp = BlockRegistry::class.declaredMemberProperties
        /*dmp.mapNotNull { it.get(BlockRegistry) as? CrystallarieumMaterial }.forEach {
            if(!it.registerBlocks)return@forEach
            listOf(it.smallBud, it.largeBud, it.cluster).forEach { b -> BlockRenderLayerMap.INSTANCE.putBlock(b, RenderLayer.getCutout()) }
        }*/

        NoxwoodDeco.LAMPS.forEach { BlockRenderLayerMap.INSTANCE.putBlock(it.second, RenderLayer.getCutout()) }
        //BlockRenderLayerMap.INSTANCE.putBlock(, RenderLayer.getCutout())
    }


    @Suppress("Unchecked_cast")
    operator fun invoke(){
        //if(FabricLoader.getInstance().isModLoaded("create")) CreateCompatBlocks()
        val dmp = BlockRegistry::class.declaredMemberProperties
        dmp.filter { it.get(BlockRegistry) is Block }.forEach {
            val id = SpectralUtilities.id(it.name.lowercase())
            val block = it.get(BlockRegistry) as Block
            Registry.register(Registries.BLOCK, id, block)
            Registry.register(Registries.ITEM, id, BlockItem(block, block.itemSettings))
        }

        dmp.filter { it.get(BlockRegistry) is CrystallarieumMaterial }.let{it as List<KProperty1<BlockRegistry,CrystallarieumMaterial>>}.forEach{it.register()}

        NoxwoodDeco()
    }

    private val Block.itemSettings: FabricItemSettings get() = (this as? BlockWithItemSettings)?.itemSettings ?: FabricItemSettings()
}