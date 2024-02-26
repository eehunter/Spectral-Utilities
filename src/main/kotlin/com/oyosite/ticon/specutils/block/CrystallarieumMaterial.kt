package com.oyosite.ticon.specutils.block

import com.oyosite.ticon.specutils.SpectralUtilities
import com.oyosite.ticon.specutils.ink.Namespaced
import de.dafuqs.spectrum.blocks.crystallarieum.CrystallarieumGrowableBlock
import de.dafuqs.spectrum.registries.SpectrumItems
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import kotlin.reflect.KProperty1

class CrystallarieumMaterial(
    val input: Ingredient,
    val smallBud: Block,
    val largeBud: Block,
    val cluster: Block,
    val output: Identifier?,
    val pureItem: Item,
    val registerPureItem: Boolean,
    val registerBlocks: Boolean,
) {
    var recipeID: Identifier? = null

    class Builder{
        var input: Ingredient? = null
        var smallBud: Block? = null
        var largeBud: Block? = null
        var cluster: Block? = null
        var output: Identifier? = null
        var pureItem: Item? = null
        var registerPureItem: Boolean = true
        var registerBlocks: Boolean = true
        var recipeID: Identifier? = null


        fun createBuds(baseBlock: Block) = apply{
            smallBud = CrystallarieumGrowableBlock(crystallarieumGrowableBlockSettings(baseBlock), CrystallarieumGrowableBlock.GrowthStage.SMALL)
            largeBud = CrystallarieumGrowableBlock(crystallarieumGrowableBlockSettings(baseBlock), CrystallarieumGrowableBlock.GrowthStage.LARGE)
            cluster = CrystallarieumGrowableBlock(crystallarieumGrowableBlockSettings(baseBlock), CrystallarieumGrowableBlock.GrowthStage.CLUSTER)
        }

        fun input(ingredient: Ingredient) = apply{input = ingredient}
        fun input(item: Item) = input(Ingredient.ofItems(item))
        fun input(tag: TagKey<Item>) = input(Ingredient.fromTag(tag))

        fun output(id: Identifier) = apply { output = id }

        fun pureItem(item: ItemConvertible, register: Boolean = false) = apply{pureItem = item.asItem(); registerPureItem = register}

        fun createPureItem(settings: FabricItemSettings) = apply{pureItem = Item(settings)}
        fun createPureItem(settings: FabricItemSettings.()->Unit) = createPureItem(FabricItemSettings().apply(settings))




        fun build() = CrystallarieumMaterial(
            input!!,
            smallBud!!,
            largeBud!!,
            cluster!!,
            output,
            pureItem!!,
            registerPureItem,
            registerBlocks
        ).apply{recipeID = this@Builder.recipeID}
    }

    companion object{
        operator fun invoke(builder: Builder.()->Unit) = Builder().apply(builder).build()

        context(T)
        fun <T> KProperty1<T, CrystallarieumMaterial>.register(){
            val mat = get(this@T)
            val matName = name.lowercase()
            if(mat.registerBlocks)mat.run{
                listOf(
                    "small_${matName}_bud" to smallBud,
                    "large_${matName}_bud" to largeBud,
                    "${matName}_cluster" to cluster
                ).forEach {(id, block)->
                    val identifier = Identifier(if(this@T is Namespaced)this@T.namespace else SpectralUtilities.MODID, id)
                    Registry.register(Registries.BLOCK, identifier, block)
                    Registry.register(Registries.ITEM, identifier, BlockItem(block, SpectrumItems.IS.of()))
                }
            }
            if(mat.registerPureItem)mat.pureItem?.let{ pure ->
                val identifier = Identifier(if(this@T is Namespaced)this@T.namespace else SpectralUtilities.MODID, "pure_$matName")
                Registry.register(Registries.ITEM, identifier, pure)
            }

        }

        fun crystallarieumGrowableBlockSettings(baseBlock: Block): AbstractBlock.Settings {
            return FabricBlockSettings.create().mapColor(baseBlock.defaultMapColor).sounds(baseBlock.getSoundGroup(baseBlock.defaultState)).strength(1.5f)
                .solid().pistonBehavior(PistonBehavior.DESTROY).requiresTool().nonOpaque()
        }

        //context(T)
        //fun <T> register(prop: KProperty1<T, CrystallarieumMaterial>) = prop.register()
    }


}