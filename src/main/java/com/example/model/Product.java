package com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedLocalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.model.Projection;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;

@DynamoDbBean
public class Product implements DynamoModel {
    public static final String PRICE_INDEX_QUERY = "priceIndex";

    private int id;
    private int price;
    private int cost;

    public Product() {
    }

    public Product(int id, int price, int cost) {
        // this.id = UUID.randomUUID().toString();
        this.id = id;
        this.price = price;
        this.cost = cost;
    }

    @DynamoDbPartitionKey
    public int getId() {
        return id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = { PRICE_INDEX_QUERY })
    public int getPrice() {
        return price;
    }

    @DynamoDbSecondarySortKey(indexNames = { PRICE_INDEX_QUERY })
    public int getCost() {
        return cost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String modelName() {
        return "Product";
    }

    @Override
    public List<EnhancedGlobalSecondaryIndex> globalSecondaryIndices() {
        List<String> indexNames = new ArrayList<>();
        List<EnhancedGlobalSecondaryIndex> indices = new ArrayList<>();
        indexNames.add(PRICE_INDEX_QUERY);

        for (String indexName : indexNames) {
            ProvisionedThroughput throughput = ProvisionedThroughput.builder().readCapacityUnits(5L)
                    .writeCapacityUnits(5L).build();

            Projection projection = Projection.builder().projectionType(ProjectionType.ALL).build();

            EnhancedGlobalSecondaryIndex index = EnhancedGlobalSecondaryIndex.builder().indexName(indexName)
                    .provisionedThroughput(throughput).projection(projection).build();

            indices.add(index);
        }

        return indices;
    }

    @Override
    public List<EnhancedLocalSecondaryIndex> localSecondaryIndices() {
        List<String> indexNames = new ArrayList<>();
        List<EnhancedLocalSecondaryIndex> indices = new ArrayList<>();
        // indexNames.add(PRICE_INDEX_QUERY);

        for (String indexName : indexNames) {
            Projection projection = Projection.builder().projectionType(ProjectionType.ALL).build();

            EnhancedLocalSecondaryIndex index = EnhancedLocalSecondaryIndex.builder().indexName(indexName)
                    .projection(projection).build();

            indices.add(index);
        }

        return indices;
    }
}