package com.zythologue.minimal_api.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {

    @RequestMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }

}