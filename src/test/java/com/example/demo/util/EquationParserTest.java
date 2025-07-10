package com.example.demo.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.example.demo.model.ExpressionTree;

public class EquationParserTest {

    @Test
    public void testInfixToPostfix_SimpleAddition() {
        List<String> postfix = EquationParser.infixToPostfix("3 + 4");
        assertEquals(3, postfix.size());
        assertEquals("3", postfix.get(0));
        assertEquals("4", postfix.get(1));
        assertEquals("+", postfix.get(2));
    }
    
    @Test
    public void testInfixToPostfix_ComplexExpression() {
        List<String> postfix = EquationParser.infixToPostfix("3 * x + 2 * y - z");
        assertEquals(9, postfix.size());
        // Check the operators in the correct positions
        assertEquals("*", postfix.get(2)); // 3 * x
        assertEquals("*", postfix.get(5)); // 2 * y
        assertEquals("+", postfix.get(6)); // (3*x) + (2*y)
        assertEquals("-", postfix.get(8)); // ((3*x)+(2*y)) - z
    }
    
    @Test
    public void testInfixToPostfix_WithParentheses() {
        List<String> postfix = EquationParser.infixToPostfix("(x + y) * z");
        assertEquals(5, postfix.size());
        assertEquals("x", postfix.get(0));
        assertEquals("y", postfix.get(1));
        assertEquals("+", postfix.get(2));
        assertEquals("z", postfix.get(3));
        assertEquals("*", postfix.get(4));
    }
    
    @Test
    public void testBuildExpressionTree_SimpleExpression() {
        ExpressionTree tree = EquationParser.parseEquation("x + y");
        assertNotNull(tree);
        assertNotNull(tree.getRoot());
        assertEquals("+", tree.getRoot().getValue());
        assertEquals("x", tree.getRoot().getLeft().getValue());
        assertEquals("y", tree.getRoot().getRight().getValue());
    }
    
    @Test
    public void testEvaluateExpression_SimpleAddition() {
        ExpressionTree tree = EquationParser.parseEquation("x + y");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 3.0);
        variables.put("y", 4.0);
        
        double result = tree.evaluate(variables);
        assertEquals(7.0, result, 0.001);
    }
    
    @Test
    public void testEvaluateExpression_ComplexExpression() {
        ExpressionTree tree = EquationParser.parseEquation("3 * x + 2 * y - z");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 1.0);
        
        double result = tree.evaluate(variables);
        assertEquals(11.0, result, 0.001);
    }
    
    @Test
    public void testEvaluateExpression_WithParentheses() {
        ExpressionTree tree = EquationParser.parseEquation("(x + y) * z");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        variables.put("z", 4.0);
        
        double result = tree.evaluate(variables);
        assertEquals(20.0, result, 0.001);
    }
    
    @Test
    public void testEvaluateExpression_WithPower() {
        ExpressionTree tree = EquationParser.parseEquation("x^2 + y^2");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 3.0);
        variables.put("y", 4.0);
        
        double result = tree.evaluate(variables);
        assertEquals(25.0, result, 0.001);
    }
    
    @Test
    public void testEvaluateExpression_MissingVariable() {
        ExpressionTree tree = EquationParser.parseEquation("x + y");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 3.0);
        
        assertThrows(IllegalArgumentException.class, () -> tree.evaluate(variables));
    }
    
    @Test
    public void testEvaluateExpression_DivisionByZero() {
        ExpressionTree tree = EquationParser.parseEquation("x / y");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 3.0);
        variables.put("y", 0.0);
        
        assertThrows(ArithmeticException.class, () -> tree.evaluate(variables));
    }
    
    @Test
    public void testEvaluateExpression_WithCoefficient() {
        ExpressionTree tree = EquationParser.parseEquation("3x + 2y");
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 3.0);
        
        double result = tree.evaluate(variables);
        assertEquals(12.0, result, 0.001);
    }
} 