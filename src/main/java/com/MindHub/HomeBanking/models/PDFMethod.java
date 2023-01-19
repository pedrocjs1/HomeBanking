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

    public static void createPDF (Set<Transaction> transactionArray) throws Exception {
        var doc = new Document();
        String route = System.getProperty("user.home");
        URL url = PDFMethod.class.getClassLoader().getResource("Downloads/TransactionInfo.pdf");
        File file = new File(url.getFile());
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();
        ClassLoader classLoader = PDFMethod.class.getClassLoader();
        var bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        try
        {
            Image foto = Image.getInstance(classLoader.getResource("web/assets/img/logoWhite.png").getPath());
            foto.scaleToFit(100, 100);
            foto.setAlignment(Chunk.ALIGN_MIDDLE);
            doc.add(foto);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        var paragraph = new Paragraph( "Your transactions", bold);


        var table = new PdfPTable(4);
        Stream.of("Amount", "Description", "Date", "Type").forEach(table::addCell);

        transactionArray.forEach(transaction -> {
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getDate()));
            table.addCell(String.valueOf(transaction.getType()));
        });

        doc.add(paragraph);
        doc.add(table);
        doc.close();
    }
}
