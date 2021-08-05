package com.example.model.interfaces;

import java.util.List;

public interface DynamoModel {
    String modelName();
    List<GlobalIndex> globalIndexes();
    List<LocalIndex> localIndexes();
}
