package com.bugworm.jdbcexecutor;

import javafx.scene.control.TextArea;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TabController{

    public TextArea query;
    public TextArea result;

    public void execute(){
        String q = query.getText();
        String qedit = q.trim().toLowerCase();
        try{
            if(qedit.startsWith("select")){
                List<LinkedHashMap<String, Object>> list = DbConnection.executeQuery(q);
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
            }else if(qedit.startsWith("update") || qedit.startsWith("insert") || qedit.startsWith("delete")){
                int num = DbConnection.executeUpdate(q);
                addResult(new Date() + ":" + num + "件");
                addResult("");
            }else{
                throw new RuntimeException("Execute only select, update, insert and delete.");
            }
        }catch(Exception e){
            e.printStackTrace();
            addResult(e.getMessage());
            addResult("");
        }
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
        String org = result.getText();
        result.setText(org + text + "\n");
    }
}
