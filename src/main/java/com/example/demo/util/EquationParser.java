package com.example.demo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

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
        // Convert infix to postfix
        List<String> postfix = infixToPostfix(equation);
        
        // Build expression tree from postfix
        return buildExpressionTree(postfix);
    }
    
    public static List<String> infixToPostfix(String equation) {
        // Tokenize the equation
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
                    operators.pop(); // Discard the "("
                }
            } else { // Operator
                while (!operators.isEmpty() && 
                       PRECEDENCE.getOrDefault(operators.peek(), 0) >= PRECEDENCE.getOrDefault(token, 0)) {
                    postfix.add(operators.pop());
                }
                operators.push(token);
            }
        }
        
        // Pop remaining operators
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
                // Add current token if exists
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                }
                // Add operator or parenthesis
                tokens.add(String.valueOf(c));
            } else {
                // Part of a number, variable, or coefficient
                currentToken.append(c);
            }
        }
        
        // Add last token if exists
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
        Stack<TreeNode> stack = new Stack<>();
        
        for (String token : postfix) {
            if (isOperator(token)) {
                // Pop two operands
                TreeNode right = stack.pop();
                TreeNode left = stack.pop();
                
                // Create operator node
                TreeNode operator = new TreeNode(token);
                operator.setLeft(left);
                operator.setRight(right);
                
                // Push back to stack
                stack.push(operator);
            } else {
                // Push operand to stack
                stack.push(new TreeNode(token));
            }
        }
        
        // The final node in the stack is the root of the expression tree
        ExpressionTree tree = new ExpressionTree();
        if (!stack.isEmpty()) {
            tree.setRoot(stack.pop());
        }
        
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