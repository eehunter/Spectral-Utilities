package com.oyosite.ticon.specutils.datagen.tags

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.FabricTagBuilder
import net.minecraft.data.server.tag.TagProvider.ProvidedTagBuilder
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier


interface SpectralUtilitiesTagGenerator<T>{

    fun getTagBuilderForExternalUse(tag: TagKey<T>): FabricTagProvider<T>.FabricTagBuilder


}

context(SpectralUtilitiesTagGenerator<T>)
inline operator fun <reified T> TagKey<T>.invoke(block: FabricTagProvider<T>.FabricTagBuilder.()->Unit) = getTagBuilderForExternalUse(this).block()

context(FabricTagProvider<T>.FabricTagBuilder)
inline operator fun <reified T> T.unaryPlus(): FabricTagProvider<T>.FabricTagBuilder = add(this)
context(FabricTagProvider<T>.FabricTagBuilder)
inline operator fun <reified T> Identifier.unaryPlus(): FabricTagProvider<T>.FabricTagBuilder = add(this)
context(FabricTagProvider<T>.FabricTagBuilder)
inline operator fun <reified T> Collection<T>.unaryPlus(): FabricTagProvider<T>.FabricTagBuilder = add(*this.toTypedArray())


inline operator fun <reified T> FabricTagProvider<T>.FabricTagBuilder.plus(obj: T): FabricTagProvider<T>.FabricTagBuilder = add(obj)
inline operator fun <reified T> FabricTagProvider<T>.FabricTagBuilder.plus(id: Identifier): FabricTagProvider<T>.FabricTagBuilder = add(id)
inline operator fun <reified T> FabricTagProvider<T>.FabricTagBuilder.plus(arr: Collection<T>): FabricTagProvider<T>.FabricTagBuilder = add(*arr.toTypedArray())


