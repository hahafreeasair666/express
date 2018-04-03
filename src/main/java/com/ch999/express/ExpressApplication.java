package com.ch999.express;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author hahalala
 */
@MapperScan("com.ch999.**.mapper*")
@EnableAutoConfiguration
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
/*@EnableRedisRepositories(basePackages = "com.ch999.haha.admin.repository.redis")*/
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class,MongoAutoConfiguration.class,MongoDataAutoConfiguration.class})
public class ExpressApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpressApplication.class, args);
	}
}
