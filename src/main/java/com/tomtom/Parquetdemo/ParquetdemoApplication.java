package com.tomtom.Parquetdemo;

import org.apache.hadoop.conf.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ParquetdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParquetdemoApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "hadoop")
	public Map<String, String> hadoopProperties() {
		return new HashMap<>();
	}

	@Bean
	public Configuration hadoopConfig(Map<String, String> hadoopProperties) {
		Configuration conf = new Configuration(false);
		hadoopProperties.forEach(conf::set);
		return conf;
	}

}
