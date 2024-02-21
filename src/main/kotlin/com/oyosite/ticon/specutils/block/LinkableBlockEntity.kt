package com.oyosite.ticon.specutils.block

import net.minecraft.util.math.BlockPos

interface LinkableBlockEntity {
    var targetPos: BlockPos?
    fun canBind(target: BlockPos): Boolean = true
}