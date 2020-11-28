package com.mathpar.Graphic2D.tangent.lexerParser;


/**
 * An interface for the visitor design pattern.
 * <p>
 * Expression nodes can be visited by fviz.lexerParser.ExpressionNodeVisitor by calling their
 * accept methods. The expression nodes, in turn, call the appropriate visit
 * method of the expression node visitor.
 */
public interface ExpressionNodeVisitor {
    /**
     * Visit a fviz.lexerParser.VariableExpressionNode
     */
    public void visit(VariableExpressionNode node);

    /**
     * Visit a fviz.lexerParser.ConstantExpressionNode
     */
    public void visit(ConstantExpressionNode node);

    /**
     * Visit a fviz.lexerParser.AdditionExpressionNode
     */
    public void visit(AdditionExpressionNode node);

    /**
     * Visit a fviz.lexerParser.MultiplicationExpressionNode
     */
    public void visit(MultiplicationExpressionNode node);

    /**
     * Visit a fviz.lexerParser.ExponentiationExpressionNode
     */
    public void visit(ExponentiationExpressionNode node);

    /**
     * Visit a fviz.lexerParser.FunctionExpressionNode
     */
    public void visit(FunctionExpressionNode node);
}
