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
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartReportExport {

    List<CartDTO> cartDTOS;
    String totalPrice;
    int month;
    int year;

    int dayStart;
    int dayLast;

    public CartReportExport(List<CartDTO> cartDTOS, String totalPrice) {
        this.cartDTOS = cartDTOS;
        this.totalPrice = totalPrice;
    }

    public void writeTableHeader(PdfPTable table)
    {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(7);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ORDER_ID",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("ORDER_NUMBER",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("TOTAL_PRICE",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("USER_ORDER",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("DATE_ORDER",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("VOUCHER",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("PAYMENT",font));
        table.addCell(cell);
    }
    public void writeTableData(PdfPTable table)
    {

        for (CartDTO cart: cartDTOS) {
            table.addCell(String.valueOf(cart.getId()));
            table.addCell(cart.getOderNumber());
            table.addCell(String.valueOf(cart.getTotalPrice()));
            table.addCell(cart.getUserOrder());
            table.addCell(String.valueOf(cart.getDateOrder()));
            table.addCell(cart.getVoucher());
            table.addCell(cart.getPayment());

        }
    }

    public void export(HttpServletResponse response) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph pDate = new Paragraph("DAY FROM : "+(dayStart!=0?dayStart:" ")
                +"- DAY TO " +(dayLast!=0?dayLast:" ")
                + ", MONTH : " + (month!=0? month:" ")
                +" , YEAR : "+(year!=0? year:" "),font);
        pDate.setAlignment(Paragraph.ALIGN_RIGHT);
        Paragraph p = new Paragraph("-----CART_REPORT----- ",font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(pDate);
        document.add(p);


        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f,3.0f,3.0f,3.0f,2.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        Paragraph pSum = new Paragraph("SUM-TOTAL-PRICE : "+totalPrice,font);
        pSum.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(pSum);
        document.close();
    }


}
