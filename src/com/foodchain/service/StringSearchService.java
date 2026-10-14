package com.foodchain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.foodchain.dsa.strings.AhoCorasick;
import com.foodchain.dsa.strings.KMP;
import com.foodchain.dsa.strings.RabinKarp;
import com.foodchain.dsa.strings.ZFunction;
import com.foodchain.model.Product;
import com.foodchain.model.QualityReport;
import com.foodchain.model.Shipment;

/** Connects manual string algorithms to the current FoodChain records. */
public final class StringSearchService {
    private static final List<String> WARNING_KEYWORDS = Collections.unmodifiableList(Arrays.asList(
            "expired", "contaminated", "damaged", "spoiled", "temperature", "mold"));

    private final FoodChainData data;
    private final AhoCorasick warningMatcher = new AhoCorasick(WARNING_KEYWORDS);

    public StringSearchService(FoodChainData data) { this.data = data; }

    /** KMP scans product names and descriptions for a catalogue query. */
    public List<Product> searchProducts(String query) {
        String pattern = normalize(query);
        List<Product> matches = new ArrayList<Product>();
        if (pattern.length() == 0) return matches;
        for (Product product : data.getProducts()) {
            if (!KMP.search(normalize(product.getName()), pattern).isEmpty()
                    || !KMP.search(normalize(product.getDescription()), pattern).isEmpty()) {
                matches.add(product);
            }
        }
        return matches;
    }

    /** KMP searches quality-report text for one requested term. */
    public List<QualityReport> searchQualityReports(String query) {
        String pattern = normalize(query);
        List<QualityReport> matches = new ArrayList<QualityReport>();
        if (pattern.length() == 0) return matches;
        for (QualityReport report : data.getQualityReports()) {
            if (!KMP.search(normalize(report.getDetails()), pattern).isEmpty()) matches.add(report);
        }
        return matches;
    }

    /** Rabin-Karp matches a supplied fragment against product IDs. */
    public List<Product> searchProductIds(String fragment) {
        String pattern = normalizeCode(fragment);
        List<Product> matches = new ArrayList<Product>();
        if (pattern.length() == 0) return matches;
        for (Product product : data.getProducts()) {
            if (!RabinKarp.search(normalizeCode(product.getProductId()), pattern).isEmpty()) matches.add(product);
        }
        return matches;
    }

    /** Rabin-Karp matches a supplied fragment against shipment tracking codes. */
    public List<Shipment> searchTrackingCodes(String fragment) {
        String pattern = normalizeCode(fragment);
        List<Shipment> matches = new ArrayList<Shipment>();
        if (pattern.length() == 0) return matches;
        for (Shipment shipment : data.getShipments()) {
            if (!RabinKarp.search(normalizeCode(shipment.getTrackingCode()), pattern).isEmpty()) {
                matches.add(shipment);
            }
        }
        return matches;
    }

    /** Aho-Corasick checks all warning terms in each report in one scan per report. */
    public List<QualityWarning> scanQualityWarnings() {
        List<QualityWarning> warnings = new ArrayList<QualityWarning>();
        for (QualityReport report : data.getQualityReports()) {
            List<AhoCorasick.Match> matches = warningMatcher.search(report.getDetails());
            if (!matches.isEmpty()) warnings.add(new QualityWarning(report, matches));
        }
        return warnings;
    }

    /** Z search finds repeated phrases across descriptions to flag possible copy/reuse. */
    public List<Product> searchDescriptionPhrase(String phrase) {
        String pattern = normalize(phrase);
        List<Product> matches = new ArrayList<Product>();
        if (pattern.length() == 0) return matches;
        for (Product product : data.getProducts()) {
            if (!ZFunction.search(normalize(product.getDescription()), pattern).isEmpty()) matches.add(product);
        }
        return matches;
    }

    public static List<String> getWarningKeywords() { return WARNING_KEYWORDS; }

    private String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private String normalizeCode(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    }

    public static final class QualityWarning {
        private final QualityReport report;
        private final List<AhoCorasick.Match> matches;

        private QualityWarning(QualityReport report, List<AhoCorasick.Match> matches) {
            this.report = report;
            this.matches = matches;
        }

        public QualityReport getReport() { return report; }
        public List<AhoCorasick.Match> getMatches() { return Collections.unmodifiableList(matches); }
    }
}
