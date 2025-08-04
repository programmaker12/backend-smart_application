package com.project.smart_application;


import com.project.smart_application.service.ChatService;
import com.project.smart_application.service.ImageService;
import com.project.smart_application.service.RecipeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class GenAIController {
    ChatService chatService;
    ImageService imageService;
    RecipeService recipeService;
    public GenAIController(ChatService chatService, ImageService imageService, RecipeService recipeService) {

        this.chatService = chatService;
        this.imageService = imageService;
        this.recipeService = recipeService;
    }


    @GetMapping("ask-ai")
    public String getResponse(@RequestParam String prompt)
    {
        return chatService.getResponse(prompt);
    }

    @GetMapping("ask-ai-options")
    public String getResponseOptions(@RequestParam String prompt)
    {
        return chatService.getResponseOptions(prompt);
    }


    @GetMapping("generate-image")
    public void getImage(HttpServletResponse response, @RequestParam String prompt,
                         @RequestParam(defaultValue = "hd") String quality,
                         @RequestParam(defaultValue = "1") int n,
                         @RequestParam(defaultValue = "1o24") int height,
                         @RequestParam(defaultValue = "1o24") int width) throws IOException {
        ImageResponse imageResponse = imageService.generateImage(prompt, quality, n, height, width);
        String imageUrl = imageResponse.getResult().getOutput().getUrl();
        response.sendRedirect(imageUrl);
    }

    @GetMapping("generate-image-options")
    public void getImageOptions(HttpServletResponse response, @RequestParam String prompt) throws IOException {
        ImageResponse imageResponse = imageService.generateImageOptions(prompt);
        String imageUrl = imageResponse.getResult().getOutput().getUrl();
        response.sendRedirect(imageUrl);
    }

    @GetMapping("generate-images")
    public List<String> getImages(HttpServletResponse response, @RequestParam String prompt) throws IOException {
        ImageResponse imageResponse = imageService.generateImage(prompt);
        List<String> imageUrls = imageResponse.getResults().stream()
                        .map(result -> result.getOutput().getUrl())
                                .toList();
        return imageUrls;
    }

    @GetMapping("recipe")
    public String getRecipe(@RequestParam String ingredients, @RequestParam String cuisine, @RequestParam String dietaryRestrictions)
    {
        return recipeService.createRecipe(ingredients, cuisine, dietaryRestrictions);
    }
}
