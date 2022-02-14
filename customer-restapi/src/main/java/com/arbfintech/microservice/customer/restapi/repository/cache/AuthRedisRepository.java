package com.arbfintech.microservice.customer.restapi.repository.cache;

import com.arbfintech.framework.component.cache.core.BaseRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * 需要使用redis多数据源的地方，自动注入此类，可以连接至redis的配置中指定的库。
 * 具体连接值哪一个库，需要根据配置决定
 */
@Repository
public class AuthRedisRepository extends BaseRedisRepository {

    /**
     * 重写基类Repository中的方法，以切换成slave源，完成多数据源的配置
     *
     * @return
     */
    @Override
    protected StringRedisTemplate redisReaderTemplate() {
        return authReaderRedisTemplate;
    }

    @Override
    protected StringRedisTemplate redisWriterTemplate() {
        return authWriterRedisTemplate;
    }

    /**
     * 多数据源的配置。自动注入配置好的一个redis源的模板对象，
     * 此对象必须在配置类中进行配置，并且此处注入的bean name需要与配置一致
     */
    @Autowired
    @Qualifier("authReaderRedisTemplate")
    private StringRedisTemplate authReaderRedisTemplate;

    @Autowired
    @Qualifier("authWriterRedisTemplate")
    private StringRedisTemplate authWriterRedisTemplate;

}
