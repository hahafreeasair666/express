package com.ch999.express.admin.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author hahalala
 */
@Configuration
@EnableRedisRepositories(basePackages = "com.ch999.express.admin.repository",redisTemplateRef = "redisTemplate")
public class RedisRepositoryConfig {
}
