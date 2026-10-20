package com.example.myduitgw.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.myduitgw.ui.material.FinanceColors
import com.example.myduitgw.ui.material.Theme

@Composable
fun DualLineChart(
    incomePoints: List<Float>,
    expensePoints: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        fun pathFor(points: List<Float>): Path {
            val path = Path()
            if (points.isEmpty()) return path
            val step = if (points.size == 1) size.width else size.width / (points.size - 1)
            points.forEachIndexed { index, value ->
                val x = step * index
                val y = size.height * (1f - value.coerceIn(0.05f, 0.95f))
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            return path
        }
        val stroke = Stroke(width = 5f, cap = StrokeCap.Round)
        drawPath(path = pathFor(incomePoints), color = FinanceColors.Income, style = stroke)
        drawPath(path = pathFor(expensePoints), color = Theme, style = stroke)
    }
}

@Composable
fun SimpleBarChart(
    values: List<Float>,
    modifier: Modifier = Modifier,
    color: Color = Theme
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (values.isEmpty()) return@Canvas
        val gap = size.width * 0.06f
        val barWidth = (size.width - gap * (values.size + 1)) / values.size
        values.forEachIndexed { index, value ->
            val barHeight = size.height * value.coerceIn(0.08f, 1f)
            val left = gap + index * (barWidth + gap)
            drawRoundRect(
                color = color.copy(alpha = 0.85f),
                topLeft = Offset(left, size.height - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
    }
}

@Composable
fun CategoryBars(
    items: List<Pair<String, Float>>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (items.isEmpty()) return@Canvas
        val rowHeight = size.height / items.size
        items.forEachIndexed { index, item ->
            val top = index * rowHeight + rowHeight * 0.28f
            val barHeight = rowHeight * 0.42f
            drawRoundRect(
                color = Theme.copy(alpha = 0.12f),
                topLeft = Offset(0f, top),
                size = Size(size.width, barHeight),
                cornerRadius = CornerRadius(10f, 10f)
            )
            drawRoundRect(
                color = Theme,
                topLeft = Offset(0f, top),
                size = Size(size.width * item.second.coerceIn(0.05f, 1f), barHeight),
                cornerRadius = CornerRadius(10f, 10f)
            )
        }
    }
}
