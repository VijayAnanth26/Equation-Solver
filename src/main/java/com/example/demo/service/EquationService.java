package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.model.Equation;

public interface EquationService {
    String storeEquation(String equation);
    List<Equation> getAllEquations();
    Equation getEquationById(String equationId);
    double evaluateEquation(String equationId, Map<String, Double> variables);
} 