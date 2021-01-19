package com.craftinginterpreters.lox;

import java.io.FileWriter;
import java.io.IOException;

public class DotPrinter  implements Expr.Visitor<String> {
    int nodeNumber = 1;

    String print(Expr expr) {
        StringBuilder builder = new StringBuilder();
        builder.append("graph {\n");
        builder.append(expr.accept(this));
        builder.append("}");
        return builder.toString();
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return makeNode(expr.name.lexeme + " = " + expr.value, nodeNumber);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return makeDotNodes(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return makeDotNodes("()", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return makeNode("nil", nodeNumber);
        return makeNode(expr.value.toString(), nodeNumber);
    }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) {
        return makeDotNodes(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return makeDotNodes(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return makeNode(expr.name.lexeme, nodeNumber);
    }

    private String makeNode(String label, int thisNodeNumber) {
        return "\tn" + thisNodeNumber + " [label=\"" + label + "\"]\n";
    }

    private String makeEdge(int nodeFrom, int nodeTo) {
        return "\tn" + nodeFrom + " -- " + "n" + nodeTo + "\n";
    }

    private String makeDotNodes(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        int myNodeNumber = nodeNumber;
        builder.append(makeNode(name, myNodeNumber));
        for (Expr expr : exprs) {
            nodeNumber += 1;
            int childNodeNumber = nodeNumber;
            builder.append(expr.accept(this));
            builder.append(makeEdge(myNodeNumber, childNodeNumber));
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Binary(
                    new Expr.Literal(45.67),
                    new Token(TokenType.PLUS, "+", null, 1),
                    new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(22.1))
                )
            )
        );
        try {
            FileWriter dotFile = new FileWriter("lox.dot");
            dotFile.write(new DotPrinter().print(expression));
            dotFile.close();
            Runtime rt = Runtime.getRuntime();
            rt.exec("dot -Tpdf -O lox.dot && open lox.dot.pdf");
        } catch (IOException e) {
            System.out.println("Couldn't create file.");
            e.printStackTrace();
        }
    }
}
