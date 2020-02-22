package com.arbfintech.microservice.customer.restapi.service;

import com.arbfintech.microservice.customer.domain.repository.reader.GeneralReaderJdbcRepository;
import com.arbfintech.microservice.customer.domain.repository.writer.GeneralWriterJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralRestService {

    public <E> Long save(Class<E> entityClass, String entityStr) {
        return generalWriter.save(entityClass, entityStr);
    }

    public <E> String findById(Class<E> entityClass, Long id, String optionStr) {
        return generalReader.findById(entityClass, id, optionStr);
    }

    public <E> String findAllByOptions(Class<E> entityClass, String optionStr) {
        return generalReader.findAllByOptions(entityClass, optionStr);
    }

    @Autowired
    private GeneralReaderJdbcRepository generalReader;

    @Autowired
    private GeneralWriterJdbcRepository generalWriter;
}
