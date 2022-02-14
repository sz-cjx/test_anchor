package com.arbfintech.microservice.customer.restapi.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class AuthRedisConfiguration {

    /**
     * 配置为Redis配置jedis连接池
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.jedis.pool")
    @Scope(value = "prototype")
    public GenericObjectPoolConfig redisPool(){
        return new GenericObjectPoolConfig();
    }

    /**
     * 指定redis的数据源
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.auth.reader")
    public RedisStandaloneConfiguration authReaderRedisConfig(){
        return new RedisStandaloneConfiguration();
    }

    /**
     * 自定义数据源使用的Factory对象
     */
    @Bean
    public JedisConnectionFactory authReaderFactory(){
        GenericObjectPoolConfig config = redisPool();
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling()
                .poolConfig(config)
                .build();
        return new JedisConnectionFactory(authReaderRedisConfig(), jedisClientConfiguration);
    }

    /**
     *自定义数据源使用的模板对象，连接数据源工厂对象与模板对象
     */
    @Bean(name = "authReaderRedisTemplate")
    public StringRedisTemplate authReaderRedisTemplate(){
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(authReaderFactory());
        return template;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.auth.writer")
    public RedisStandaloneConfiguration authWriterRedisConfig() {
        return new RedisStandaloneConfiguration();
    }

    /**
     * 自定义数据源使用的Factory对象
     */
    @Bean
    public JedisConnectionFactory authWriterFactory() {
        GenericObjectPoolConfig config = redisPool();
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling()
                .poolConfig(config)
                .build();
        return new JedisConnectionFactory(authWriterRedisConfig(), jedisClientConfiguration);
    }

    /**
     * 自定义数据源使用的模板对象，连接数据源工厂对象与模板对象
     */
    @Bean(name = "authWriterRedisTemplate")
    public StringRedisTemplate authWriterRedisTemplate() {
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(authWriterFactory());
        return template;
    }

    /**
     * 设置Redis模板对象的相关参数，序列化工具等
     * @return
     */
    private StringRedisTemplate getRedisTemplate(){
        StringRedisTemplate template = new StringRedisTemplate();
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
