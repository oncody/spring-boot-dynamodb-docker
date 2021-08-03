package com.example;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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

    dynamo.createTable(Product.class);
    dynamo.deleteAllRecords();
    List<Product> records = dynamo.getAllRecords();
    assertThat(records.size(), is(equalTo(0)));

    dynamo.insertRecord(new Product(price, cost));
    records = dynamo.getAllRecords();
    assertThat(records.size(), is(equalTo(1)));

    Product record = records.get(0);
    assertThat(record.getPrice(), is(equalTo(price)));
    assertThat(record.getCost(), is(equalTo(cost)));
  }
}