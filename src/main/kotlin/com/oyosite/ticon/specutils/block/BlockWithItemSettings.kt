package com.oyosite.ticon.specutils.block

import net.minecraft.item.Item


interface BlockWithItemSettings {

    val itemSettings: Item.Settings?
}