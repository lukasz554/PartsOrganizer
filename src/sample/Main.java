package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.KeyEvent;

public class Main extends Application implements KeyListener {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("sample.fxml"));

        AnchorPane pane = loader.load();

        Scene scene = new Scene(pane);

        scene.getStylesheets().addAll(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("Parts Organizer");
        primaryStage.setScene(scene);
        primaryStage.show();


    }






    public static void main(String[] args) throws IOException {
        launch(args);

    }

    @Override
    public void keyTyped(KeyEvent ket) {

    }

    @Override
    public void keyPressed(KeyEvent kep) {

    }

    @Override
    public void keyReleased(KeyEvent ker) {
        System.out.println("Key released");

    }

}
