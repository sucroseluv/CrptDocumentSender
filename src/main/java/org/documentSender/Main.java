package org.documentSender;

import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 1);
        try {
            CrptApi.Document document = CrptApi.Document.fromJson(documentStringExample);
            HttpResponse<String> response = crptApi.sendDocument(document, "SIGN_CODE");
            System.out.println(response.body());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    static String documentStringExample = "{\n" +
            "    \"description\": {\n" +
            "        \"participantInn\": \"string\"\n" +
            "    },\n" +
            "    \"doc_id\": \"string\",\n" +
            "    \"doc_status\": \"string\",\n" +
            "    \"doc_type\": \"LP_INTRODUCE_GOODS\",\n" +
            "    \"importRequest\": true,\n" +
            "    \"owner_inn\": \"string\",\n" +
            "    \"participant_inn\": \"string\",\n" +
            "    \"producer_inn\": \"string\",\n" +
            "    \"production_date\": \"2020-01-23\",\n" +
            "    \"production_type\": \"string\",\n" +
            "    \"products\": [\n" +
            "        {\n" +
            "            \"certificate_document\": \"string\",\n" +
            "            \"certificate_document_date\": \"2020-01-23\",\n" +
            "            \"certificate_document_number\": \"string\",\n" +
            "            \"owner_inn\": \"string\",\n" +
            "            \"producer_inn\": \"string\",\n" +
            "            \"production_date\": \"2020-01-23\",\n" +
            "            \"tnved_code\": \"string\",\n" +
            "            \"uit_code\": \"string\",\n" +
            "            \"uitu_code\": \"string\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"reg_date\": \"2020-01-23\",\n" +
            "    \"reg_number\": \"string\"\n" +
            "}";
}