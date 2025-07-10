package com.example.demo.model;

import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;

@Data
public class ExpressionTree {
    private TreeNode root;

    public ExpressionTree() {
        this.root = null;
    }

    public double evaluate(Map<String, Double> variables) {
        if (root == null) {
            throw new IllegalStateException("Expression tree is empty");
        }
        return evaluateNode(root, variables);
    }

    private double evaluateNode(TreeNode node, Map<String, Double> variables) {
        if (node == null) {
            return 0;
        }

        // If leaf node (operand)
        if (node.left == null && node.right == null) {
            if (node.isVariable()) {
                String varName = node.getValue();
                if (!variables.containsKey(varName)) {
                    throw new IllegalArgumentException("Variable " + varName + " not provided");
                }
                return variables.get(varName);
            } else if (isCoefficient(node.getValue())) {
                // Handle coefficients like "3x"
                return evaluateCoefficient(node.getValue(), variables);
            } else {
                return Double.parseDouble(node.getValue());
            }
        }

        // Evaluate left and right subtrees
        double leftValue = evaluateNode(node.left, variables);
        double rightValue = evaluateNode(node.right, variables);

        // Apply operator
        switch (node.getValue()) {
            case "+": return leftValue + rightValue;
            case "-": return leftValue - rightValue;
            case "*": return leftValue * rightValue;
            case "/": 
                if (rightValue == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return leftValue / rightValue;
            case "^": return Math.pow(leftValue, rightValue);
            default: throw new IllegalArgumentException("Unknown operator: " + node.getValue());
        }
    }
    
    private boolean isCoefficient(String token) {
        // Check if the token is a coefficient like "3x"
        return token.matches("\\d+[a-zA-Z]+");
    }
    
    private double evaluateCoefficient(String token, Map<String, Double> variables) {
        Pattern pattern = Pattern.compile("(\\d+)([a-zA-Z]+)");
        Matcher matcher = pattern.matcher(token);
        
        if (matcher.matches()) {
            double coefficient = Double.parseDouble(matcher.group(1));
            String varName = matcher.group(2);
            
            if (!variables.containsKey(varName)) {
                throw new IllegalArgumentException("Variable " + varName + " not provided");
            }
            
            return coefficient * variables.get(varName);
        }
        
        throw new IllegalArgumentException("Invalid coefficient format: " + token);
    }

    public String toInfixNotation() {
        if (root == null) {
            return "";
        }
        return toInfixNotation(root);
    }

    private String toInfixNotation(TreeNode node) {
        if (node == null) {
            return "";
        }

        if (node.left == null && node.right == null) {
            return node.getValue();
        }

        StringBuilder sb = new StringBuilder();
        
        // Add parentheses for clarity
        sb.append("(");
        sb.append(toInfixNotation(node.left));
        sb.append(" ").append(node.getValue()).append(" ");
        sb.append(toInfixNotation(node.right));
        sb.append(")");
        
        return sb.toString();
    }
} 