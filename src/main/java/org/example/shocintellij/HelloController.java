package org.example.shocintellij;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String testEndpoint() {
        return "Endpoint is working!";
    }
}
