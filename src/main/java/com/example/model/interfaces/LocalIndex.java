package com.example.model.interfaces;

import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedLocalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.model.Projection;
import software.amazon.awssdk.services.dynamodb.model.ProjectionType;

public abstract class LocalIndex {
    public abstract String indexName();

    public EnhancedLocalSecondaryIndex toLocalSecondaryIndex() {
        Projection projection = Projection.builder().projectionType(ProjectionType.ALL).build();

        return EnhancedLocalSecondaryIndex.builder().indexName(indexName()).projection(projection).build();
    }
}
