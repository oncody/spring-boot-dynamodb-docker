package com.example;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = { Config.class })
public class DynamoTest {
  private static final Product PRODUCT = new Product(1, 50, 20);
  private final DynamoService<Product> dynamo;

  DynamoTest(@Autowired DynamoDbEnhancedClient database) {
    dynamo = new DynamoService<>(database, Product.class, PRODUCT);
  }

  @BeforeEach
  public void setup() {
    try {
      dynamo.deleteTable();
    } catch (Exception e) {
      System.out.println(e);
    }

    try {
      dynamo.createTable();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testTablesCreatedAreEmpty() {
    List<Product> records = dynamo.getAllRecords();
    assertTrue(records.isEmpty());
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testInsertingAndGettingRecord() {
    dynamo.insertRecord(PRODUCT);

    List<Product> records = dynamo.getAllRecords();
    records = dynamo.getAllRecords();
    assertEquals(1, records.size());

    Product record = (Product) records.get(0);
    assertEquals(PRODUCT.getId(), record.getId());
    assertEquals(PRODUCT.getPrice(), record.getPrice());
    assertEquals(PRODUCT.getCost(), record.getCost());
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testInsertingAndGettingRecordWithQuery() {
    dynamo.insertRecord(PRODUCT);

    Key key = Key.builder().partitionValue(PRODUCT.getId()).build();
    QueryConditional query = QueryConditional.keyEqualTo(key);
    List<Product> records = dynamo.query(query);
    assertEquals(1, records.size());

    Product record = (Product) records.get(0);
    assertEquals(PRODUCT.getId(), record.getId());
    assertEquals(PRODUCT.getPrice(), record.getPrice());
    assertEquals(PRODUCT.getCost(), record.getCost());
    System.out.println(record.getId());
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testInsertingAndGettingRecordWithIndex() {
    dynamo.insertRecord(PRODUCT);

    Key key = Key.builder().partitionValue(PRODUCT.getPrice()).build();
    QueryConditional query = QueryConditional.keyEqualTo(key);
    List<Product> records = dynamo.indexQuery(Product.PRICE_GLOBAL_INDEX, query);
    assertEquals(1, records.size());

    Product record = (Product) records.get(0);
    assertEquals(PRODUCT.getId(), record.getId());
    assertEquals(PRODUCT.getPrice(), record.getPrice());
    assertEquals(PRODUCT.getCost(), record.getCost());
    System.out.println(record.getId());
  }
}