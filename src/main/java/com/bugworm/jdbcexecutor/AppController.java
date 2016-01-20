package com.bugworm.jdbcexecutor;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class AppController implements Initializable{

    private int currentTabNumber = 1;

    public TabPane tabPane;
    public TextField url;
    public TextField user;
    public TextField pass;

    @Override
    public void initialize(URL location, ResourceBundle resources){

        url.textProperty().addListener(e -> change());
        user.textProperty().addListener(e -> change());
        pass.textProperty().addListener(e -> change());

        Properties prop = new Properties();
        try{
            prop.load(new FileInputStream("db.properties"));
            url.setText(prop.getProperty("url"));
            user.setText(prop.getProperty("user"));
            pass.setText(prop.getProperty("pass"));
        }catch(Exception e){
            e.printStackTrace();
            //無い場合は放置
        }

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        tabPane.getTabs().set(0, AppController.createTab("Query#1"));
    }

    public void addNewTab() throws IOException{
        currentTabNumber++;
        tabPane.getTabs().add(createTab("Query#" + currentTabNumber));
    }

    public void change() {
        DbConnection.close();//本当はDISCONNECTボタンとかの方がいいか
        DbConnection.url = url.getText();
        DbConnection.user = user.getText();
        DbConnection.pass = pass.getText();
    }

    public static Tab createTab(String name){
        FXMLLoader loader = new FXMLLoader();
        try{
            Node node = loader.load(
                    AppController.class.getResourceAsStream("/tab.fxml"));
            return new Tab(name, node);
        }catch(IOException e){
            throw new RuntimeException("Fail to load tab.fxml.", e);
        }
    }
}
