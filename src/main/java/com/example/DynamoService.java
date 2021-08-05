package com.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.model.DynamoModel;
import com.example.model.GlobalIndex;

import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

public class DynamoService {
    private final DynamoDbEnhancedClient database;

    public DynamoService(@Autowired DynamoDbEnhancedClient database) {
        this.database = database;
    }

    public void createTable(DynamoModel model) {
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
        getTable(model).createTable(request);
    }

    public void deleteTable(DynamoModel model) {
        getTable(model).deleteTable();
    }

    public List query(DynamoModel model, QueryConditional query) {
        Iterator<Page> results = getTable(model).query(query).iterator();
        List records = new ArrayList<>();
        while (results.hasNext()) {
            Page record = results.next();
            records.addAll(record.items());
        }

        return records;
    }

    public List indexQuery(DynamoModel model, GlobalIndex globalIndex, QueryConditional query) {
        DynamoDbIndex tableIndex = getTable(model).index(globalIndex.indexName());
        Iterator<Page> results = tableIndex.query(query).iterator();
        List records = new ArrayList<>();
        while (results.hasNext()) {
            Page record = results.next();
            records.addAll(record.items());
        }

        return records;
    }

    public Iterator getAllRecords(DynamoModel model) {
        return getTable(model).scan().items().iterator();
    }

    public void insertRecord(DynamoModel model) {
        getTable(model).putItem(model);
    }

    private DynamoDbTable getTable(DynamoModel model) {
        return database.table(model.modelName(), TableSchema.fromBean(model.getClass()));
    }
}