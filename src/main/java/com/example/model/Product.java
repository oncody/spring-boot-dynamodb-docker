package com.example.model;

import java.util.List;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

@DynamoDbBean
public class Product implements DynamoModel {
    public static final class PriceGlobalIndex extends GlobalIndex {
        @Override
        public String indexName() {
            return "priceIndex";
        }
    }

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

    @DynamoDbSecondaryPartitionKey(indexNames = { "priceIndex" })
    public int getPrice() {
        return price;
    }

    @DynamoDbSecondarySortKey(indexNames = { "priceIndex" })
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
    public List<GlobalIndex> globalIndexes() {
        return List.of(new PriceGlobalIndex());
    }

    @Override
    public List<LocalIndex> localIndexes() {
        return List.of();
    }
}