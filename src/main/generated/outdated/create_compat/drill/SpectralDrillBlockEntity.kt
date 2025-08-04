package outdated.create_compat.drill

import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos

class SpectralDrillBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : BlockBreakingKineticBlockEntity(type, pos, state) {




    override fun getBreakingPos(): BlockPos {
        TODO("Not yet implemented")
    }
}