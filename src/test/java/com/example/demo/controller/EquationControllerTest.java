package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.EquationListResponse;
import com.example.demo.dto.EvaluateEquationRequest;
import com.example.demo.dto.EvaluateEquationResponse;
import com.example.demo.dto.StoreEquationRequest;
import com.example.demo.dto.StoreEquationResponse;
import com.example.demo.model.Equation;
import com.example.demo.model.ExpressionTree;
import com.example.demo.service.EquationService;

public class EquationControllerTest {

    @Mock
    private EquationService equationService;
    
    @InjectMocks
    private EquationController equationController;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testStoreEquation_Success() {
        StoreEquationRequest request = new StoreEquationRequest("3x + 2y - z");
        
        when(equationService.storeEquation("3x + 2y - z")).thenReturn("1");
        
        ResponseEntity<StoreEquationResponse> response = equationController.storeEquation(request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Equation stored successfully", response.getBody().getMessage());
        assertEquals("1", response.getBody().getEquationId());
        
        verify(equationService).storeEquation("3x + 2y - z");
    }
    
    @Test
    public void testGetAllEquations_Success() {
        List<Equation> equations = new ArrayList<>();
        equations.add(new Equation("1", "3x + 2y - z", new ExpressionTree()));
        equations.add(new Equation("2", "x^2 + y^2", new ExpressionTree()));
        
        when(equationService.getAllEquations()).thenReturn(equations);
        
        ResponseEntity<EquationListResponse> response = equationController.getAllEquations();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getEquations().size());
        assertEquals("1", response.getBody().getEquations().get(0).getEquationId());
        assertEquals("3x + 2y - z", response.getBody().getEquations().get(0).getEquation());
        
        verify(equationService).getAllEquations();
    }
    
    @Test
    public void testEvaluateEquation_Success() {
        String equationId = "1";
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 1.0);
        
        EvaluateEquationRequest request = new EvaluateEquationRequest(variables);
        
        Equation equation = new Equation(equationId, "3x + 2y - z", new ExpressionTree());
        
        when(equationService.getEquationById(equationId)).thenReturn(equation);
        when(equationService.evaluateEquation(equationId, variables)).thenReturn(11.0);
        
        ResponseEntity<EvaluateEquationResponse> response = equationController.evaluateEquation(equationId, request);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(equationId, response.getBody().getEquationId());
        assertEquals("3x + 2y - z", response.getBody().getEquation());
        assertEquals(11.0, response.getBody().getResult(), 0.001);
        
        verify(equationService).getEquationById(equationId);
        verify(equationService).evaluateEquation(equationId, variables);
    }
} 