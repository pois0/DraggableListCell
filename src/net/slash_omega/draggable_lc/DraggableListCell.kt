package net.slash_omega.draggable_lc

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.input.*
import javafx.util.Duration
import java.awt.MouseInfo

/**
 * @author poispois
 */
@Suppress("MemberVisibilityCanPrivate")
open class DraggableListCell<T>(lv: ListView<T>, protected val dataFormat: DataFormat) : ListCell<T>() {
    protected var items: ObservableList<T> = lv.items
    protected var orientation: Orientation = lv.orientation
    protected var isVertical = orientation == Orientation.VERTICAL
    protected var timerDuration: Double = 10.0
    protected val timer: Timeline = with(Timeline(KeyFrame(Duration.millis(timerDuration), EventHandler {
        timerHandle(it)
    }))) {
        cycleCount = Animation.INDEFINITE
        this
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
        setOnDragDone {
            onDragDone(it)
            it.consume()
        }
    }

    protected open fun onDragDetected(e: MouseEvent) {
        if (item == null) return
        val dragBoard = startDragAndDrop(TransferMode.MOVE)
        val content = ClipboardContent()
        content.put(dataFormat, item)
        dragBoard.setContent(content)
        timer.play()
    }

    protected open fun onDragOver(e: DragEvent) {
        if (item == null) return
        if (e.dragboard.hasContent(dataFormat)) {
            e.acceptTransferModes(TransferMode.MOVE)
            changeOrder(e)
        }
    }

    protected open fun onDragDone(e: DragEvent) {
        timer.stop()
    }

    protected fun changeOrder(e: DragEvent): Boolean {
        val dragBoard = e.dragboard
        if (dragBoard.hasContent(dataFormat)) {
            val source = dragBoard.getContent(dataFormat) as T
            val sourceIndex = items.indexOf(source)
            val targetIndex = items.indexOf(item)
            val cursorPos = if (orientation == Orientation.VERTICAL) e.y else e.x
            if (overHandler(cursorPos, targetIndex - sourceIndex)) {
                items.remove(source)
                items.add(targetIndex, source)
                return true
            }
        }
        return false
    }

    /**
     * カーソルがListCellのどこに達したときに順序を交換するかを定めます。overrideされることを念頭に置いています。
     * @param cursorPos カーソルの位置が与えられます。OrientationがHORIZONTALの場合はx座標を,VERTICALの場合はy座標が与えられます。
     * @param direction ListCellが向かう方向が与えられます。ListCellがindexの若い方に向かうときは負,大きな方向に向かうときは正の値が与えられます。絶対値はターゲットとソースの差でしょうが,保証されません。
     * @return 順序がかる場合はtrueを返す必要があります。
     */
    protected open fun overHandler(cursorPos: Double, direction: Int): Boolean {
        val cellLength = if (orientation == Orientation.VERTICAL) height else width
        val center = cellLength / 2
        return (direction < 0 && cursorPos < center) || (direction > 0 && cursorPos > center)
    }

    protected open fun timerHandle(e: ActionEvent) {
        val cursorPos = {
            val it = Utils.awtPointToFXPoint2D(MouseInfo.getPointerInfo().location)
            if (isVertical) it.y else it.x
        }
        val thisNodeMinPos = {
            val it = Utils.getNodeMinPosition(this)
            if(isVertical) it.y else it.x
        }
        val listViewSize = if (isVertical) listView.height else listView.width
        scroll(cursorPos(), thisNodeMinPos(), listViewSize)
    }

    protected open fun scroll(cursorPos: Double, nodeMinPos: Double, listViewSize: Double){
        val scrollBar = Utils.getScrollBar(listView)
        if (cursorPos < nodeMinPos) {
            scrollBar.decrement()
        } else if(cursorPos > nodeMinPos + listViewSize) {
            scrollBar.increment()
        }
    }
}