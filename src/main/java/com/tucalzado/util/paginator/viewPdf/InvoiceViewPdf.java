package com.tucalzado.util.paginator.viewPdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tucalzado.models.entity.Item;
import com.tucalzado.models.entity.Purchase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.awt.*;
import java.util.List;
import java.util.Map;

@Component("invoice/pdf")
public class InvoiceViewPdf extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        Purchase purchase = (Purchase) model.get("purchase");

        PdfPTable tablaT = new PdfPTable(1);
        tablaT.setSpacingAfter(40);
        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase("Detalles de Factura", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25)));
        cell.setBorder(0);
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tablaT.addCell(cell);
        document.add(tablaT);

        // Nueva tabla para los datos del emisor de la factura
        PdfPTable tablaEmisor = new PdfPTable(1);
        tablaEmisor.setWidthPercentage(100); // Ajustar el ancho de la tabla al 100%
        tablaEmisor.setSpacingAfter(20);

        // Datos de la Empresa Emisora
        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(8f);
        cell.setBackgroundColor(new Color(230, 230, 230));

        cell.addElement(new Paragraph("Datos de la empresa emisora", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.addElement(new Paragraph("Nombre empresa: tutienda"));
        cell.addElement(new Paragraph("Teléfono: 010-020-0340"));
        cell.addElement(new Paragraph("Email:  info@company.com"));
        cell.addElement(new Paragraph("Dirección de la Empresa : 123 Consectetur at ligula 10660"));

        tablaEmisor.addCell(cell);

        document.add(tablaEmisor);


        PdfPTable tabla = new PdfPTable(3); //  2 columnas para datos del cliente y factura
        tabla.setWidthPercentage(100); // Ajustar el ancho de la tabla al 100%
        tabla.setWidths(new float[]{8, 1, 8});

        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(8f);
        cell.setBackgroundColor(new Color(184, 218, 255));

        // Datos del Cliente
        cell.addElement(new Paragraph((new Phrase("Datos del Cliente", FontFactory.getFont(FontFactory.HELVETICA_BOLD)))));
        cell.addElement(new Paragraph("Nombre: " + purchase.getUser().getFirstname()));
        cell.addElement(new Paragraph("Apellido: " + purchase.getUser().getLastname()));
        cell.addElement(new Paragraph("Email: " + purchase.getUser().getEmail()));

        tabla.addCell(cell);

        // Celda vacía para el espacio
        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setColspan(1);
        tabla.addCell(cell);

        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(8f);
        cell.setBackgroundColor(new Color(195, 230, 203));

        // Datos de la Factura
        cell.addElement(new Paragraph(new Phrase("Datos de la factura", FontFactory.getFont(FontFactory.HELVETICA_BOLD))));
        cell.addElement(new Paragraph("Nº Factura: " + purchase.getId()));
        cell.addElement(new Paragraph("Descripción: " + "Factura de Compra"));
        cell.addElement(new Paragraph("Fecha: " + purchase.getDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        tabla.addCell(cell);

        tabla.setSpacingAfter(20);

        document.add(tabla);

        PdfPTable tabla3 = new PdfPTable(4);
        tabla3.setWidthPercentage(100); // Ajustar el ancho de la tabla al 100%
        tabla3.setWidths(new float[]{3.5f, 1, 1, 1});
        tabla3.setSpacingAfter(20);

        // Establecer el color de fondo de las celdas de los nombres de las columnas
        Color colorGris = new Color(192, 192, 192);
        cell = new PdfPCell(new Phrase("Producto"));
        cell.setBackgroundColor(colorGris);
        cell.setBorder(PdfPCell.NO_BORDER);
        tabla3.addCell(cell);

        cell = new PdfPCell(new Phrase("Precio"));
        cell.setBackgroundColor(colorGris);
        cell.setBorder(PdfPCell.NO_BORDER);
        tabla3.addCell(cell);

        cell = new PdfPCell(new Phrase("Cantidad"));
        cell.setBackgroundColor(colorGris);
        cell.setBorder(PdfPCell.NO_BORDER);
        tabla3.addCell(cell);

        cell = new PdfPCell(new Phrase("Total"));
        cell.setBackgroundColor(colorGris);
        cell.setBorder(PdfPCell.NO_BORDER);
        tabla3.addCell(cell);

        List<Item> items = purchase.getItems();
        for (Item item : items) {
            cell = new PdfPCell(new Phrase(item.getShoe().getName()));
            cell.setFixedHeight(30);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabla3.addCell(cell);

            cell = new PdfPCell(new Phrase(item.getShoe().getPrice() + "€"));
            cell.setFixedHeight(30);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabla3.addCell(cell);

            cell = new PdfPCell(new Phrase(item.getQuantity() + ""));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabla3.addCell(cell);

            cell = new PdfPCell(new Phrase(item.getTotalPrice() + "€"));
            cell.setFixedHeight(30);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabla3.addCell(cell);
        }

        // Añadir filas vacías si hay menos de 10 elementos
        int itemsCount = items.size();
        for (int i = itemsCount; i < 10; i++) {
            cell = new PdfPCell(new Phrase(" "));
            cell.setFixedHeight(30);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabla3.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setFixedHeight(30);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabla3.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setFixedHeight(30);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabla3.addCell(cell);
            cell = new PdfPCell(new Phrase(" "));
            cell.setFixedHeight(30);
            cell.setBorder(PdfPCell.NO_BORDER);
            tabla3.addCell(cell);
        }
        document.add(tabla3);

        PdfPTable tablaTotal = new PdfPTable(1);
        tablaTotal.setWidthPercentage(20);
        tablaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);

        cell = new PdfPCell(new Phrase("Total: " + purchase.getTotalPurchase() + "€", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setBackgroundColor(colorGris);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(PdfPCell.NO_BORDER);
        tablaTotal.addCell(cell);

        tablaTotal.setSpacingAfter(20);

        document.add(tablaTotal);
    }
}
