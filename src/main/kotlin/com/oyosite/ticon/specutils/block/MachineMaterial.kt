package com.oyosite.ticon.specutils.block

import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Property
import net.minecraft.util.StringIdentifiable

enum class MachineMaterial : StringIdentifiable{
    CALCITE, BASALT, BLACKSLAG, DEEPSLATE;


    private val nameString = this.name.lowercase()
    override fun asString(): String = nameString

    companion object{
        val PROPERTY: Property<MachineMaterial> = EnumProperty.of("machine_material", MachineMaterial::class.java)
    }
}