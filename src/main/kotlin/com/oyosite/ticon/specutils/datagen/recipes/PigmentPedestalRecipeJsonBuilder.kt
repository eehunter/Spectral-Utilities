package com.oyosite.ticon.specutils.datagen.recipes

import com.google.gson.JsonArray
import com.google.gson.JsonObject
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

open class PigmentPedestalRecipeJsonBuilder(result: ItemConvertible, count: Int = 1) {

    val json = JsonObject()

    var id = Registries.ITEM.getId(result.asItem())
    var advancementBuilder: Advancement.Builder = Advancement.Builder.createUntelemetered()
    var advancementId: Identifier? = null

    init {
        val resultJson = JsonObject()
        resultJson.addProperty("item", Registries.ITEM.getId(result.asItem()).toString())
        resultJson.addProperty("count", count)
        json.add("result", resultJson)
    }

    fun time(time: Int) = apply { json.addProperty("time", time) }
    fun tier(tier: String) = apply { json.addProperty("tier", tier) }
    fun powders(cyan: Int = 0, magenta: Int = 0, yellow: Int = 0, black: Int = 0, white: Int = 0) = apply {
        json.addProperty("cyan", cyan)
        json.addProperty("magenta", magenta)
        json.addProperty("yellow", yellow)
        json.addProperty("black", black)
        json.addProperty("white", white)
    }

    fun experience(xp: Double) = apply { json.addProperty("experience", xp) }
    fun pattern(row1: String, row2: String? = null, row3: String? = null) = apply { json.add("pattern", JsonArray().apply{add(row1);row2?.let(::add);row3?.let(::add)}) }
    fun ingredient(key: Char, value: Ingredient) = apply {
        val keys = json.getAsJsonObject("key")?:JsonObject().also{json.add("key", it)}
        keys.add(key.toString(), value.toJson())
    }

    //values entries should not be null, but are nullable for convenience.
    fun ingredient(key: Char, vararg values: ItemConvertible?) = ingredient(key, Ingredient.ofItems(*values))

    fun ingredient(key: Char, tag: TagKey<Item>) = ingredient(key, Ingredient.fromTag(tag))

    fun id(id: Identifier) = apply{this.id = id}
    fun id(transform: Identifier.()->Identifier) = apply {id = id.transform()}

    fun criterion(criterion: String, conditions: CriterionConditions) = apply { advancementBuilder.criterion(criterion, conditions) }

    fun offerTo(exporter: Consumer<RecipeJsonProvider>) = exporter.accept(PigmentPedestalRecipeJsonProvider(this))

    class PigmentPedestalRecipeJsonProvider(val builder: PigmentPedestalRecipeJsonBuilder): RecipeJsonProvider{
        override fun serialize(json: JsonObject) {
            builder.json.entrySet().forEach { json.add(it.key, it.value) }
            json.addProperty("required_advancement", advancementId.toString())
        }


        override fun getRecipeId(): Identifier = builder.id

        override fun getSerializer(): RecipeSerializer<*> = SpectrumRecipeSerializers.SHAPED_PEDESTAL_RECIPE_SERIALIZER

        override fun toAdvancementJson(): JsonObject = builder.advancementBuilder.toJson()

        override fun getAdvancementId(): Identifier = builder.advancementId?:builder.id.withPrefixedPath("recipes/pedestal/")

    }


}