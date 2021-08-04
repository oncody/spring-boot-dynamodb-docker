package com.example;

import java.util.Iterator;
import com.example.table.DynamoTable;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoService {
    private final DynamoDbEnhancedClient database;

    public DynamoService(@Autowired DynamoDbEnhancedClient database) {
        this.database = database;
    }

    public void createTable(DynamoTable table) {
        getTable(table).createTable();
    }

    public void deleteTable(DynamoTable table) {
        getTable(table).deleteTable();
    }

    public Iterator getAllRecords(DynamoTable table) {
        return getTable(table).scan().items().iterator();
    }

    public void insertRecord(DynamoTable table, Object payload) {
        getTable(table).putItem(payload);
    }

    private DynamoDbTable getTable(DynamoTable table) {
        return database.table(table.tableName(), TableSchema.fromBean(table.getClass()));
    }
}