package org.example.shocintellij;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.example.shocintellij.commons.JamilaSON;
import org.example.shocintellij.commons.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class ChatGPTController {

    public static final String apiKey = "sk-proj-W8B5lUA5vZgj4QwcKQCsT3BlbkFJ0F5aX0Zuh91G1BT9hari";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @PostMapping("/api/gpt/translate")
    public ResponseEntity<String> getTranslationInLang(@RequestParam("lang")String lang, @org.springframework.web.bind.annotation.RequestBody String code)
    {
        String systemText="You are a highly appreciated and intelligent professor in Computer Science with a Master's in competitive programming that knows very well the "+lang+" language. Keep the answer short";
        String question="Your single job is to translate everything from this code into "+lang+". But make sure the code still work. Don't say anything besides the code, just say the code but translated in "+lang+". The code is: ";
        String ans=askChatGpt(systemText,question,code);
        if(ans==null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(ans);
    }
  //  @PostMapping("/api/gpt/complexity")

    @PostMapping("/api/gpt/testability")
        public ResponseEntity<String> getIsItTestable(@org.springframework.web.bind.annotation.RequestBody String code)
    {
        String systemText = "For this conversation, act like a senior software engineer/developer," +
                " with a master's degree in Computer Science and Engineering. Your job is to review the " +
                "code of others and to focus on one of these 4 aspects: 1. testability, 2. complexity, 3. " +
                "adding comments, 4. explaining the code. For the 4th, if the code implements a known algorithm " +
                "(ex. djikstra, kruskal, binary search) also mention the algorithm. For the comments, only add " +
                "them as text, do not include any code. In all cases, try to give a concise explanation. If you " +
                "do not detect the input as code, please indicate that by saying it is not a valid input. Keep the answer short";
        String question = "Check the testability of this code. Also try to include how much of the code can be covered by tests and the aspects that make it testable or the ones that can be improved. Try to be concise.";
        String ans = askChatGpt(systemText,question,code);
        if(ans == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(ans);
    }

    @PostMapping("/api/gpt/complexity")
    public ResponseEntity<String> complexity(@org.springframework.web.bind.annotation.RequestBody String code) throws FileNotFoundException {

        String systemText = "For this conversation, act like a senior software engineer/developer," +
                " with a master's degree in Computer Science and Engineering. Your job is to review the " +
                "code of others and to focus on one of these 4 aspects: 1. testability, 2. complexity, 3. " +
                "adding comments, 4. explaining the code. For the 4th, if the code implements a known algorithm " +
                "(ex. djikstra, kruskal, binary search) also mention the algorithm. For the comments, only add " +
                "them as text, do not include any code. In all cases, try to give a concise explanation. If you " +
                "do not detect the input as code, please indicate that by saying it is not a valid input. Keep the answer short";
        String question = "Calculate the complexity of the following code. Try to analyse it a bit and see if it can be further optimized. Keep the explanation concise. The complexity does not need to be fully broken down.";
        String ans=askChatGpt(systemText,question,code);
        if(ans==null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(ans);
    }

    @PostMapping("/api/gpt/codeExplanation")
    public ResponseEntity<String> getCodeExplanation(@org.springframework.web.bind.annotation.RequestBody String code)
    {
        String systemText = "For this conversation, act like a senior software engineer/developer," +
                " with a master's degree in Computer Science and Engineering. Keep the answer short";
        String question = "Describe what this code does. Try to analyse it step by step a bit. Keep the explanation concise. Do not write any code. If the code implements a specific known algorithm such as djikstra, kruskal or merge sort, also specify which one it implements. Keep the answer short";
        String ans = askChatGpt(systemText,question,code);
        if(ans == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(ans);
    }

    //@GetMapping("/api/gpt")
    public String askChatGpt(String systemText, String question,String code) {
        OkHttpClient client = new OkHttpClient();
        List<Role> roleList=new ArrayList<>();
        roleList.add(new Role("system",systemText));
        roleList.add(new Role("user",question+code));
        JamilaSON jamilaSON=new JamilaSON("gpt-4o",roleList,3000);
        ObjectMapper mapper = new ObjectMapper();
        String json="";
        RequestBody body;
        try {
            json = mapper.writeValueAsString(jamilaSON);
            System.out.println("Generated JSON:");
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer sk-proj-W8B5lUA5vZgj4QwcKQCsT3BlbkFJ0F5aX0Zuh91G1BT9hari")
                .post(body)
                .build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return null;//ResponseEntity.status(response.code()).body("Unexpected code " + response);
            }
            ObjectMapper mapper2 = new ObjectMapper();
            JsonNode rootnode = mapper2.readTree(response.body().string());
            return rootnode.path("choices").get(0).path("message").path("content").asText().toString();
        } catch (IOException e) {
            return null;//ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
