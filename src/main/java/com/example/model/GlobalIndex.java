package com.example.model;

import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.model.Projection;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;

public abstract class GlobalIndex {
    public abstract String indexName();

    public EnhancedGlobalSecondaryIndex toGlobalSecondaryIndex() {
        ProvisionedThroughput throughput = ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L)
                .build();

        Projection projection = Projection.builder().projectionType(ProjectionType.ALL).build();

        return EnhancedGlobalSecondaryIndex.builder().indexName(indexName()).provisionedThroughput(throughput)
                .projection(projection).build();
    }
}
