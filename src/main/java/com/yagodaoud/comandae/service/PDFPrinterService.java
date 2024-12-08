package com.yagodaoud.comandae.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.stereotype.Service;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

@Service
public class PDFPrinterService {

    public void printPDF(String pdfPath) throws PrinterException, IOException {
        PDDocument pdf = Loader.loadPDF(new File(pdfPath));
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(pdf));
        job.print();
    }
}