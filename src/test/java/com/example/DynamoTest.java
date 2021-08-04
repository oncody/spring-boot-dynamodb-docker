package com.example;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = { Config.class, DynamoService.class })
public class DynamoTest {
  private final DynamoService<Product> dynamo;

  DynamoTest(@Autowired DynamoService<Product> dynamo) {
    this.dynamo = dynamo;
  }

  @Test
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  public void testProductTable() {
    String price = "50";
    String cost = "20";

    try {
      dynamo.createTable(Product.class);
    } catch (Exception e) {
    }

    dynamo.deleteAllRecords();
    Iterator<Product> records = dynamo.getAllRecords().iterator();
    assertFalse(records.hasNext());

    dynamo.insertRecord(new Product(price, cost));
    records = dynamo.getAllRecords().iterator();
    assertTrue(records.hasNext());

    Product record = records.next();
    assertEquals(price, record.getPrice());
    assertEquals(cost, record.getCost());
    assertFalse(records.hasNext());
    System.out.println(record.getId());
  }
}