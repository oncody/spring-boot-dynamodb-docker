package com.example;

import java.util.List;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.example.model.DynamoModel;
import com.example.model.Product;

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

  DynamoTest(@Autowired DynamoService dynamo) {
    this.dynamo = dynamo;
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testTablesCreatedAreEmpty() {
    DynamoModel productTable = new Product();
    String price = "50";
    String cost = "20";

    try {
      dynamo.deleteTable(productTable);
    } catch (Exception e) {
      System.out.println(e);
    }

    try {
      dynamo.createTable(productTable);
    } catch (Exception e) {
      System.out.println(e);
    }

    Iterator records = dynamo.getAllRecords(productTable);
    assertFalse(records.hasNext());
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testInsertingAndGettingRecord() {
    DynamoModel productTable = new Product();
    String id = "1";
    String price = "50";
    String cost = "20";

    try {
      dynamo.deleteTable(productTable);
    } catch (Exception e) {
      System.out.println(e);
    }

    try {
      dynamo.createTable(productTable);
    } catch (Exception e) {
      System.out.println(e);
    }

    dynamo.insertRecord(productTable, new Product(id, price, cost));

    Iterator records = dynamo.getAllRecords(productTable);
    records = dynamo.getAllRecords(productTable);
    assertTrue(records.hasNext());

    Product record = (Product) records.next();
    assertEquals(id, record.getId());
    assertEquals(price, record.getPrice());
    assertEquals(cost, record.getCost());
    assertFalse(records.hasNext());
    System.out.println(record.getId());
  }

  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  public void testInsertingAndGettingRecordWithIndex() {
    DynamoModel productTable = new Product();
    String id = "1";
    String price = "50";
    String cost = "20";

    try {
      dynamo.deleteTable(productTable);
    } catch (Exception e) {
      System.out.println(e);
    }

    try {
      dynamo.createTable(productTable);
    } catch (Exception e) {
      System.out.println(e);
    }

    dynamo.insertRecord(productTable, new Product(id, price, cost));

    Key key = Key.builder().partitionValue("1").build();
    QueryConditional query = QueryConditional.keyEqualTo(key);
    System.out.println("Test Value: " + Product.PRICE_INDEX_QUERY);
    List records = dynamo.indexQuery(productTable, Product.PRICE_INDEX_QUERY, query);
    assertEquals(1, records.size());

    Product record = (Product) records.get(0);
    assertEquals(id, record.getId());
    assertEquals(price, record.getPrice());
    assertEquals(cost, record.getCost());
    System.out.println(record.getId());
  }
}