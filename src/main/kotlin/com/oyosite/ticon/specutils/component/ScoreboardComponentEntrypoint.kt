package com.oyosite.ticon.specutils.component

import net.minecraft.util.Identifier
import org.ladysnake.cca.api.v3.component.ComponentRegistry

object ScoreboardComponentEntrypoint : ScoreboardComponentInitializer{

    val ENDER_FLASK = ComponentRegistry.getOrCreate(Identifier("specutils:ender_flask"), StaticEnderInkStorageComponent::class.java)

    override fun registerScoreboardComponentFactories(registry: ScoreboardComponentFactoryRegistry) {
        registry.registerScoreboardComponent(ENDER_FLASK, ::StaticEnderInkStorageComponent)
    }

}