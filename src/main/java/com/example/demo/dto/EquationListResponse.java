package com.example.demo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquationListResponse {
    private List<EquationDto> equations;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquationDto {
        private String equationId;
        private String equation;
    }
} 