package com.example.model;

import java.util.List;

public interface DynamoModel {
    String modelName();
    List<GlobalIndex> globalIndexes();
    List<LocalIndex> localIndexes();
}
