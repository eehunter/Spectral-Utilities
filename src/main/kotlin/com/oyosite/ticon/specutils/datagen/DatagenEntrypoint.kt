package com.oyosite.ticon.specutils.datagen

import com.oyosite.ticon.specutils.SpectralUtilities
import com.oyosite.ticon.specutils.datagen.tags.BlockTagGen
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object DatagenEntrypoint: DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        SpectralUtilities.info("Initializing Data Generator...")
        val pack: FabricDataGenerator.Pack = fabricDataGenerator.createPack()//fabricDataGenerator.createBuiltinResourcePack(SpectralUtilities.id("resources"))
        pack.addProvider(::ModelGen)
        pack.addProvider(::RecipeGen)
        pack.addProvider(::LanguageGen)
        pack.addProvider(::BlockTagGen)
        pack.addProvider(::LootTableGen)
    }
}