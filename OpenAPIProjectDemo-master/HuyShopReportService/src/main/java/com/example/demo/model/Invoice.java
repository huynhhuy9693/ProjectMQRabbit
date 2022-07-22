package com.example.demo.model;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private CartDTO cartDTO;
    private List<CartItemDTO> cartItemDTOList;

    public void writeTableHeader(PdfPTable table)
    {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(4);

        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("NAME",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("QUANTITY",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("UNIT-PRICE",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("TOTAL-PRICE",font));
        table.addCell(cell);

    }
    public void writeTableData(PdfPTable table)
    {

        for (CartItemDTO cartItem: cartItemDTOList) {
            table.addCell(String.valueOf(cartItem.getName()));
            table.addCell(String.valueOf(cartItem.getQuantity()));
            table.addCell(String.valueOf(cartItem.getPrice()));
            table.addCell(String.valueOf(cartItem.getQuantity()* cartItem.getPrice()));
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        Long totalPrice =0L;
        DecimalFormat formatter = new DecimalFormat("#,###.###");
        for (CartItemDTO cartItem: cartItemDTOList) {
            totalPrice+=cartItem.getPrice()* cartItem.getQuantity();
        }
        String totalPriceString = formatter.format(totalPrice);
        String discontPrice = formatter.format(cartDTO.getTotalPrice());
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Font fontNumber = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        fontNumber.setColor(Color.RED);
        fontNumber.setSize(20);
        Paragraph pOrderNumber = new Paragraph("Order_Number: " +cartDTO.getOderNumber(),fontNumber);
        pOrderNumber.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(pOrderNumber);
        Paragraph pName = new Paragraph("User_Name : " +cartDTO.getUserOrder(),font);
        pName.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(pName);
        Paragraph pAddress = new Paragraph("Address: " +cartDTO.getShippingAddress(),font);
        pAddress.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(pAddress);
        Paragraph pVoucher = new Paragraph("Voucher: " +cartDTO.getVoucher(),font);
        pVoucher.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(pVoucher);
        Paragraph pPayment = new Paragraph("Payment: " +cartDTO.getPayment(),font);
        pPayment.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(pPayment);

        Paragraph p = new Paragraph("-----INVOICE----- ",font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.5f, 3.5f, 3.0f,3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        Paragraph pTotal = new Paragraph("TOTAL-PRICE : "+totalPriceString,font);
        pTotal.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(pTotal);
        Paragraph pDiscount = new Paragraph("TOTAL-PRICE-DISCOUNT : "+discontPrice,font);
        pDiscount.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(pDiscount);
        Paragraph pFooter = new Paragraph("THANH YOU ",font);
        pFooter.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(pFooter);
        document.close();
    }
}
