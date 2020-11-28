package com.mathpar.Graphic2D.paintElements.GeometryEngine;

import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.error.EngineException;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.error.ParseException;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.Operator;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.VarStorage;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.ScreenParams;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.VarModel;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

public class LineParser {

    // TODO: extract params into object
    public static VarModel parse(String line,
                               VarStorage vars,
                               Map<String, Map<String, Operator>> operators,
                               ScreenParams screen) throws EngineException {
        Token token = extractToken(line, 0);

        if (token.kind == TokenKind.POSSIBLE_RAW) {
            throw ParseException.at(0, "variable or operator", token.value);
        }

        if (token.kind == TokenKind.OPERATOR) {
            return parseOperator(token, line, vars, operators, screen);
        }

        return parseNewVar(token, line, vars, operators, screen);
    }

    public static String argsCanonForm(List<ArgType> argTypes) {
        return argTypes.toString();
    }

    private static VarModel parseNewVar(Token leftToken, String line,
                                      VarStorage vars, Map<String, Map<String, Operator>> operators,
                                      ScreenParams screen) throws EngineException {
        validateNewVar(leftToken, vars);

        if (line.length() <= leftToken.location || line.charAt(leftToken.location) != '=') {
            // TODO: fix message
            throw ParseException.at(leftToken.location, "=", "end of line");
        }

        Token rightToken = extractToken(line, leftToken.location + 1);

        // TODO: RAW_VALUE or VAR ?

        VarModel varModel = parseOperator(rightToken, line, vars, operators, screen);
        varModel.setName(leftToken.value);

        return varModel;
    }

    private static Token extractToken(String line, int location) {
        TokenKind tokenKind = TokenKind.POSSIBLE_RAW;
        StringBuilder value = new StringBuilder();
        char ch;

        for (; location < line.length(); location++) {
            ch = line.charAt(location);

            // TODO: add infix by extracting till the end of a whitespace trail

            if (ch == '=') {
                tokenKind = TokenKind.VAR;
                break;
            }

            if (ch == '(') {
                tokenKind = TokenKind.OPERATOR;
                break;
            }

            if (ch == ',' || ch == ')') {
                break;
            }

            value.append(ch);
        }

        return new Token(tokenKind, value.toString(), location);
    }

    private static void validateNewVar(Token token, VarStorage vars) throws ParseException {
        String value = token.value;
        int originalLength = value.length();

        value = value.trim();
        token.value = value;
        int length = value.length();

        if (length == 0) {
            // TODO: fix message
            throw ParseException.at(token.location + originalLength, "variable name", "=");
        }

        if (vars.contains(value)) {
            return;
        }

        if (!value.chars().allMatch(Character::isLetterOrDigit)) {
            throw ParseException.at(token.location, "variable name must be alphanumeric");
        }

        if (!Character.isLetter(value.charAt(0))) {
            throw ParseException.at(token.location, "variable name must start with a letter");
        }
    }

    private static void validateOperator(Token token,
                                         Map<String, Map<String, Operator>> operators) throws ParseException {
        token.value = token.value.trim();
        if (operators.containsKey(token.value)) {
            return;
        }

        throw ParseException.at(token.location, "Unknown operator: " + token.value);
    }

    private static void validatePossibleRaw(Token token, VarStorage vars) throws ParseException {
        // TODO: validate '=' separator (only when token.kind == VAR)
        // TODO: add operator support?

        String value = token.value;
        value = value.trim();
        token.value = value;

        if (vars.contains(value)) {
            token.kind = TokenKind.VAR;
            return;
        }

        if (value.charAt(0) == value.charAt(value.length() - 1) && value.charAt(0) == '\"') {
            token.kind = TokenKind.RAW_STRING;
            return;
        }

        try {
            Integer.parseInt(value);
            token.kind = TokenKind.RAW_INT;
            return;
        } catch (Throwable ignored) {}

        try {
            Double.parseDouble(value);
            token.kind = TokenKind.RAW_DOUBLE;
            return;
        } catch (Throwable ignored) {}

        throw ParseException.at(token.location, "invalid value");
    }

    private static VarModel parseOperator(Token operatorToken, String line,
                                          VarStorage vars, Map<String, Map<String, Operator>> operators,
                                          ScreenParams screen) throws EngineException {
        validateOperator(operatorToken, operators);

        // extract operator arguments
        Token argToken;
        int location = operatorToken.location + 1;
        List<GeometryVar> args = new ArrayList<>();

        for (; location < line.length() && line.charAt(location) != ')'; location++) {
            argToken = extractToken(line, location);
            validatePossibleRaw(argToken, vars);

            args.add(convertToGeometryVar(argToken, vars));

            location = argToken.location;
        }

        if (line.length() <= location) {
            // TODO: fix message
            throw ParseException.at(location, ")", "end of line");
        }

        // collect operator by name and arguments
        args = Collections.unmodifiableList(args);

        String typeString = argsCanonForm(args
                .stream()
                .map(GeometryVar::getType)
                .collect(Collectors.toList())
        );

        Operator operator = operators.get(operatorToken.value).get(typeString);
        if (operator == null) {
            throw ParseException.at(operatorToken.location, String.format(
                    "no operator '%s' exists with args '%s'",
                    operatorToken.value, typeString
            ));
        }

        // TODO: add ring

        // apply operator
        GeometryVar result = operator.apply(args, screen, null);

        // extract label
        String label = extractLabel(line, location);

        // TODO: return location for inner operator
        return new VarModel(null, label, result, line);
    }

    private static String extractLabel(String line, int location) {
        // TODO: implement
        return null;
    }

    private static GeometryVar convertToGeometryVar(Token argToken, VarStorage vars) {
        // TODO: implement
        return null;
    }

    @AllArgsConstructor
    private static class Token {
        TokenKind kind;
        String value;
        int location;
    }

    private enum TokenKind {
        VAR,
        OPERATOR,
        POSSIBLE_RAW,
        RAW_STRING,
        RAW_INT,
        RAW_DOUBLE,
    }

}
