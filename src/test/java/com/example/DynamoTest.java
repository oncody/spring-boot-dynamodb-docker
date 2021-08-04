package com.example;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.example.table.DynamoTable;
import com.example.table.ProductTable;

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
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  public void testProductTable() {
    DynamoTable productTable = new ProductTable();
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

    dynamo.insertRecord(productTable, new ProductTable(price, cost));
    records = dynamo.getAllRecords(productTable);
    assertTrue(records.hasNext());

    ProductTable record = (ProductTable) records.next();
    assertEquals(price, record.getPrice());
    assertEquals(cost, record.getCost());
    assertFalse(records.hasNext());
    System.out.println(record.getId());
  }
}