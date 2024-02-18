package com.oyosite.ticon.specutils.datagen


import com.google.gson.JsonObject
import com.oyosite.ticon.specutils.datagen.recipes.PigmentPedestalRecipeJsonBuilder
import com.oyosite.ticon.specutils.item.ItemRegistry
import de.dafuqs.revelationary.advancement_criteria.AdvancementGottenCriterion
import de.dafuqs.spectrum.energy.color.InkColor
import de.dafuqs.spectrum.registries.SpectrumBlocks
import de.dafuqs.spectrum.registries.SpectrumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import java.util.function.Consumer

class RecipeGen(output: FabricDataOutput) : FabricRecipeProvider(output) {
    override fun generate(exporter: Consumer<RecipeJsonProvider>) {
        for(color in DyeColor.entries)enderFlaskRecipe(color, exporter)
    }

    fun enderFlaskRecipe(color: DyeColor, exporter: Consumer<RecipeJsonProvider>){
        val pigment = SpectrumItems::class.java.getDeclaredField("${color.name.uppercase()}_PIGMENT").get(null) as? Item
        val builder = PigmentPedestalRecipeJsonBuilder(ItemRegistry.enderFlasks[color.ordinal])
        builder.tier("complex").experience(8.0).pattern("OYO","PFP","OEO")
            .ingredient('O', Ingredient.ofItems(Items.OBSIDIAN))
            .ingredient('E', Ingredient.ofItems(SpectrumBlocks.RADIATING_ENDER))
            .ingredient('P', Ingredient.ofItems(pigment))
            .ingredient('F', Ingredient.ofItems(SpectrumItems.INK_FLASK))
            .ingredient('Y', Ingredient.ofItems(Items.ENDER_EYE))
            .powders(8, 2, 8, 16, 4)
            .time(240)
            .criterion("gotten_radiating_ender", AdvancementGottenCriterion.create(Identifier("spectrum:get_radiating_ender")))
            .criterion("obtained_ink_flask", InventoryChangedCriterion.Conditions.items(SpectrumItems.INK_FLASK))
            .criterion("can_use_moonstone_pedestal", AdvancementGottenCriterion.create(Identifier("spectrum:build_complex_pedestal_structure")))
            .criterion("has_required_pigment", InventoryChangedCriterion.Conditions.items(pigment))
            .offerTo(exporter)
    }

}