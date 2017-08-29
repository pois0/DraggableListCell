package net.slash_omega.draggable_lc

import javafx.geometry.Orientation
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.control.ListView
import javafx.scene.control.ScrollBar
import java.awt.Point

/**
 * Created on 2017/08/29.
 */
class Utils {
    companion object {
        fun getNodeMinPosition(node: Node, position: Point2D = Point2D(node.scene.window.x,node.scene.window.y).add(node.scene.x, node.scene.y)) : Point2D {
            val position = position.add(node.layoutX, node.layoutY)
            return node.parent?.let {
                getNodeMinPosition(it,position)
            } ?: position
        }

        fun awtPointToFXPoint2D(point: Point): Point2D {
            return Point2D(point.getX(), point.getY())
        }

        fun <T> getScrollBar(lv: ListView<T>, orientation: Orientation = lv.orientation): ScrollBar {
            val nodes: Set<Node> = lv.lookupAll(".scroll-bar")
            nodes
                .filterIsInstance<ScrollBar>()
                .filter { orientation == it.orientation }
                .forEach { return it }
            throw IllegalStateException()
        }
    }
}