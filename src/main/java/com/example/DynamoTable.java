package com.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.example.model.interfaces.DynamoModel;
import com.example.model.interfaces.GlobalIndex;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

public class DynamoTable<T extends DynamoModel> {
    private final DynamoDbEnhancedClient database;
    private final Class<T> clazz;
    private final T model;

    public DynamoTable(DynamoDbEnhancedClient database, Class<T> clazz, T model) {
        this.database = database;
        this.clazz = clazz;
        this.model = model;
    }

    public void createTable() {
        CreateTableEnhancedRequest.Builder builder = CreateTableEnhancedRequest.builder();
        if (!model.globalIndexes().isEmpty()) {
            builder = builder.globalSecondaryIndices(model.globalIndexes().stream()
                    .map(index -> index.toGlobalSecondaryIndex()).collect(Collectors.toList()));
        }

        if (!model.localIndexes().isEmpty()) {
            builder = builder.localSecondaryIndices(model.localIndexes().stream()
                    .map(index -> index.toLocalSecondaryIndex()).collect(Collectors.toList()));
        }

        CreateTableEnhancedRequest request = builder.build();
        getTable().createTable(request);
    }

    public void deleteTable() {
        getTable().deleteTable();
    }

    public List<T> query(QueryConditional query) {
        return StreamSupport.stream(getTable().query(query).spliterator(), false).flatMap(page -> page.items().stream())
                .collect(Collectors.toList());
    }

    public List<T> indexQuery(GlobalIndex globalIndex, QueryConditional query) {
        return StreamSupport.stream(getTable().index(globalIndex.indexName()).query(query).spliterator(), false)
                .flatMap(page -> page.items().stream()).collect(Collectors.toList());
    }

    public List<T> getAllRecords() {
        return StreamSupport.stream(getTable().scan().items().spliterator(), false).collect(Collectors.toList());
    }

    public void insertRecord(T model) {
        getTable().putItem(model);
    }

    private DynamoDbTable<T> getTable() {
        return database.table(model.modelName(), TableSchema.fromBean(clazz));
    }
}