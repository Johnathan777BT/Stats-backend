package com.bancolombia.ms_stats.infraestructure.config;

import com.bancolombia.ms_stats.domain.model.Stats;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Component
public class DynamoDbInitializer {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;
    private final DynamoDbAsyncTable<Stats> contactoClienteTable;

    public DynamoDbInitializer(DynamoDbAsyncClient dynamoDbAsyncClient,
                               DynamoDbEnhancedAsyncClient enhancedClient) {
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
        this.contactoClienteTable = enhancedClient.table(
                "stats", TableSchema.fromBean(Stats.class));
    }

    @PostConstruct
    public void init() {
        dynamoDbAsyncClient.listTables()
                .thenAccept(result -> {
                    if (!result.tableNames().contains("stats")) {
                        contactoClienteTable.createTable(builder -> builder
                                .provisionedThroughput(p -> p
                                        .readCapacityUnits(5L)
                                        .writeCapacityUnits(5L)
                                )
                        ).join();
                        System.out.println("✅ Tabla creada: stats");
                    } else {
                        System.out.println("✅ Tabla ya existe: stats");
                    }
                })
                .join();
    }
}
