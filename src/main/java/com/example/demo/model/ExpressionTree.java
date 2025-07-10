package com.example.demo.model;

import java.util.Map;
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

        if (node.left == null && node.right == null) {
            if (node.isVariable()) {
                String varName = node.getValue();
                if (!variables.containsKey(varName)) {
                    throw new IllegalArgumentException("Variable " + varName + " not provided");
                }
                return variables.get(varName);
            } else if (isCoefficient(node.getValue())) {
                return evaluateCoefficient(node.getValue(), variables);
            } else {
                return Double.parseDouble(node.getValue());
            }
        }

        double leftValue = evaluateNode(node.left, variables);
        double rightValue = evaluateNode(node.right, variables);

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

        String leftStr = toInfixNotation(node.left);
        String rightStr = toInfixNotation(node.right);
        
        // Determine if parentheses are needed based on operator precedence
        boolean needLeftParentheses = node.left != null && node.left.isOperator() && 
                                     hasLowerPrecedence(node.left.getValue(), node.getValue());
        boolean needRightParentheses = node.right != null && node.right.isOperator() && 
                                      (hasLowerPrecedence(node.right.getValue(), node.getValue()) || 
                                       (node.getValue().equals("-") || node.getValue().equals("/")));
        
        StringBuilder sb = new StringBuilder();
        
        if (needLeftParentheses) {
            sb.append("(").append(leftStr).append(")");
        } else {
            sb.append(leftStr);
        }
        
        sb.append(" ").append(node.getValue()).append(" ");
        
        if (needRightParentheses) {
            sb.append("(").append(rightStr).append(")");
        } else {
            sb.append(rightStr);
        }
        
        return sb.toString();
    }
    
    private boolean hasLowerPrecedence(String op1, String op2) {
        int prec1 = getOperatorPrecedence(op1);
        int prec2 = getOperatorPrecedence(op2);
        return prec1 < prec2;
    }
    
    private int getOperatorPrecedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }
} 