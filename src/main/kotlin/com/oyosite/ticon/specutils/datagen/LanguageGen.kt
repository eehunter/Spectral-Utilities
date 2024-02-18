package com.oyosite.ticon.specutils.datagen

import com.oyosite.ticon.specutils.item.ItemRegistry
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import java.util.*

class LanguageGen(output: FabricDataOutput): FabricLanguageProvider(output) {
    override fun generateTranslations(translationBuilder: TranslationBuilder) {
        ItemRegistry.enderFlasks.forEach {
            translationBuilder.add(it, "${it.color.dyeColor.name.toEnglishName} Ender Flask")
            translationBuilder.add("item.specutils.${it.color.dyeColor.name.lowercase()}_ender_flask.tooltip", "§7Stores up to %d§7 ${it.color.dyeColor.name.toEnglishName} Ink")
        }
        translationBuilder.add("item.specutils.ender_flask.tooltip.owner", "Owner: %s")
        translationBuilder.add("item.specutils.ender_flask.tooltip.unlinked_0", "Owner: Nobody")
        translationBuilder.add("item.specutils.ender_flask.tooltip.unlinked_1", "Right-Click to bind.")
        translationBuilder.add("item.specutils.ender_flask.tooltip.unknown_player", "Unknown Player")
    }

    val String.toEnglishName get() = lowercase().split("_").joinToString(" ") { it.capitalized }

    val String.capitalized get() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}