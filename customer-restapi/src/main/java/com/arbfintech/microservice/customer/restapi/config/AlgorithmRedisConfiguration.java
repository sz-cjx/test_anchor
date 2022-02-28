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
public class AlgorithmRedisConfiguration {

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
    @ConfigurationProperties(prefix = "spring.redis.algorithm.reader")
    public RedisStandaloneConfiguration algorithmReaderRedisConfig(){
        return new RedisStandaloneConfiguration();
    }

    /**
     * 自定义数据源使用的Factory对象
     */
    @Bean
    public JedisConnectionFactory algorithmReaderFactory(){
        GenericObjectPoolConfig config = redisPool();
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling()
                .poolConfig(config)
                .build();
        return new JedisConnectionFactory(algorithmReaderRedisConfig(), jedisClientConfiguration);
    }

    /**
     *自定义数据源使用的模板对象，连接数据源工厂对象与模板对象
     */
    @Bean(name = "algorithmReaderRedisTemplate")
    public StringRedisTemplate algorithmReaderRedisTemplate(){
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(algorithmReaderFactory());
        return template;
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.redis.algorithm.writer")
    public RedisStandaloneConfiguration algorithmWriterRedisConfig(){
        return new RedisStandaloneConfiguration();
    }

    @Bean
    public JedisConnectionFactory algorithmWriterFactory(){
        GenericObjectPoolConfig config = redisPool();
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling()
                .poolConfig(config)
                .build();
        return new JedisConnectionFactory(algorithmWriterRedisConfig(), jedisClientConfiguration);
    }

    @Bean(name = "algorithmWriterRedisTemplate")
    public StringRedisTemplate algorithmWriterRedisTemplate(){
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(algorithmWriterFactory());
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
