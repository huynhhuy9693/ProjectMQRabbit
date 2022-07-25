package com.example.demo.service;

import com.example.demo.model.CartDTO;
import com.example.demo.model.CartItemDTO;
import com.example.demo.model.CartReportExport;
import com.example.demo.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;
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
    @Autowired
    ProductReportService productReportService;

    public void exPortToPDF(HttpServletResponse response) throws IOException {
        System.out.println("report");

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = " Content-Disposition";
        String headerValue = "attachment; filename=cart-report_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);
        NumberFormat formatter = new DecimalFormat("###,###.###");
        List<CartDTO> cartDTOList = cartFeignClient.findAllCart();
        String totalPrice =formatter.format(cartFeignClient.sumTotalPrice()) ;
        CartReportExport cartReportExport = new CartReportExport(cartDTOList,totalPrice);
        cartReportExport.export(response);
    }

    public void exPortToPDFBetween(HttpServletResponse response, LocalDate startDate, LocalDate lastDate) throws IOException {


        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = " Content-Disposition";
        String headerValue = "attachment; filename=cart-report_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);
        DecimalFormat formatter = new DecimalFormat("#,###.###");
        List<CartDTO> cartDTOList = cartFeignClient.findByDateOrderBetween(startDate,lastDate);
        String totalPrice = formatter.format(cartFeignClient.sumTotalPriceBetween(startDate,lastDate));
        int month = startDate.getMonth().getValue();
        int year = startDate.getYear();
        int dayFrom = startDate.getDayOfMonth();
        int dayTo = lastDate.getDayOfMonth();
        CartReportExport cartReportExport = new CartReportExport(cartDTOList,totalPrice,month,year,dayFrom,dayTo);
        cartReportExport.export(response);
    }

    public void exPortInvoice(HttpServletResponse response, String oderNumber,String token) throws IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = " Content-Disposition";
        String headerValue = "attachment; filename=invoice_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);
        NumberFormat formatter = new DecimalFormat("#,###.###");
        CartDTO cartDTO = cartFeignClient.findByOderNumber(oderNumber, token);
        List<CartItemDTO> cartItemDTOList = cartFeignClient.findItemByOrderNumber(oderNumber,token);
        for (CartItemDTO cartItem: cartItemDTOList) {
            cartItem.setName(productReportService.findById(cartItem.getProductId()).getName());
        }
        Invoice invoice = new Invoice(cartDTO, cartItemDTOList);
        invoice.export(response);

    }

}
