package com.bancolombia.ms_stats;

import com.bancolombia.ms_stats.infraestructure.bus.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsStatsApplication  {

	@Autowired
	private MessageProducer producer;

	public static void main(String[] args) {

		SpringApplication.run(MsStatsApplication.class, args);
	}

}
