package com.tr.nebula.report;

import java.io.IOException;

import org.jfree.report.JFreeReport;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.TableDataFactory;
import org.jfree.report.modules.output.csv.CSVDataReportUtil;
import org.jfree.report.modules.output.pageable.pdf.PdfReportUtil;
import org.jfree.report.modules.output.pageable.plaintext.PlainTextReportUtil;
import org.jfree.report.modules.output.table.rtf.RTFReportUtil;
import org.jfree.report.modules.output.table.xls.ExcelReportUtil;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.pentaho.reporting.libraries.xmlns.parser.ParseException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.swing.table.TableModel;

/**
 * Uses Pentaho Reporting and Spring to generate a report.
 * 
 * <p>An instance of this class is instantiated and configured via Spring. Note that this class uses Spring 
 * annotations.</p>
 * 
 * @author mlowery
 */
@Service
public class ReportUtil {

  private Resource reportResource;

  private ReportGenerator reportGenerator=ReportGenerator.createInstance();

//  @Autowired
//  ReportConfiguration configuration=new ReportConfiguration();

//  private String inputPath=configuration.getInputPath();
//
//  private String outputPath=configuration.getOutputPath();
  private static String inputPath;
  private static String outputPath;
  private TableDataFactory tableDataFactory;
  private String ext;


/* Excel */
  public void reportExcel(TableModel reportData,String inputFile,String outputFileName) throws ParseException, IOException, ReportProcessingException {

    ext=".xls";
    tableDataFactory=new TableDataFactory("default",reportData);
//      tableDataFactory.addTable("data",reportData);
    JFreeReport report = new JFreeReport();
    try {
      // Load and parse the report
      report = reportGenerator.parseReport(inputPath+inputFile);
    } catch (Exception e) {
      throw new ParseException("Failed to parse the report", e);
    }

    // Set the source of data for the report
    report.setDataFactory(tableDataFactory);
    // Create the report and export to the supplied output filename
    ExcelReportUtil.createXLS(report, outputPath+outputFileName+ext);
//    final PreviewDialog dialog = new PreviewDialog(report);
//    dialog.pack();
//    dialog.setVisible(true);
  }
//  /* Pdf */
  public void reportPdf(TableModel reportData,String inputFile,String outputFileName) throws ParseException, IOException, ReportProcessingException {

    ext=".pdf";
    tableDataFactory=new TableDataFactory("default",reportData);
    JFreeReport report = new JFreeReport();
    try {
      // Load and parse the report
      report = reportGenerator.parseReport(inputPath+inputFile);
    } catch (Exception e) {
      throw new ParseException("Failed to parse the report", e);
    }

    // Set the source of data for the report
    report.setDataFactory(tableDataFactory);
    // Create the report and export to the supplied output filename
    PdfReportUtil.createPDF(report, outputPath+outputFileName+ext);

  }
  /* CSV */
  public void reportCsv(TableModel reportData,String inputFile,String outputFileName) throws ParseException, IOException, ReportProcessingException {

    ext=".csv";
    tableDataFactory=new TableDataFactory("default",reportData);
    JFreeReport report = new JFreeReport();
    try {
      // Load and parse the report
      report = reportGenerator.parseReport(inputPath+inputFile);
    } catch (Exception e) {
      throw new ParseException("Failed to parse the report", e);
    }

    // Set the source of data for the report
    report.setDataFactory(tableDataFactory);
    // Create the report and export to the supplied output filename
    CSVDataReportUtil.createCSV(report, outputPath+outputFileName+ext);

  }

  /* Html */
//  public void reportHtml(TableModel reportData,String inputFile,String outputFileName) throws ParseException, IOException, ReportProcessingException {
//
//    ext=".html";
//    tableDataFactory.addTable("default",reportData);
//    JFreeReport report = null;
//    try {
//      // Load and parse the report
//      report = reportGenerator.parseReport(inputPath+inputFile);
//    } catch (Exception e) {
//      throw new ParseException("Failed to parse the report", e);
//    }
//
//    // Set the source of data for the report
//    report.setDataFactory(tableDataFactory);
//    // Create the report and export to the supplied output filename
//    HtmlReportUtil.createZIPHTML(report, outputPath+outputFileName+ext);
////    final PreviewDialog dialog = new PreviewDialog(report);
////    dialog.pack();
////    dialog.setVisible(true);
//  }
  /* Text */
  public void reportTxt(TableModel reportData,String inputFile,String outputFileName) throws ParseException, IOException, ReportProcessingException {

    ext=".txt";
    tableDataFactory=new TableDataFactory("default",reportData);
    JFreeReport report = new JFreeReport();
    try {
      // Load and parse the report
      report = reportGenerator.parseReport(inputPath+inputFile);
    } catch (Exception e) {
      throw new ParseException("Failed to parse the report", e);
    }

    // Set the source of data for the report
    report.setDataFactory(tableDataFactory);
    // Create the report and export to the supplied output filename
    PlainTextReportUtil.createPlainText(report, outputPath+outputFileName+ext);

  }
  /* Rtf */
  public void reportRtf(TableModel reportData,String inputFile,String outputFileName) throws ParseException, IOException, ReportProcessingException {

    ext=".rtf";
    tableDataFactory=new TableDataFactory("default",reportData);
    JFreeReport report = new JFreeReport();
    try {
      // Load and parse the report
      report = reportGenerator.parseReport(inputPath+inputFile);
    } catch (Exception e) {
      throw new ParseException("Failed to parse the report", e);
    }

    // Set the source of data for the report
    report.setDataFactory(tableDataFactory);
    // Create the report and export to the supplied output filename
    RTFReportUtil.createRTF(report, outputPath+outputFileName+ext);

  }


  public void setReportResource(Resource reportResource) {
    this.reportResource = reportResource;
  }


  public void setReportGenerator(ReportGenerator reportGenerator) {
    this.reportGenerator = reportGenerator;
  }




  public void setTableDataFactory(TableDataFactory tableDataFactory) {
    this.tableDataFactory = tableDataFactory;
  }

  public static String getInputPath() {
    return inputPath;
  }

  public static void setInputPath(String inputPath) {
    ReportUtil.inputPath = inputPath;
  }

  public static String getOutputPath() {
    return outputPath;
  }

  public static void setOutputPath(String outputPath) {
    ReportUtil.outputPath = outputPath;
  }
}