package com.example;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component
public class DynamoService {
    private final DynamoDbEnhancedClient database;

    public DynamoService(@Autowired DynamoDbEnhancedClient database) {
        this.database = database;
    }

    public void createTable(Class clazz, String tableName) {
        database.table(tableName, TableSchema.fromClass(clazz)).createTable();
    }

    public void deleteTable(Class clazz, String tableName) {
        database.table(tableName, TableSchema.fromClass(clazz)).deleteTable();
    }

    public Iterator<Object> getAllRecords(Class clazz, String tableName) {
        return database.table(tableName, TableSchema.fromBean(clazz)).scan().items().iterator();
    }

    public void insertRecord(Class clazz, String tableName, Object payload) {
        database.table(tableName, TableSchema.fromBean(clazz)).putItem(payload);
    }
}