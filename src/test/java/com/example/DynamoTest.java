package com.example;

import java.util.List;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.example.model.DynamoModel;
import com.example.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.UUID;

@SpringBootTest(classes = { Config.class, DynamoService.class })
public class DynamoTest {
  private final DynamoService dynamo;
  private static final Product PRODUCT = new Product(1, 50, 20);

  DynamoTest(@Autowired DynamoService dynamo) {
    this.dynamo = dynamo;
  }

  @BeforeEach
  public void setup() {
    try {
      dynamo.deleteTable(PRODUCT);
    } catch (Exception e) {
      System.out.println(e);
    }

    try {
      dynamo.createTable(PRODUCT);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testTablesCreatedAreEmpty() {
    Iterator records = dynamo.getAllRecords(PRODUCT);
    assertFalse(records.hasNext());
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testInsertingAndGettingRecord() {
    dynamo.insertRecord(PRODUCT);

    Iterator records = dynamo.getAllRecords(PRODUCT);
    records = dynamo.getAllRecords(PRODUCT);
    assertTrue(records.hasNext());

    Product record = (Product) records.next();
    assertEquals(PRODUCT.getId(), record.getId());
    assertEquals(PRODUCT.getPrice(), record.getPrice());
    assertEquals(PRODUCT.getCost(), record.getCost());
    assertFalse(records.hasNext());
    System.out.println(record.getId());
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testInsertingAndGettingRecordWithIndex() {
    dynamo.insertRecord(PRODUCT);

    Key key = Key.builder().partitionValue(PRODUCT.getId()).build();
    QueryConditional query = QueryConditional.keyEqualTo(key);
    List records = dynamo.query(PRODUCT, query);
    assertEquals(1, records.size());

    Product record = (Product) records.get(0);
    assertEquals(PRODUCT.getId(), record.getId());
    assertEquals(PRODUCT.getPrice(), record.getPrice());
    assertEquals(PRODUCT.getCost(), record.getCost());
    System.out.println(record.getId());
  }
}