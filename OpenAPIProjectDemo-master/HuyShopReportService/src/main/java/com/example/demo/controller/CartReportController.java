package com.example.demo.controller;


import com.example.demo.model.CartReport;
import com.example.demo.service.CartFeignClient;
import com.example.demo.model.CartReportExport;
import com.example.demo.service.CartReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/report")
public class CartReportController {



    @Autowired
    CartFeignClient cartFeignClient;
    @Autowired
    CartReportService cartReportService;

    @GetMapping("/cart/export/pdf")
    public void showReport(HttpServletResponse response) throws IOException {
        cartReportService.exPortToPDF(response);
        new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/cart/export/pdf/{startDate}/{lastDate}")
    public void showReport(HttpServletResponse response,
                           @PathVariable("startDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                           @PathVariable("lastDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate)
            throws IOException {
        cartReportService.exPortToPDFBetween(response, startDate, lastDate);
        new ResponseEntity<>(HttpStatus.OK);
    }
}
