package com.example;

import java.util.List;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public class DynamoService<T> {
    private final AmazonDynamoDB dynamo;
    private final DynamoDBMapper mapper;
    private final CrudRepository<T, String> repo;

    public DynamoService(@Autowired AmazonDynamoDB dynamo, @Autowired DynamoDBMapper mapper,
            @Autowired CrudRepository<T, String> repo) {
        this.dynamo = dynamo;
        this.mapper = mapper;
        this.repo = repo;
    }

    public void createTable(Class clazz) {
        CreateTableRequest tableRequest = mapper.generateCreateTableRequest(clazz);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        dynamo.createTable(tableRequest);
    }

    public void deleteAllRecords() {
        mapper.batchDelete(repo.findAll());
    }

    public List<T> getAllRecords() {
        return Lists.newArrayList(repo.findAll());
    }

    public void insertRecord(T payload) {
        repo.save(payload);
    }
}