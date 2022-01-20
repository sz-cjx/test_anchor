package com.arbfintech.microservice.customer.restapi.repository.cache;

import com.arbfintech.framework.component.cache.core.BaseRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AlgorithmRedisRepository extends BaseRedisRepository {

    /**
     * 重写基类Repository中的方法，以切换成slave源，完成多数据源的配置
     *
     * @return
     */
    @Override
    protected StringRedisTemplate redisReaderTemplate() {
        return algorithmReaderRedisTemplate;
    }


    @Override
    protected StringRedisTemplate redisWriterTemplate() {
        return algorithmWriterRedisTemplate;
    }


    /**
     * 多数据源的配置。自动注入配置好的一个redis源的模板对象，
     * 此对象必须在配置类中进行配置，并且此处注入的bean name需要与配置一致
     */
    @Autowired
    @Qualifier("algorithmReaderRedisTemplate")
    private StringRedisTemplate algorithmReaderRedisTemplate;

    @Autowired
    @Qualifier("algorithmWriterRedisTemplate")
    private StringRedisTemplate algorithmWriterRedisTemplate;
}
