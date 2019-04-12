package com.arbfintech.microservice.loan.service;

import com.arbfintech.microservice.loan.entity.Document;
import com.arbfintech.microservice.loan.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Wade
 */
@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    private DocumentRepository documentRepository;

    public Document getDataByLoanId(Integer loanId){
        return documentRepository.findByLoanId(loanId);
    }

    public Document saveData(Document document){
        logger.info("Save the document Data");
        return documentRepository.save(document);
    }
}
