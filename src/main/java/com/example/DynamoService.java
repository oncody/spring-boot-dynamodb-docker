package com.example;

import java.util.Iterator;

import com.example.model.DynamoModel;

import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoService {
    private final DynamoDbEnhancedClient database;

    public DynamoService(@Autowired DynamoDbEnhancedClient database) {
        this.database = database;
    }

    public void createTable(DynamoModel model) {
        getTable(model).createTable();
    }

    public void deleteTable(DynamoModel model) {
        getTable(model).deleteTable();
    }

    public Iterator getAllRecords(DynamoModel model) {
        return getTable(model).scan().items().iterator();
    }

    public void insertRecord(DynamoModel model, Object payload) {
        getTable(model).putItem(payload);
    }

    private DynamoDbTable getTable(DynamoModel model) {
        return database.table(model.modelName(), TableSchema.fromBean(model.getClass()));
    }
}