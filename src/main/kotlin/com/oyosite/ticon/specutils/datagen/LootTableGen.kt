package com.oyosite.ticon.specutils.datagen

import com.oyosite.ticon.specutils.block.BlockRegistry
import com.oyosite.ticon.specutils.block.CrystallarieumMaterial
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.minecraft.block.Block
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemConvertible
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.MatchToolLootCondition
import net.minecraft.loot.entry.AlternativeEntry
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.entry.LootPoolEntry
import net.minecraft.loot.function.LootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.LootNumberProvider
import net.minecraft.loot.provider.number.LootNumberProviderTypes
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.item.EnchantmentPredicate
import net.minecraft.predicate.item.ItemPredicate

class LootTableGen(output: FabricDataOutput) : FabricBlockLootTableProvider(output) {
    override fun generate() {
        addDrop(BlockRegistry.MOONSTONE_GROW_LAMP)
        addDrop(BlockRegistry.BASALT_AUXILIARY_INK_SUPPLIER)
        addDrop(BlockRegistry.CALCITE_AUXILIARY_INK_SUPPLIER)

        addDrop(BlockRegistry.DRAGONBONE)



    }

    fun addDrop(mat: CrystallarieumMaterial) = mat.run{
        listOf(smallBud, largeBud, cluster).forEach{ addBudDrop(it, if(it == cluster) pureItem else null) }
    }

    fun addBudDrop(bud: Block, clusterDrop: ItemConvertible?){
        addDrop(bud,
            if(clusterDrop != null)lootTable(
                pool().with(AlternativeEntry.builder(
                    item(bud).toolCondition(enchantedItem(Enchantments.SILK_TOUCH)),
                    item(clusterDrop).apply(countRange(3,5))
                ))
            )
            else lootTable(
                pool().with(item(bud).toolCondition(enchantedItem(Enchantments.SILK_TOUCH)))
            )
        )
    }

    fun countRange(min: Int, max: Int) = SetCountLootFunction.builder(UniformLootNumberProvider.create(min.toFloat(), max.toFloat()))

    fun LootPoolEntry.Builder<*>.toolCondition(itemPredicate: ItemPredicate.Builder) = conditionally(MatchToolLootCondition.builder(itemPredicate))

    fun pool() = LootPool.builder()
    fun item(item: ItemConvertible) = ItemEntry.builder(item)
    fun atLeast(i: Int) = NumberRange.IntRange.atLeast(i)
    fun between(a: Int, b: Int) = NumberRange.IntRange.between(a,b)
    fun exactly(i: Int) = NumberRange.IntRange.exactly(i)
    fun enchantedItem(enchantment: Enchantment, minLevel: Int = 1, maxLevel: Int? = null) = ItemPredicate.Builder.create().enchantment(enchantment(enchantment, minLevel, maxLevel))

    fun enchantment(enchantment: Enchantment, minLevel: Int = 1, maxLevel: Int? = null) = EnchantmentPredicate(enchantment, if(maxLevel==null)atLeast(minLevel) else if(maxLevel<=minLevel)exactly(minLevel) else between(minLevel, maxLevel))



    fun lootTable(pool: LootPool.Builder) = LootTable.builder().pool(pool)




}