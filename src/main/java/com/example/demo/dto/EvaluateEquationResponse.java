package com.example.demo.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateEquationResponse {
    private String equationId;
    private String equation;
    private Map<String, Double> variables;
    private double result;
} 