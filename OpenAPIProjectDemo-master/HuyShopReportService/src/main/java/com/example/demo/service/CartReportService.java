package com.example.demo.service;

import com.example.demo.model.CartReport;
import com.example.demo.model.CartReportExport;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class CartReportService {

    @Autowired
    CartFeignClient cartFeignClient;

    public void exPortToPDF(HttpServletResponse response) throws IOException {
        System.out.println("report");

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = " Content-Disposition";
        String headerValue = "attachment; filename=cart-report_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);
        NumberFormat formatter = new DecimalFormat("###,###.###");
        List<CartReport> cartReportList = cartFeignClient.findAllCart();
        String totalPrice =formatter.format(cartFeignClient.sumTotalPrice()) ;
        CartReportExport cartReportExport = new CartReportExport(cartReportList,totalPrice);
        cartReportExport.export(response);
    }

    public void exPortToPDFBetween(HttpServletResponse response, LocalDate startDate, LocalDate lastDate) throws IOException {


        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = " Content-Disposition";
        String headerValue = "attachment; filename=cart-report_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);
        DecimalFormat formatter = new DecimalFormat("###,###.###");
        List<CartReport> cartReportList = cartFeignClient.findByDateOrderBetween(startDate,lastDate);
        String totalPrice = formatter.format(cartFeignClient.sumTotalPriceBetween(startDate,lastDate));
        int month = startDate.getMonth().getValue();
        int year = startDate.getYear();
        int dayFrom = startDate.getDayOfMonth();
        int dayTo = lastDate.getDayOfMonth();
        CartReportExport cartReportExport = new CartReportExport(cartReportList,totalPrice,month,year,dayFrom,dayTo);
        cartReportExport.export(response);
    }

}
