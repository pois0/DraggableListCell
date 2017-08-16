package net.slash_omega.draggable_lc.example

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * Created by poispois on 2017/08/16.
 */
class ExampleApplication: Application() {
    override fun start(primaryStage: Stage?) {
        val loader = FXMLLoader(javaClass.getResource("./fxml/ExampleMain.fxml"))
        var scene = Scene(loader.load())
        var controller: ExampleMainController = loader.getController()
        controller.update()
        primaryStage!!.scene = scene
        primaryStage.show()
    }
}