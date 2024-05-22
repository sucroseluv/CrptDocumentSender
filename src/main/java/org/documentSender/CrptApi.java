package org.documentSender;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private HttpClient client;
    private TimeUnit concurrentUnit;
    private Semaphore semaphore;

    public CrptApi(TimeUnit concurrentUnit, int requestLimit) {
        this.concurrentUnit = concurrentUnit;
        this.semaphore = new Semaphore(requestLimit);
        client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    }
    public HttpResponse<String> sendDocument(Document document, String sign) throws IOException, InterruptedException {
        semaphore.acquire();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                .POST(HttpRequest.BodyPublishers.ofString(document.toJson()))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        CompletableFuture.delayedExecutor(1, this.concurrentUnit).execute(() -> semaphore.release());
        return response;
    }

    @JsonAutoDetect
    public static class Document {
        public Description description;
        public String doc_id;
        public String doc_status;
        public String doc_type;
        public boolean importRequest;
        public String owner_inn;
        public String participant_inn;
        public String producer_inn;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        public Date production_date;
        public String production_type;
        public ArrayList<Product> products;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        public Date reg_date;
        public String reg_number;

        @JsonAutoDetect
        public static class Description{
            public String participantInn;
        }
        @JsonAutoDetect
        public static class Product{
            public String certificate_document;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            public Date certificate_document_date;
            public String certificate_document_number;
            public String owner_inn;
            public String producer_inn;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            public Date production_date;
            public String tnved_code;
            public String uit_code;
            public String uitu_code;
        }

        static Document fromJson(String json) throws IOException {
            StringReader reader = new StringReader(json);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(reader, Document.class);
        }
        String toJson() throws IOException {
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, this);
            return writer.toString();
        }
    }
}
