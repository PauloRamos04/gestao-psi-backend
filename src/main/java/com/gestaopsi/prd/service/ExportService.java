package com.gestaopsi.prd.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportService {

    private static final com.itextpdf.text.Font TITLE_FONT = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
    private static final com.itextpdf.text.Font HEADER_FONT = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
    private static final com.itextpdf.text.Font NORMAL_FONT = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);

    /**
     * Exporta dados para PDF
     */
    public byte[] exportToPDF(String title, List<String> headers, List<List<String>> rows) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Paragraph titleParagraph = new Paragraph(title, TITLE_FONT);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleParagraph.setSpacingAfter(20);
            document.add(titleParagraph);

            // Data de geração
            Paragraph dateParagraph = new Paragraph(
                "Gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                NORMAL_FONT
            );
            dateParagraph.setSpacingAfter(20);
            document.add(dateParagraph);

            // Tabela
            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);

            // Cabeçalho
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Linhas
            for (List<String> row : rows) {
                for (String cell : row) {
                    PdfPCell pdfCell = new PdfPCell(new Phrase(cell != null ? cell : "", NORMAL_FONT));
                    pdfCell.setPadding(5);
                    table.addCell(pdfCell);
                }
            }

            document.add(table);

            // Rodapé
            Paragraph footer = new Paragraph(
                "GestaoPsi - Sistema de Gestão para Clínicas de Psicologia",
                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8, com.itextpdf.text.Font.ITALIC)
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

            document.close();
            
            log.info("PDF gerado com sucesso: {} linhas", rows.size());
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            log.error("Erro ao gerar PDF", e);
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    /**
     * Exporta dados para Excel
     */
    public byte[] exportToExcel(String sheetName, List<String> headers, List<List<String>> rows) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);

            // Estilo do cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Estilo das células
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            // Linhas de dados
            int rowNum = 1;
            for (List<String> rowData : rows) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.size(); i++) {
                    Cell cell = row.createCell(i);
                    String value = rowData.get(i);
                    if (value != null) {
                        // Tenta converter para número
                        try {
                            double numValue = Double.parseDouble(value);
                            cell.setCellValue(numValue);
                        } catch (NumberFormatException e) {
                            cell.setCellValue(value);
                        }
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            // Ajusta largura das colunas
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            
            log.info("Excel gerado com sucesso: {} linhas", rows.size());
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Erro ao gerar Excel", e);
            throw new RuntimeException("Erro ao gerar Excel", e);
        }
    }

    /**
     * Exporta relatório de sessões para PDF
     */
    public byte[] exportSessoesToPDF(List<Map<String, Object>> sessoes) {
        List<String> headers = List.of("Data", "Horário", "Paciente", "Psicólogo", "Status", "Valor");
        List<List<String>> rows = sessoes.stream()
            .map(sessao -> List.of(
                String.valueOf(sessao.get("data")),
                String.valueOf(sessao.get("horario")),
                String.valueOf(sessao.get("paciente")),
                String.valueOf(sessao.get("psicologo")),
                String.valueOf(sessao.get("status")),
                String.valueOf(sessao.get("valor"))
            ))
            .toList();

        return exportToPDF("Relatório de Sessões", headers, rows);
    }

    /**
     * Exporta relatório de pagamentos para Excel
     */
    public byte[] exportPagamentosToExcel(List<Map<String, Object>> pagamentos) {
        List<String> headers = List.of("Data", "Paciente", "Valor", "Forma Pagamento", "Status");
        List<List<String>> rows = pagamentos.stream()
            .map(pag -> List.of(
                String.valueOf(pag.get("data")),
                String.valueOf(pag.get("paciente")),
                String.valueOf(pag.get("valor")),
                String.valueOf(pag.get("formaPagamento")),
                String.valueOf(pag.get("status"))
            ))
            .toList();

        return exportToExcel("Pagamentos", headers, rows);
    }
}

