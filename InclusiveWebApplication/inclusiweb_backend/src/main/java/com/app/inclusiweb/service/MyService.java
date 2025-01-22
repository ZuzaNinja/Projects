package com.app.inclusiweb.service;

import com.app.inclusiweb.model.Review;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;

import java.util.List;

public class MyService {
    private final String myKey;
    private final OpenAiService openAiService;

    public MyService(){
        openAiService = new OpenAiService(myKey);
    }

    public FunctionExecutor buildFunctionExecutor(List<ChatFunction> functionList){
        return new FunctionExecutor(functionList);
    }

}
