package com.bugworm.jdbcexecutor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class TabController implements Initializable{

    public TextArea query;
    public TextArea result;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        result.textProperty().addListener((obs, oldVal, newVal) -> result.setScrollTop(Double.MAX_VALUE));
    }

    public void execute(){
        try{
            for(String singleQuery : query.getText().split(";")){
                String lowerCase = singleQuery.trim().toLowerCase();

                if(lowerCase.startsWith("select")){
                    executeQuery(singleQuery);

                }else if(lowerCase.startsWith("update") ||
                        lowerCase.startsWith("insert") ||
                        lowerCase.startsWith("delete")){
                    execute(singleQuery);

                }else if(!lowerCase.trim().equals("")){
                    throw new RuntimeException("Execute only select, update, insert and delete.");
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            addResult(e.getMessage());
            addResult("");
            try{
                DbConnection.rollback();
                addResult("Rollback.");
            }catch(SQLException sqlEx){
                sqlEx.printStackTrace();
                addResult(sqlEx.getMessage());
                addResult("");
                addResult("Fail to rollback.");
            }
            addResult("  ---------------------------------------------------------------");
        }
    }

    public void executeQuery(String singleQuery)throws SQLException{

        List<LinkedHashMap<String, Object>> list = DbConnection.executeQuery(singleQuery);
        for(LinkedHashMap<String, Object> rs : list){
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<String, Object> entry : rs.entrySet()){
                sb.append(entry.getValue()).append("\t");
            }
            addResult(sb.toString());
        }
        addResult("");
        addResult(new Date() + ":" + list.size() + "件");
        addResult("");
    }

    public void execute(String singleQuery)throws SQLException{

        int num = DbConnection.executeUpdate(singleQuery);
        addResult(new Date() + ":" + num + "件");
        addResult("");
    }

    public void commit(){
        try{
            DbConnection.commit();
        }catch(Exception e){
            addResult(e.getMessage());
            addResult("");
        }
    }

    public void rollback(){
        try{
            DbConnection.rollback();
        }catch(Exception e){
            addResult(e.getMessage());
            addResult("");
        }
    }

    public void addResult(String text){
        result.appendText(text + "\n");
    }
}
