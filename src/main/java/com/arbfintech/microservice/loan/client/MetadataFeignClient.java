package com.arbfintech.microservice.loan.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("microservice-metadata")
@RequestMapping("/metadata")
public interface MetadataFeignClient {

    ///////////////////////////////////
    // portfolios
    ///////////////////////////////////

    @GetMapping("/portfolios")
    String listPortfolio();

    @GetMapping("/portfolios/{id}")
    String getPortfolioById(
            @PathVariable("id") Integer id);

    @PostMapping("/portfolios")
    String addPortfolio(
            @RequestBody String portfolioStr);

    @PutMapping("/portfolios/{id}")
    String updatePortfolio(
            @PathVariable("id") Integer id,
            @RequestBody String portfolioStr);

    @DeleteMapping("/portfolios/{id}")
    String deletePortfolio(
            @PathVariable("id") Integer id);

    @GetMapping("/portfolio/{companyId}")
    String getPortfolioByCompanyId(@PathVariable("companyId") Integer companyId);


    ///////////////////////////////////
    // lead-provider
    ///////////////////////////////////

    @GetMapping("/lead-providers")
    String listLeadProvider();

    @PostMapping("/lead-providers")
    String addLeadProvider(
            @RequestBody String leadProviderStr);

    @PutMapping("/lead-providers/{id}")
    String updateLeadProvider(
            @PathVariable("id") Integer id,
            @RequestBody String leadProviderStr);

    @DeleteMapping("/lead-providers/{id}")
    String deleteLeadProvider(
            @PathVariable("id") Integer id);

    ///////////////////////////////////
    // ach-provider
    ///////////////////////////////////

    @GetMapping("/ach-providers")
    String listAchProvider();

    @PostMapping("/ach-providers")
    String addAchProvider(
            @RequestBody String achProviderStr);

    @PutMapping("/ach-providers/{id}")
    String updateAchProvider(
            @PathVariable("id") Integer id,
            @RequestBody String achProviderStr);

    @DeleteMapping("/ach-providers/{id}")
    String deleteAchProvider(
            @PathVariable("id") Integer id);

    ///////////////////////////////////
    // holiday
    ///////////////////////////////////

    @GetMapping("/holidays")
    String listHoliday();

    @PostMapping("/holidays/{year}")
    String batchAddHolidays(
            @PathVariable("year") Integer year,
            @RequestBody String holidayStr);

    @PutMapping("/holidays/{id}")
    String updateHolidayById(
            @PathVariable("id") Integer id,
            @RequestBody String holidayStr);

    @DeleteMapping("/holidays/{id}")
    String deleteHolidayById(
            @PathVariable("id") Integer id);

    ///////////////////////////////////
    // program
    ///////////////////////////////////

    @GetMapping("/programs")
    String listProgramByCompanyId();

    @PostMapping("/programs")
    String addProgram(@RequestBody String programStr);

    @PutMapping("/programs/{programId}")
    String updateProgram(@PathVariable("programId") Integer programId, @RequestBody String programStr);

    @PostMapping("/programs/{programId}")
    String switchProgramStatus(@PathVariable("programId") Integer programId, @RequestParam("status") Integer status);






    @GetMapping("/metadatas")
    String listMetadataByCompanyId(
            @RequestParam("companyId") Integer companyId
    );

    @PostMapping("/metadatas")
    String addMetadata(
            @RequestBody String metadataStr
    );

    @PutMapping("/metadatas/{id}")
    String updateMetadata(
            @PathVariable("id") Integer id,
            @RequestBody String metadataStr
    );

    @DeleteMapping("/metadatas/{id}")
    String deleteMetadata(
            @PathVariable("id") Integer id
    );

    @GetMapping("/metadata-options")
    String listMetadataOptionByMetadataId(
            @RequestParam("metadataId") Integer metadataId
    );

    @PostMapping("/metadata-options")
    String addMetadataOption(
            @RequestBody String metadataOptionStr
    );

    @PutMapping("/metadata-options/{id}")
    String updateMetadataOption(
            @PathVariable("id") Integer id,
            @RequestBody String metadataOptionStr
    );

    @DeleteMapping("/metadata-options/{id}")
    String deleteMetadataOption(
            @PathVariable("id") Integer id
    );

}
