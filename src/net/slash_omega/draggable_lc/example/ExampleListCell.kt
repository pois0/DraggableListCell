package net.slash_omega.draggable_lc.example

import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ListView
import javafx.scene.input.DataFormat
import net.slash_omega.draggable_lc.DraggableListCell

/**
 * Created by poispois on 2017/08/16.
 */
class ExampleListCell(lv: ListView<String>, dataFormat: DataFormat): DraggableListCell<String>(lv, dataFormat) {
    companion object {
        val dataFormat: DataFormat = DataFormat("net.slash_omega.draggable_lc.ExampleListCell:")
    }
    private var controller: ExampleListCellController? = null
    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty || item == null) {
            graphic = null
            text = null
            controller = null
            return
        }
        if (controller == null || graphic == null) {
            val loader = FXMLLoader(javaClass.getResource("./fxml/exampleListCell.fxml"))
            var node: Node = loader.load()
            controller = loader.getController()
            graphic = node
        }
        controller!!.update(item)
    }
}