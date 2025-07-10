package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EquationListResponse;
import com.example.demo.dto.EquationListResponse.EquationDto;
import com.example.demo.dto.EvaluateEquationRequest;
import com.example.demo.dto.EvaluateEquationResponse;
import com.example.demo.dto.StoreEquationRequest;
import com.example.demo.dto.StoreEquationResponse;
import com.example.demo.model.Equation;
import com.example.demo.service.EquationService;

@RestController
@RequestMapping("/api/equations")
public class EquationController {
    
    private final EquationService equationService;
    
    public EquationController(EquationService equationService) {
        this.equationService = equationService;
    }
    
    @PostMapping("/store")
    public ResponseEntity<StoreEquationResponse> storeEquation(@RequestBody StoreEquationRequest request) {
        String equationId = equationService.storeEquation(request.getEquation());
        StoreEquationResponse response = new StoreEquationResponse("Equation stored successfully", equationId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<EquationListResponse> getAllEquations() {
        List<Equation> equations = equationService.getAllEquations();
        
        List<EquationDto> equationDtos = equations.stream()
                .map(eq -> new EquationDto(eq.getEquationId(), eq.getEquation()))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new EquationListResponse(equationDtos));
    }
    
    @PostMapping("/{equationId}/evaluate")
    public ResponseEntity<EvaluateEquationResponse> evaluateEquation(
            @PathVariable String equationId,
            @RequestBody EvaluateEquationRequest request) {
        Equation equation = equationService.getEquationById(equationId);
        double result = equationService.evaluateEquation(equationId, request.getVariables());
        
        EvaluateEquationResponse response = new EvaluateEquationResponse(
                equationId,
                equation.getEquation(),
                request.getVariables(),
                result
        );
        
        return ResponseEntity.ok(response);
    }
} 