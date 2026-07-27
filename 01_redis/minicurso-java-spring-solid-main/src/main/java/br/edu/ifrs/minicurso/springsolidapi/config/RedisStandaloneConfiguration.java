package br.edu.ifrs.minicurso.springsolidapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
public class RedisStandaloneConfiguration {

  /**
   * Jedis
   */
  @Bean
  public RedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory();
  }
}