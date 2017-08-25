package net.slash_omega.draggable_lc

import javafx.collections.ObservableList
import javafx.geometry.Orientation
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.input.*

/**
 * @author poispois
 */
open class DraggableListCell<T>(lv: ListView<T>, val dataFormat: DataFormat) : ListCell<T>() {
    var items: ObservableList<T> = lv.items
    var orientation: Orientation = lv.orientation

    var overHandler: (Double, Double, Int) -> Boolean = { cursorPos, cellLength, direction ->
        val center = cellLength / 2
        (direction < 0 && cursorPos < center) || (direction > 0 && cursorPos > center)
    }

    init {
        updateListView(lv)
        setOnDragDetected {
            onDragDetected(it)
            it.consume()
        }
        setOnDragOver {
            onDragOver(it)
            it.consume()
        }
    }


    protected fun onDragDetected(e: MouseEvent) {
        if (item == null) return
        var dragBoard = startDragAndDrop(TransferMode.MOVE)
        var content = ClipboardContent()
        content.put(dataFormat, item)
        dragBoard.setContent(content)
    }

    protected fun onDragOver(e: DragEvent) {
        if (item == null) return
        if (e.dragboard.hasContent(dataFormat)) {
            e.acceptTransferModes(TransferMode.MOVE)
            changeOrder(e)
        }
    }

    protected fun changeOrder(e: DragEvent): Boolean {
        val dragBoard = e.dragboard
        if (dragBoard.hasContent(dataFormat)) {
            val source = dragBoard.getContent(dataFormat) as T
            val sourceIndex = items.indexOf(source)
            val targetIndex = items.indexOf(item)
            val (cursorPos, cellLength) = if (orientation == Orientation.VERTICAL) Pair(e.y, height) else Pair(e.x, width)
            if(overHandler(cursorPos, cellLength, targetIndex - sourceIndex)) {
                items.remove(source)
                items.add(targetIndex, source)
                return true
            }
        }
        return false
    }
}