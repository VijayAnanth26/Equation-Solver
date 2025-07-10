package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.demo.model.Equation;
import com.example.demo.model.ExpressionTree;
import com.example.demo.service.EquationService;
import com.example.demo.util.EquationParser;

@Service
public class EquationServiceImpl implements EquationService {
    
    private final Map<String, Equation> equationsMap = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    @Override
    public String storeEquation(String equationStr) {
        // Validate equation
        if (equationStr == null || equationStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Equation cannot be empty");
        }
        
        try {
            // Parse equation and build expression tree
            ExpressionTree expressionTree = EquationParser.parseEquation(equationStr);
            
            // Generate unique ID
            String equationId = String.valueOf(idGenerator.getAndIncrement());
            
            // Store equation
            Equation equation = new Equation(equationId, equationStr, expressionTree);
            equationsMap.put(equationId, equation);
            
            return equationId;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid equation format: " + e.getMessage());
        }
    }
    
    @Override
    public List<Equation> getAllEquations() {
        return new ArrayList<>(equationsMap.values());
    }
    
    @Override
    public Equation getEquationById(String equationId) {
        Equation equation = equationsMap.get(equationId);
        if (equation == null) {
            throw new IllegalArgumentException("Equation not found with ID: " + equationId);
        }
        return equation;
    }
    
    @Override
    public double evaluateEquation(String equationId, Map<String, Double> variables) {
        Equation equation = getEquationById(equationId);
        
        // Evaluate expression tree
        return equation.getExpressionTree().evaluate(variables);
    }
} 