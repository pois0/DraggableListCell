package net.slash_omega.draggable_lc.example

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.input.DataFormat

/**
 * Created by poispois on 2017/08/16.
 */
class ExampleMainController {
    @FXML lateinit var main_listview: ListView<String>
    fun update() {
        val strs: ObservableList<String> = FXCollections.observableArrayList("1111", "2222", "3333")
        val df = DataFormat("net.slash_omega.draggable_lc:" + main_listview.hashCode())
        main_listview.items = strs
        main_listview.setCellFactory { ExampleListCell(main_listview, df) }
    }
}