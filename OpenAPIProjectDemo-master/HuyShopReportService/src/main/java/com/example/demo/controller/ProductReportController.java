package com.example.demo.controller;


import com.example.demo.model.ProductReport;
import com.example.demo.service.ProductReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/report")
public class ProductReportController {

    @Autowired
    ProductReportService service;

    @GetMapping(value = "/products")
    public ResponseEntity<List<ProductReport>> getProductDataToReport()
    {
        return new ResponseEntity<>(service.getProductData(), HttpStatus.OK);
    }

    @GetMapping(value = "/dowload")
    public ResponseEntity<Object> dowloadFile() throws IOException {
        FileWriter filewriter =  null;
        try {
            List<ProductReport> productReportList = service.getProductData();
            StringBuilder fileContent = new StringBuilder("ID,NAME,PRICE,QUANTITY-TOTAL,SOLD,QUANTITY-PRESENT,STATUS");
            for (ProductReport product :productReportList) {
                fileContent.append("\n").append(product.getId()).append(",")
                        .append(product.getName()).append(",")
                        .append(product.getPrice()).append(",")
                        .append(product.getQuantity()).append(",")
                        .append(product.getQuantity()- product.getQuantityPresent()).append(",")
                        .append(product.getQuantityPresent()).append(",")
                        .append(product.isStatus()?"Active":"In-Active").append("\n");
            }


            String fileName ="C:\\Users\\huy.huynh\\dowloadProdect\\productreport.csv";

            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(fileContent.toString());
            fileWriter.flush();

            File file = new File(fileName);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);

            return responseEntity;
        }catch (Exception e)
        {
            return new ResponseEntity<>("error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            if(filewriter!=null)
                filewriter.close();
        }
        }

}
