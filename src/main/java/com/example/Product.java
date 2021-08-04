package com.example;

import java.util.UUID;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Product {
    private String id;
    private String price;
    private String cost;

    public Product() {
    }

    public Product(String price, String cost) {
        this.id = UUID.randomUUID().toString();
        this.price = price;
        this.cost = cost;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getCost() {
        return cost;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}