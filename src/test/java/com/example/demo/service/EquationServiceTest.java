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
    public void testStoreEquation() {
        String equationId = equationService.storeEquation("x + y");
        assertNotNull(equationId);
        
        Equation equation = equationService.getEquationById(equationId);
        assertNotNull(equation);
        assertEquals("x + y", equation.getEquation());
        assertEquals(equationId, equation.getEquationId());
    }
    
    @Test
    public void testStoreMultipleEquations() {
        String id1 = equationService.storeEquation("x + y");
        String id2 = equationService.storeEquation("3x - 2y");
        
        assertNotEquals(id1, id2);
        
        List<Equation> equations = equationService.getAllEquations();
        assertEquals(2, equations.size());
    }
    
    @Test
    public void testGetAllEquations_Empty() {
        List<Equation> equations = equationService.getAllEquations();
        assertNotNull(equations);
        assertTrue(equations.isEmpty());
    }
    
    @Test
    public void testGetEquationById_NotFound() {
        assertThrows(IllegalArgumentException.class, () -> equationService.getEquationById("999"));
    }
    
    @Test
    public void testEvaluateEquation() {
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
        String equationId = equationService.storeEquation("x + y");
        
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        
        assertThrows(IllegalArgumentException.class, () -> equationService.evaluateEquation(equationId, variables));
    }
    
    @Test
    public void testStoreInvalidEquation() {
        assertThrows(IllegalArgumentException.class, () -> equationService.storeEquation(""));
    }
} 