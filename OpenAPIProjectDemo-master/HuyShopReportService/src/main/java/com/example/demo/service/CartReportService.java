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
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
        NumberFormat formatter = new DecimalFormat("#0.00");
        List<CartReport> cartReportList = cartFeignClient.findAllCart();
        Double totalPrice = Double.valueOf(formatter.format(cartFeignClient.sumTotalPrice()));

        CartReportExport cartReportExport = new CartReportExport(cartReportList,totalPrice);
        cartReportExport.export(response);
    }

}
