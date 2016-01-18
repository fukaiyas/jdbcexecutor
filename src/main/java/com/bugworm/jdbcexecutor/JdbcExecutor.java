package com.bugworm.jdbcexecutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        AppController controller = loader.getController();
        controller.tabPane.getTabs().set(0, AppController.createTab("Query#1"));

        Properties prop = new Properties();
        try{
            prop.load(new FileInputStream("db.properties"));
            controller.url.setText(prop.getProperty("url"));
            controller.user.setText(prop.getProperty("user"));
            controller.pass.setText(prop.getProperty("pass"));
        }catch(Exception e){
            //無い場合は放置
        }
        primaryStage.setOnCloseRequest(e -> DbConnection.close());
        primaryStage.show();
    }

    public static void main(String... args){
        Application.launch(JdbcExecutor.class, args);
    }
}
