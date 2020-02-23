package com.arbfintech.microservice.customer.restapi.service;

import com.arbfintech.microservice.customer.domain.repository.reader.CustomerReaderJdbcRepository;
import com.arbfintech.microservice.customer.domain.repository.writer.CustomerWriterJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRestService {

    public <E> Long save(Class<E> entityClass, String entityStr) {
        return customerWriter.save(entityClass, entityStr);
    }

    public <E> String findById(Class<E> entityClass, Long id, String optionStr) {
        return customerReader.findById(entityClass, id, optionStr);
    }

    public <E> String findAllByOptions(Class<E> entityClass, String optionStr) {
        return customerReader.findAllByOptions(entityClass, optionStr);
    }

    @Autowired
    private CustomerReaderJdbcRepository customerReader;

    @Autowired
    private CustomerWriterJdbcRepository customerWriter;
}
