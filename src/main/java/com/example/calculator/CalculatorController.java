package com.example.calculator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CalculatorController {

    @GetMapping("/")
    public String index() {
        return "index"; // We will create index.html
    }

    // API for Basic Math Operations
    @PostMapping("/calculate")
    @ResponseBody
    public Map<String, Object> calculate(@RequestParam double num1, @RequestParam double num2, @RequestParam String op) {
        Map<String, Object> response = new HashMap<>();
        double result = 0;
        try {
            switch (op) {
                case "add": result = num1 + num2; break;
                case "sub": result = num1 - num2; break;
                case "mul": result = num1 * num2; break;
                case "div": 
                    if(num2 == 0) throw new ArithmeticException("Cannot divide by zero");
                    result = num1 / num2; 
                    break;
            }
            response.put("result", result);
            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // API for Fibonacci SVG (Only Generator Mode is used now)
    @PostMapping("/fibonacci/svg")
    @ResponseBody
    public String generateFibonacciSvg(@RequestParam(defaultValue = "6") int n) {
        // We force 'true' for units and 'true' for generator mode to match requirements
        return FibonacciSVGGenerator.generateSVG(n, true, true);
    }
}
