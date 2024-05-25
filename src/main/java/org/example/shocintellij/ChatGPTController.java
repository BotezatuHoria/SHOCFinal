package org.example.shocintellij;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ChatGPTController {
    public static final String apiKey = "sk-proj-W8B5lUA5vZgj4QwcKQCsT3BlbkFJ0F5aX0Zuh91G1BT9hari";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @GetMapping("/api/gpt")
    public ResponseEntity<String> getPOSTReq() {
        OkHttpClient client = new OkHttpClient();

        String json = "{\n" +
                "    \"model\": \"gpt-4\",\n" +
                "    \"messages\": [\n" +
                "        {\"role\": \"system\", \"content\": \"You are a highly appreciated and intelligent professor in Computer Science with a Master's in competitive programming.\"},\n" +
                "        {\"role\": \"user\", \"content\": \"Is this code testable? Explain what could be improved and color with green the parts that are easily testable, with yellow the potentially testable parts, and with red the parts that are hard to test. The code is\"}\n" +
                "    ],\n" +
                "    \"max_tokens\": 2000\n" +
                "}";

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();



        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return ResponseEntity.status(response.code()).body("Unexpected code " + response);
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootnode = mapper.readTree(response.body().string());
            return ResponseEntity.ok(rootnode.path("choices").get(0).path("message").path("content").toString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
