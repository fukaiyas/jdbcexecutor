package com.bugworm.jdbcexecutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.util.Properties;

public class JdbcExecutor extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(
                JdbcExecutor.class.getResourceAsStream("/jdbcexecutor.fxml"));
        primaryStage.setScene(new Scene(root));

        primaryStage.setOnCloseRequest(e -> DbConnection.close());
        primaryStage.show();
    }

    public static void main(String... args){
        Application.launch(JdbcExecutor.class, args);
    }
}
