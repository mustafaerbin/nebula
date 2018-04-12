package com.tr.nebula.report;

import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Mustafa Erbin on 18.05.2017.
 */
@Service
public class ReportConfiguration implements Serializable {

    private String inputPath;

    private String outputPath;



    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
