package com.example.model;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedLocalSecondaryIndex;

public interface DynamoModel {
    String modelName();
    List<EnhancedGlobalSecondaryIndex> globalSecondaryIndices();
    List<EnhancedLocalSecondaryIndex> localSecondaryIndices();
}
