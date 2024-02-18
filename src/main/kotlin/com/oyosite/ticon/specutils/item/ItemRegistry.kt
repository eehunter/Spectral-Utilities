package com.oyosite.ticon.specutils.item

import com.oyosite.ticon.specutils.SpectralUtilities
import de.dafuqs.spectrum.energy.color.InkColor
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.DyeColor

object ItemRegistry {

    val enderFlasks = Array(16){EnderFlask(FabricItemSettings().maxCount(1), InkColor.of(DyeColor.byId(it)))}

    operator fun invoke(){
        enderFlasks.forEachIndexed { i, it -> "${DyeColor.byId(i).name.lowercase()}_ender_flask" % it }
    }

    inline operator fun <reified I: Item> String.rem(item: I): I = Registry.register(Registries.ITEM, SpectralUtilities.id(this), item)

}