package com.example.demo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;

import com.example.demo.model.ExpressionTree;
import com.example.demo.model.TreeNode;

public class EquationParser {
    
    private static final Map<String, Integer> PRECEDENCE = new HashMap<>();
    
    static {
        PRECEDENCE.put("+", 1);
        PRECEDENCE.put("-", 1);
        PRECEDENCE.put("*", 2);
        PRECEDENCE.put("/", 2);
        PRECEDENCE.put("^", 3);
        PRECEDENCE.put("(", 0);
    }
    
    public static ExpressionTree parseEquation(String equation) {
        if (equation == null || equation.trim().isEmpty()) {
            throw new IllegalArgumentException("Equation cannot be empty");
        }
        
        validateEquationSyntax(equation);
        
        List<String> postfix = infixToPostfix(equation);
        
        return buildExpressionTree(postfix);
    }
    
    private static void validateEquationSyntax(String equation) {
        int openCount = 0;
        for (char c : equation.toCharArray()) {
            if (c == '(') openCount++;
            if (c == ')') openCount--;
            if (openCount < 0) throw new IllegalArgumentException("Unbalanced parentheses in equation");
        }
        if (openCount != 0) throw new IllegalArgumentException("Unbalanced parentheses in equation");
        
        if (!Pattern.matches("[\\d\\w\\s\\+\\-\\*\\/\\^\\(\\)]+", equation)) {
            throw new IllegalArgumentException("Equation contains invalid characters");
        }
        
        if (Pattern.matches(".*[\\+\\-\\*\\/\\^][\\+\\*\\/\\^].*", equation)) {
            throw new IllegalArgumentException("Invalid consecutive operators in equation");
        }
        
        if (Pattern.matches(".*\\(\\s*\\).*", equation)) {
            throw new IllegalArgumentException("Empty parentheses in equation");
        }
    }
    
    public static List<String> infixToPostfix(String equation) {
        List<String> tokens = tokenizeEquation(equation);
        
        List<String> postfix = new ArrayList<>();
        Stack<String> operators = new Stack<>();
        
        for (String token : tokens) {
            if (isOperand(token)) {
                postfix.add(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    postfix.add(operators.pop());
                }
                if (!operators.isEmpty() && operators.peek().equals("(")) {
                    operators.pop();
                }
            } else {
                while (!operators.isEmpty() && 
                       PRECEDENCE.getOrDefault(operators.peek(), 0) >= PRECEDENCE.getOrDefault(token, 0)) {
                    postfix.add(operators.pop());
                }
                operators.push(token);
            }
        }
        
        while (!operators.isEmpty()) {
            postfix.add(operators.pop());
        }
        
        return postfix;
    }
    
    private static List<String> tokenizeEquation(String equation) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        
        for (int i = 0; i < equation.length(); i++) {
            char c = equation.charAt(i);
            
            if (Character.isWhitespace(c)) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                }
                continue;
            }
            
            if (c == '(' || c == ')' || isOperator(String.valueOf(c))) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                }
                tokens.add(String.valueOf(c));
            } else {
                currentToken.append(c);
            }
        }
        
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }
        
        return tokens;
    }
    
    private static boolean isOperand(String token) {
        return !isOperator(token) && !token.equals("(") && !token.equals(")");
    }
    
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || 
               token.equals("/") || token.equals("^");
    }
    
    public static ExpressionTree buildExpressionTree(List<String> postfix) {
        if (postfix.isEmpty()) {
            throw new IllegalArgumentException("Empty postfix expression");
        }
        
        Stack<TreeNode> stack = new Stack<>();
        
        for (String token : postfix) {
            if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression: insufficient operands for operator " + token);
                }
                
                TreeNode right = stack.pop();
                TreeNode left = stack.pop();
                
                TreeNode operator = new TreeNode(token);
                operator.setLeft(left);
                operator.setRight(right);
                
                stack.push(operator);
            } else {
                stack.push(new TreeNode(token));
            }
        }
        
        ExpressionTree tree = new ExpressionTree();
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: too many operands");
        }
        
        tree.setRoot(stack.pop());
        return tree;
    }
    
    public static String postfixToInfix(List<String> postfix) {
        Stack<String> stack = new Stack<>();
        
        for (String token : postfix) {
            if (isOperator(token)) {
                String right = stack.pop();
                String left = stack.pop();
                stack.push("(" + left + " " + token + " " + right + ")");
            } else {
                stack.push(token);
            }
        }
        
        return stack.isEmpty() ? "" : stack.pop();
    }
} 