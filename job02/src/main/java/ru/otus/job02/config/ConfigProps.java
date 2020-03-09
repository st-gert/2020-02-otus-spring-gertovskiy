package ru.otus.job02.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@PropertySource("classpath:application.properties")
public class ConfigProps {

    @Value("${locale:en_US}")
    private Locale locale;

    @Value("${exam.file.name}")
    private String fileName;
    @Value("${exam.file.charset}")
    private String fileCharset;
    @Value("${exam.file.separator}")
    private char fileSeparator;

    @Value("${criteria.excel}")
    private int criteriaExcel;
    @Value("${criteria.good}")
    private int criteriaGood;
    @Value("${criteria.satisf}")
    private int criteriaSatisfl;

    // generated getters & setters
    public Locale getLocale() {
        return locale;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileCharset() {
        return fileCharset;
    }
    public void setFileCharset(String fileCharset) {
        this.fileCharset = fileCharset;
    }
    public char getFileSeparator() {
        return fileSeparator;
    }
    public void setFileSeparator(char fileSeparator) {
        this.fileSeparator = fileSeparator;
    }
    public int getCriteriaExcel() {
        return criteriaExcel;
    }
    public void setCriteriaExcel(int criteriaExcel) {
        this.criteriaExcel = criteriaExcel;
    }
    public int getCriteriaGood() {
        return criteriaGood;
    }
    public void setCriteriaGood(int criteriaGood) {
        this.criteriaGood = criteriaGood;
    }
    public int getCriteriaSatisfl() {
        return criteriaSatisfl;
    }
    public void setCriteriaSatisfl(int criteriaSatisfl) {
        this.criteriaSatisfl = criteriaSatisfl;
    }
}
