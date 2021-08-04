package com.example;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.example.model.DynamoModel;
import com.example.model.Product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    dynamo.insertRecord(productTable, new Product(price, cost));
    records = dynamo.getAllRecords(productTable);
    assertTrue(records.hasNext());

    Product record = (Product) records.next();
    assertEquals(price, record.getPrice());
    assertEquals(cost, record.getCost());
    assertFalse(records.hasNext());
    System.out.println(record.getId());
  }
}