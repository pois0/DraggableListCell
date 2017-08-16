package net.slash_omega.draggable_lc.example

import javafx.fxml.FXML
import javafx.scene.control.Label

/**
 * Created by poispois on 2017/08/16.
 */
class ExampleListCellController {
    @FXML lateinit var cell_label: Label
    fun update(str: String) {
        cell_label.text = str
    }
}