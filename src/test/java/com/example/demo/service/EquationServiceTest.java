package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.model.Equation;
import com.example.demo.service.impl.EquationServiceImpl;

public class EquationServiceTest {

    private EquationService equationService;
    
    @BeforeEach
    public void setUp() {
        equationService = new EquationServiceImpl();
    }
    
    @Test
    public void testStoreEquation_ValidEquation() {
        String equationId = equationService.storeEquation("3x + 2y - z");
        assertNotNull(equationId);
        assertEquals("1", equationId);
    }
    
    @Test
    public void testStoreEquation_EmptyEquation() {
        assertThrows(IllegalArgumentException.class, () -> equationService.storeEquation(""));
    }
    
    @Test
    public void testGetAllEquations_Empty() {
        List<Equation> equations = equationService.getAllEquations();
        assertNotNull(equations);
        assertTrue(equations.isEmpty());
    }
    
    @Test
    public void testGetAllEquations_WithStoredEquations() {
        equationService.storeEquation("3x + 2y - z");
        equationService.storeEquation("x^2 + y^2");
        
        List<Equation> equations = equationService.getAllEquations();
        assertNotNull(equations);
        assertEquals(2, equations.size());
    }
    
    @Test
    public void testGetEquationById_ExistingId() {
        String equationId = equationService.storeEquation("3x + 2y - z");
        
        Equation equation = equationService.getEquationById(equationId);
        assertNotNull(equation);
        assertEquals(equationId, equation.getEquationId());
        assertEquals("3x + 2y - z", equation.getEquation());
    }
    
    @Test
    public void testGetEquationById_NonExistingId() {
        assertThrows(IllegalArgumentException.class, () -> equationService.getEquationById("999"));
    }
    
    @Test
    public void testEvaluateEquation_ValidEquation() {
        String equationId = equationService.storeEquation("3x + 2y - z");
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 1.0);
        
        double result = equationService.evaluateEquation(equationId, variables);
        assertEquals(11.0, result, 0.001);
    }
    
    @Test
    public void testEvaluateEquation_MissingVariable() {
        String equationId = equationService.storeEquation("3x + 2y - z");
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        
        assertThrows(IllegalArgumentException.class, () -> equationService.evaluateEquation(equationId, variables));
    }
    
    @Test
    public void testEvaluateEquation_NonExistingId() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        
        assertThrows(IllegalArgumentException.class, () -> equationService.evaluateEquation("999", variables));
    }
    
    @Test
    public void testMultipleEquations() {
        String eq1Id = equationService.storeEquation("3x + 2y - z");
        String eq2Id = equationService.storeEquation("x^2 + y^2");
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 3.0);
        variables.put("y", 4.0);
        variables.put("z", 1.0);
        
        double result1 = equationService.evaluateEquation(eq1Id, variables);
        double result2 = equationService.evaluateEquation(eq2Id, variables);
        
        assertEquals(16.0, result1, 0.001);
        assertEquals(25.0, result2, 0.001);
    }
} 