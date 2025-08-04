package com.oyosite.ticon.specutils.datagen.recipes

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.oyosite.ticon.specutils.block.CrystallarieumMaterial
import de.dafuqs.spectrum.api.energy.color.InkColor
import de.dafuqs.spectrum.registries.SpectrumRecipeSerializers
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.function.Consumer

open class CrystallarieumRecipeJsonBuilder(val material: CrystallarieumMaterial, val secondPerGrowthStage: Int, inkColor: InkColor, inkCostTier: Int, catalystRequired: Boolean = true) {
    val json = JsonObject()
    val catalysts = JsonArray()
    val outputs = JsonArray()

    var id = material.recipeID?:Registries.ITEM.getId(material.pureItem.asItem())

    var advancementBuilder: Advancement.Builder = Advancement.Builder.createUntelemetered()
    var advancementId: Identifier? = null

    var requiresAdvancement = false



    init{
        val growthStages = JsonArray()
        listOf(material.smallBud, material.largeBud, material.cluster).map { Registries.BLOCK.getId(it).toString() }.forEach(growthStages::add)
        json.add("growth_stage_states", growthStages)
        outputs.add(Registries.ITEM.getId(material.pureItem).toString())
        json.addProperty("grows_without_catalyst", !catalystRequired)

        json.add("ingredient", material.input.toJson())

        json.addProperty("ink_color", inkColor.toString())
        json.addProperty("ink_cost_tier", inkCostTier)

        json.addProperty("seconds_per_growth_stage", secondPerGrowthStage)
    }



    fun catalyst(ingredient: Ingredient, accelerationMod: Double, inkMod: Double, consumeChancePerSecond: Double) = apply{
        val j = JsonObject()
        j.add("ingredient", ingredient.toJson())
        j.addProperty("growth_acceleration_mod", accelerationMod)
        j.addProperty("ink_consumption_mod", inkMod)
        j.addProperty("consume_chance_per_second", consumeChancePerSecond)
        catalysts.add(j)
    }


    fun catalyst(item: ItemConvertible, accelerationMod: Double, inkMod: Double, consumeChancePerSecond: Double) = catalyst(Ingredient.ofItems(item), accelerationMod, inkMod, consumeChancePerSecond)
    fun catalyst(tag: TagKey<Item>, accelerationMod: Double, inkMod: Double, consumeChancePerSecond: Double) = catalyst(Ingredient.fromTag(tag), accelerationMod, inkMod, consumeChancePerSecond)


    fun id(id: Identifier) = apply{this.id = id}
    fun id(transform: Identifier.()->Identifier) = apply {id = id.transform()}

    fun criterion(criterion: String, conditions: CriterionConditions) = apply { advancementBuilder.criterion(criterion, conditions) }

    fun requiresAdvancement(req: Boolean = true) = apply { requiresAdvancement = req }

    fun offerTo(exporter: Consumer<RecipeJsonProvider>){
        json.add("catalysts", catalysts)
        json.add("additional_recipe_manager_outputs", outputs)

        exporter.accept(CrystallarieumRecipeJsonProvider(this))
    }

    class CrystallarieumRecipeJsonProvider(val builder: CrystallarieumRecipeJsonBuilder): RecipeJsonProvider {
        override fun serialize(json: JsonObject) {
            builder.json.entrySet().forEach { json.add(it.key, it.value) }
            if(builder.requiresAdvancement)json.addProperty("required_advancement", advancementId.toString())
        }

        override fun getRecipeId(): Identifier = builder.id

        override fun getSerializer(): RecipeSerializer<*> = SpectrumRecipeSerializers.CRYSTALLARIEUM_RECIPE_SERIALIZER

        override fun toAdvancementJson(): JsonObject = builder.advancementBuilder.toJson()

        override fun getAdvancementId(): Identifier = builder.advancementId?:builder.id.withPrefixedPath("recipes/crystallarieum/")

    }
}