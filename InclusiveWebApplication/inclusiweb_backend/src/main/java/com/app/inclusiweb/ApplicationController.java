package com.app.inclusiweb;

import com.app.inclusiweb.model.Review;
import com.app.inclusiweb.model.ReviewResponse;
import com.app.inclusiweb.service.MyService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final MyService myService;

    public ApplicationController(){
        this.myService = new MyService();
    }

    public static List<String> splitString(String input, int wordsPerChunk) {
        List<String> result = new ArrayList<>();
        String[] words = input.split("\\s+");

        StringBuilder currentChunk = new StringBuilder();
        int wordCount = 0;

        for (String word : words) {
            currentChunk.append(word).append(" ");
            wordCount++;

            if (wordCount >= wordsPerChunk) {
                result.add(currentChunk.toString().trim());
                currentChunk = new StringBuilder();
                wordCount = 0;
            }
        }

        // Add the last chunk if it's not empty
        if (currentChunk.length() > 0) {
            result.add(currentChunk.toString().trim());
        }

        return result;
    }

    @PostMapping("/recommendation")
    ResponseEntity<String> getText(@RequestBody String url) throws IOException, InterruptedException {
        String accessibilityScore="";

        String filePath = "./reports/report.html";
        String command = "C:\\Users\\andre\\AppData\\Roaming\\npm\\lighthouse.cmd " + url + " --quiet --chrome-flags=\"--headless\" --output-path=\"" + filePath;
        Process p = Runtime.getRuntime().exec(command);
        int exitVal = p.waitFor();

        String targetString = "\"accessibility\",\"score\":";
        try {
            File f=new File(filePath);
            Scanner scanner = new Scanner(f);
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                int index = line.indexOf(targetString);
                if(index != -1){
                    accessibilityScore= line.substring(index + targetString.length(), index + targetString.length() + 4);
                    break;
                }
            }
            log.info("Accessibility score: " + accessibilityScore);
            scanner.close();
            if(f.delete()){
                log.info("File deleted successfully");
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        log.info("Exit Value of lighthouse: " + exitVal);
        String content = null;
        URLConnection connection = null;
        try {
            connection =  new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }

        log.info("Number of characters: " + content.length());
        int countWords = content.split("\\s").length;
//        content=content.replaceAll("\\s", "");
        List<String>listOfContents=splitString(content,300);


        log.info("Number of words: " + countWords);
        String basicRecommendations="";
        String layoutRecommendations="";
        String visibilityRecommendations="";
        String languageRecommendations="";
        String disabilitiesAffected="";
        String rating="";
        Integer ratingInt=0;
        Integer evaluated=0;

        int roundedScore=(int) Math.round(Double.parseDouble(accessibilityScore)*100.0);

        for(String s:listOfContents){
            System.out.println(String.valueOf(s.length())+" "+s.split("\\s").length);
            String myKey;
            OpenAiService service = new OpenAiService(myKey);
            ChatFunction chatFunction = ChatFunction.builder()
                    .name("review_code")
                    .description("review code and give a rating on a scale from 0 to 100, give some MANDATORY basic recommendations based on the look of the web page (field basicRecommendations),give some MANDATORY layout recommendations (layoutRecommendations), also give MANDATORY visual recommendations and if something has to be changed, MANDATORY language recommendations based on the text content and what MANDATORY disabilities, people that cannot use the page have")
                    .executor(Review.class, r -> new ReviewResponse(r.getRating(),r.getBasicRecommendations(),r.getLayoutRecommendations(),r.getVisibilityRecommendations(),r.getLanguageRecommendations(),r.getDisabilitiesAffected()))
                    .build();
            FunctionExecutor functionExecutor = new FunctionExecutor(List.of(chatFunction));
            List<ChatMessage> messages = new ArrayList<>();
            ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are an assistant that reviews accessibility features of web pages based on the given code. You have to write about the following aspects and pay attention to the following aspects and do not skip any of them, THEY ARE ALL MANDATORY FOR ENSURING A COMPREHENSIVE REVIEW: "+
                    "1. The rating of the page. The rating is MANDATORY on a scale from 0 to 100. The higher the rating, the better the accessibility. "+
                    "2. The basic recommendations. These are MANDATORY and given based on how the page looks and feels "+
                    "3. The layout recommendations. These are MANDATORY and given based on the layout of the page. "+
                    "4. The visibility recommendations. These MANDATORY and are given based on the visibility of the page. "+
                    "5. The language recommendations. These MANDATORY and are given based on the language and text of the page. "+
                    "6. The disabilities of people that cannot use the page because of them. These are MANDATORY and given based on the disabilities of people that cannot use the page. ");
            messages.add(systemMessage);
//            if(countWords<=1000){
                ChatMessage firstMsg = new ChatMessage(ChatMessageRole.USER.value(), s);

                messages.add(firstMsg);
                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                        .builder()
                        .model("gpt-3.5-turbo-0613")
                        .messages(messages)
                        .functions(functionExecutor.getFunctions())
                        .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("auto"))
                        .n(1)
                        .maxTokens(500)
                        .build();
            ChatMessage responseMessage = null;
                try{
                    responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
                }catch (Exception e){
                    e.printStackTrace();
                    responseMessage=new ChatMessage(ChatMessageRole.SYSTEM.value(),"");
                }

                messages.add(responseMessage);
                ChatFunctionCall functionCall = responseMessage.getFunctionCall();
//        functionCall.getArguments().forEach(x-> System.out.println(x));

            if(functionCall!=null){

                if(functionCall.getArguments().get("rating")==null){
                    rating= String.valueOf(roundedScore);
                }else{
                    rating = functionCall.getArguments().get("rating").asText();
                }
                if(!rating.equals("")){
                    ratingInt+=Integer.parseInt(rating);
                    evaluated++;
                }
                if(functionCall.getArguments().get("basicRecommendations")!=null){
                    basicRecommendations += functionCall.getArguments().get("basicRecommendations").asText();
                }
                if(functionCall.getArguments().get("layoutRecommendations")!=null){
                    layoutRecommendations += functionCall.getArguments().get("layoutRecommendations").asText();
                }
                if(functionCall.getArguments().get("visibilityRecommendations")!=null){
                    visibilityRecommendations += functionCall.getArguments().get("visibilityRecommendations").asText();
                }
                if(functionCall.getArguments().get("languageRecommendations")!=null){
                    languageRecommendations += functionCall.getArguments().get("languageRecommendations").asText();
                }
                if(functionCall.getArguments().get("disabilitiesAffected")!=null){
                    disabilitiesAffected += functionCall.getArguments().get("disabilitiesAffected").asText();
                }

            }




//            }
//            else{
//                return ResponseEntity.ok("The code is too long");
//            }
            Thread.sleep(2000);
        }
        JSONObject json = new JSONObject();
        int val= (ratingInt/evaluated);
        json.put("rating", Integer.toString(val));
        json.put("basicRecommendations", basicRecommendations);
        json.put("layoutRecommendations", layoutRecommendations);
        json.put("visibilityRecommendations", visibilityRecommendations);
        json.put("languageRecommendations", languageRecommendations);
        json.put("disabilitiesAffected", disabilitiesAffected);
        json.put("accessibilityScore", String.valueOf(roundedScore));
        return ResponseEntity.ok(json.toString());
//        return ResponseEntity.ok("");

    }
}