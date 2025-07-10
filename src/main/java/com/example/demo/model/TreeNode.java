package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {
    private String value;
    public TreeNode left;
    public TreeNode right;
    
    public TreeNode(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
    
    public boolean isOperator() {
        return value.equals("+") || value.equals("-") || value.equals("*") || 
               value.equals("/") || value.equals("^");
    }
    
    public boolean isVariable() {
        return value.matches("[a-zA-Z]+");
    }
    
    public boolean isNumber() {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 