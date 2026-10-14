package com.foodchain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.foodchain.dsa.suffix.KasaiLcp;
import com.foodchain.dsa.suffix.SuffixArray;
import com.foodchain.dsa.suffix.SuffixAutomaton;
import com.foodchain.model.Product;
import com.foodchain.model.QualityReport;

/** Builds a searchable suffix index across FoodChain product and quality documents. */
public final class DocumentSimilarityService {
    private static final char DOCUMENT_SEPARATOR = '\u0001';

    private final FoodChainData data;
    private List<Document> documents = Collections.emptyList();
    private String corpus = "";
    private int[] suffixArray = new int[0];
    private int[] lcp = new int[0];
    private int[] documentAtPosition = new int[0];
    private int[] remainingInDocument = new int[0];
    private SuffixAutomaton automaton = new SuffixAutomaton("");

    public DocumentSimilarityService(FoodChainData data) {
        if (data == null) throw new IllegalArgumentException("FoodChain data is required.");
        this.data = data;
        rebuildIndex();
    }

    /** Re-indexes current product descriptions and quality-report details. */
    public void rebuildIndex() {
        List<Document> currentDocuments = new ArrayList<Document>();
        for (Product product : data.getProducts()) {
            String body = safe(product.getName()) + " " + safe(product.getDescription());
            currentDocuments.add(new Document("Product", product.getProductId(), normalize(body)));
        }
        for (QualityReport report : data.getQualityReports()) {
            String title = report.getReportId() + " (product " + report.getProductId() + ")";
            currentDocuments.add(new Document("Quality report", title,
                    normalize(safe(report.getDetails()))));
        }

        StringBuilder text = new StringBuilder();
        for (Document document : currentDocuments) {
            text.append(document.text).append(DOCUMENT_SEPARATOR);
        }
        String newCorpus = text.toString();
        int[] owner = new int[newCorpus.length()];
        int[] remaining = new int[newCorpus.length()];
        int offset = 0;
        for (int documentIndex = 0; documentIndex < currentDocuments.size(); documentIndex++) {
            String body = currentDocuments.get(documentIndex).text;
            for (int i = 0; i < body.length(); i++) {
                owner[offset + i] = documentIndex;
                remaining[offset + i] = body.length() - i;
            }
            offset += body.length();
            owner[offset] = -1;
            remaining[offset] = 0;
            offset++;
        }

        documents = Collections.unmodifiableList(currentDocuments);
        corpus = newCorpus;
        documentAtPosition = owner;
        remainingInDocument = remaining;
        suffixArray = SuffixArray.build(corpus);
        lcp = KasaiLcp.build(corpus, suffixArray);
        automaton = new SuffixAutomaton(corpus);
    }

    public int getIndexedDocumentCount() { return documents.size(); }

    public List<Document> getDocuments() { return documents; }

    /** Uses a suffix-array range search to return each matching document once. */
    public List<Document> searchDocuments(String query) {
        String pattern = normalize(query);
        if (pattern.length() == 0 || pattern.indexOf(DOCUMENT_SEPARATOR) >= 0) {
            return Collections.emptyList();
        }
        int low = lowerBound(pattern);
        Set<Integer> matchingDocuments = new LinkedHashSet<Integer>();
        for (int order = low; order < suffixArray.length; order++) {
            int position = suffixArray[order];
            if (compareSuffixWithPattern(position, pattern) != 0) break;
            if (remainingInDocument[position] >= pattern.length()) {
                matchingDocuments.add(documentAtPosition[position]);
            }
        }
        List<Document> results = new ArrayList<Document>();
        for (Integer index : matchingDocuments) results.add(documents.get(index.intValue()));
        return results;
    }

    /** Uses the suffix automaton to answer whether any indexed document has a phrase. */
    public boolean containsPhrase(String query) {
        String pattern = normalize(query);
        return pattern.length() > 0 && pattern.indexOf(DOCUMENT_SEPARATOR) < 0
                && automaton.contains(pattern);
    }

    /** Returns a longest repeated fragment, capped so it cannot cross a document boundary. */
    public RepeatedFragment findLongestRepeatedFragment() {
        int bestLength = 0;
        int bestLeft = -1;
        int bestRight = -1;
        for (int order = 1; order < suffixArray.length; order++) {
            int left = suffixArray[order - 1];
            int right = suffixArray[order];
            int length = Math.min(lcp[order],
                    Math.min(remainingInDocument[left], remainingInDocument[right]));
            if (length > bestLength) {
                bestLength = length;
                bestLeft = left;
                bestRight = right;
            }
        }
        if (bestLength == 0) return null;

        String fragment = corpus.substring(bestLeft, bestLeft + bestLength).trim();
        if (fragment.length() == 0) return null;
        Document first = documents.get(documentAtPosition[bestLeft]);
        Document second = documents.get(documentAtPosition[bestRight]);
        return new RepeatedFragment(fragment, first, second, bestLength);
    }

    private int lowerBound(String pattern) {
        int low = 0;
        int high = suffixArray.length;
        while (low < high) {
            int middle = low + (high - low) / 2;
            if (compareSuffixWithPattern(suffixArray[middle], pattern) < 0) low = middle + 1;
            else high = middle;
        }
        return low;
    }

    private int compareSuffixWithPattern(int suffixStart, String pattern) {
        for (int i = 0; i < pattern.length(); i++) {
            if (i >= corpus.length() - suffixStart) return -1;
            char textCharacter = corpus.charAt(suffixStart + i);
            char patternCharacter = pattern.charAt(i);
            if (textCharacter != patternCharacter) {
                return textCharacter < patternCharacter ? -1 : 1;
            }
        }
        return 0;
    }

    private static String safe(String value) { return value == null ? "" : value; }

    private static String normalize(String value) {
        if (value == null) return "";
        return value.replace(DOCUMENT_SEPARATOR, ' ').toLowerCase(Locale.ROOT);
    }

    public static final class Document {
        private final String kind;
        private final String title;
        private final String text;

        private Document(String kind, String title, String text) {
            this.kind = kind;
            this.title = title;
            this.text = text;
        }

        public String getKind() { return kind; }
        public String getTitle() { return title; }
        public String getText() { return text; }
    }

    public static final class RepeatedFragment {
        private final String text;
        private final Document firstDocument;
        private final Document secondDocument;
        private final int indexedLength;

        private RepeatedFragment(String text, Document firstDocument,
                Document secondDocument, int indexedLength) {
            this.text = text;
            this.firstDocument = firstDocument;
            this.secondDocument = secondDocument;
            this.indexedLength = indexedLength;
        }

        public String getText() { return text; }
        public Document getFirstDocument() { return firstDocument; }
        public Document getSecondDocument() { return secondDocument; }
        public int getIndexedLength() { return indexedLength; }
    }
}
