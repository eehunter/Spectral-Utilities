package com.oyosite.ticon.specutils.item

import com.oyosite.ticon.specutils.SpectralUtilities
import com.oyosite.ticon.specutils.util.ModRegistry
import de.dafuqs.spectrum.energy.color.InkColor
import de.dafuqs.spectrum.registries.SpectrumItemGroups
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.DyeColor
import kotlin.reflect.full.declaredMemberProperties

@Suppress("Unused")
object ItemRegistry: ModRegistry<Item> {

    val enderFlasks = Array(16){EnderFlask(FabricItemSettings().maxCount(1), InkColor.of(DyeColor.byId(it)))}

    val BINDING_TOOL = BindingTool{maxCount(1)}

    operator fun invoke(){
        enderFlasks.forEachIndexed { i, it -> "${DyeColor.byId(i).name.lowercase()}_ender_flask" % it }

        ItemRegistry::class.declaredMemberProperties.filter { it.get(ItemRegistry) is Item }.forEach { it.name.lowercase() % it.get(ItemRegistry) as Item }

        //ItemGroupEvents.modifyEntriesEvent(SpectrumItemGroups.ENERGY).register{}

    }

    inline operator fun <reified I: Item> String.rem(item: I): I = Registry.register(Registries.ITEM, SpectralUtilities.id(this), item)

}