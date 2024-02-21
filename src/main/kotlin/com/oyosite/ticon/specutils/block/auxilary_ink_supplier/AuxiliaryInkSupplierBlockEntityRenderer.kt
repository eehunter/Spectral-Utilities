package com.oyosite.ticon.specutils.block.auxilary_ink_supplier

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.RotationAxis
import kotlin.math.sin

class AuxiliaryInkSupplierBlockEntityRenderer: BlockEntityRenderer<AuxiliaryInkSupplierBlockEntity> {
    override fun render(
        entity: AuxiliaryInkSupplierBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val client = MinecraftClient.getInstance()
        val inkStorageStack = entity.getStack(0)
        if (!inkStorageStack.isEmpty) {
            matrices.push()
            val time = (entity.world!!.time % 50000L).toFloat() + tickDelta
            val height = 1.0 + sin(time.toDouble() / 8.0) / 6.0
            matrices.translate(0.5, 0.5 + height, 0.5)
            matrices.multiply(client.blockEntityRenderDispatcher.camera.rotation)
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f))
            MinecraftClient.getInstance().itemRenderer.renderItem(
                inkStorageStack,
                ModelTransformationMode.GROUND,
                light,
                overlay,
                matrices,
                vertexConsumers,
                entity.world,
                0
            )
            matrices.pop()
        }
    }
}