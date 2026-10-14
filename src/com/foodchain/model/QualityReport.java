package com.foodchain.model;

public class QualityReport {
    private final String reportId;
    private String productId;
    private String reportDate;
    private String details;
    private boolean passed;

    public QualityReport(String reportId, String productId, String reportDate,
            String details, boolean passed) {
        this.reportId = reportId;
        this.productId = productId;
        this.reportDate = reportDate;
        this.details = details;
        this.passed = passed;
    }

    public String getReportId() { return reportId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
}
