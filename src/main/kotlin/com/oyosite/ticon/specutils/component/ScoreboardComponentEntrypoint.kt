package com.oyosite.ticon.specutils.component

import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.scoreboard.ScoreboardComponentInitializer
import net.minecraft.util.Identifier

object ScoreboardComponentEntrypoint : ScoreboardComponentInitializer{

    val ENDER_FLASK = ComponentRegistry.getOrCreate(Identifier("specutils:ender_flask"), StaticEnderInkStorageComponent::class.java)

    override fun registerScoreboardComponentFactories(registry: ScoreboardComponentFactoryRegistry) {
        registry.registerScoreboardComponent(ENDER_FLASK, ::StaticEnderInkStorageComponent)
    }

}