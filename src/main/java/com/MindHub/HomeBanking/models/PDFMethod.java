package com.MindHub.HomeBanking.models;



import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import java.util.stream.Stream;

public class PDFMethod {

    public PDFMethod() {
    }

    public static void createPDF (Set<Transaction> transactionArray,  Client client) throws Exception {
        Font titleFont = new Font(Font.FontFamily.COURIER,20);
        Font subFont = new Font(Font.FontFamily.HELVETICA,11);
        Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);


        try
        {
            Document doc = new Document(PageSize.A4);
            String route = System.getProperty("user.home");
            URL url = PDFMethod.class.getClassLoader().getResource("Downloads/TransactionInfo.pdf");
            File file = new File(url.getFile());
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();
            ClassLoader classLoader = PDFMethod.class.getClassLoader();


            Image img = Image.getInstance("src/main/resources/static/web/images/logoWhite.png");
            img.scaleAbsoluteWidth(120);
            img.scaleAbsoluteHeight(70);
            img.setAlignment(Element.ALIGN_CENTER);


            Paragraph title = new Paragraph( "Your transactions", titleFont );
            title.setSpacingAfter(3);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(-2);

            Paragraph subTitle = new Paragraph("Client: " + client.getFullname(),subFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            subTitle.setSpacingAfter(1);

            var table = new PdfPTable(4);
            Stream.of("Amount", "Description", "Date", "Type").forEach(table::addCell);

            transactionArray.forEach(transaction -> {
                table.addCell(String.valueOf(transaction.getAmount()));
                table.addCell(transaction.getDescription());
                table.addCell(String.valueOf(transaction.getDate()));
                table.addCell(String.valueOf(transaction.getType()));
            });
            doc.add(img);
            doc.add(title);
            doc.add(subTitle);
            doc.add(table);
            doc.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }



    }
}