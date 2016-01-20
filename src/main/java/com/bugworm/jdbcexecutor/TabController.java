package com.bugworm.jdbcexecutor;

import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

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
            addResult(e.getMessage(), "");
            try{
                DbConnection.rollback();
                addResult("Rollback.");
            }catch(SQLException sqlEx){
                sqlEx.printStackTrace();
                addResult(sqlEx.getMessage(), "", "Fail to rollback.");
            }
        }
        addResult("---------------------------------------------------------------");
    }

    public void executeQuery(String singleQuery)throws SQLException{

        List<LinkedHashMap<String, Object>> list = DbConnection.executeQuery(singleQuery);
        list.stream().forEach(rs -> {
            StringBuilder sb = new StringBuilder();
            rs.entrySet().stream().forEach(entry -> sb.append(entry.getValue()).append(("\t")));
            addResult(sb.toString());
        });
        addResult("", new Date() + " [" + list.size() + "件]", "");
    }

    public void execute(String singleQuery)throws SQLException{

        int num = DbConnection.executeUpdate(singleQuery);
        addResult("", new Date() + " [" + num + "件]", "");
    }

    public void commit(){
        try{
            DbConnection.commit();
        }catch(Exception e){
            addResult(e.getMessage(), "");
        }
    }

    public void rollback(){
        try{
            DbConnection.rollback();
        }catch(Exception e){
            addResult(e.getMessage(), "");
        }
    }

    public void addResult(String... text){
        Stream.of(text).forEach(t -> result.appendText(t + "\n"));
    }
}
