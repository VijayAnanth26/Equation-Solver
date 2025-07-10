package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equation {
    private String equationId;
    private String equation;
    private ExpressionTree expressionTree;
} 