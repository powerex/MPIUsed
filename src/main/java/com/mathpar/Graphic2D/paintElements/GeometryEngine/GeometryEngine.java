package com.mathpar.Graphic2D.paintElements.GeometryEngine;

import com.mathpar.Graphic2D.paintElements.EngineImpl.register.OperatorsRegister;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.error.EngineException;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.GeometryVar;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.Operator;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces.VarStorage;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.GeometryEngineOutput;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.ScreenParams;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.VarMap;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.VarModel;
import com.mathpar.number.Ring;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class GeometryEngine {

    // TODO:
    // - handleLine
    // - add single showPlots at the end
    // - label support
    // - add Ring param to Operators

    private static final Map<String, Map<String, Operator>> OPERATORS = Collections.unmodifiableMap(
            registerOperators()
    );
    private static final String DELIMITER = ";";

    public static GeometryEngineOutput parse(String input, Ring ring) {
        VarStorage vars = new VarMap();
        Map<String, String> labels = new HashMap<>();

        String[] lines = input.split(DELIMITER);
        if (lines.length == 0) {
            return new GeometryEngineOutput();
        }

        FirstLineOutput flOutput = handleFirstLine(lines);
        int startIndex = flOutput.startIndex;
        ScreenParams screen = flOutput.screen;

        StringBuilder processedInput = new StringBuilder();

        for (int i = startIndex; i < lines.length; i++) {
            VarModel varModel = null;
            try {
                varModel = LineParser.parse(lines[i], vars, OPERATORS, screen);
            } catch (EngineException e) {
                e.printStackTrace();
            }
            processedInput.append(varModel.getProcessedInput());
            labels.put(varModel.getName(), varModel.getLabel());
        }

        String outputCode = vars.values()
                .stream()
                .filter(GeometryVar::isDisplayed)
                .map(GeometryVar::draw)
                .collect(Collectors.joining("\n"));

        return new GeometryEngineOutput(processedInput.toString(), outputCode);
    }

    private static FirstLineOutput handleFirstLine(String[] lines) {
        // TODO: implement

        return new FirstLineOutput();
    }

    private static Map<String, Map<String, Operator>> registerOperators() {
        Map<String, Map<String, Operator>> operators = new HashMap<>();

        String operatorName;
        Map<String, Operator> types;
        String typeString;
        Operator presentOperator;

        for (Operator operator : OperatorsRegister.OPERATORS) {
            operatorName = operator.getFullName();
            types = operators.computeIfAbsent(operatorName, k -> new HashMap<>());

            typeString = LineParser.argsCanonForm(operator.getArgTypes());
            presentOperator = types.get(typeString);

            if (presentOperator != null) {
                log.error(String.format(
                        "Operators clash: '%s' and '%s' have the same definition",
                        presentOperator.getClass().getCanonicalName(),
                        operator.getClass().getCanonicalName()
                ));
                continue;
            }

            types.put(typeString, operator);
        }

        return operators;
    }

    private static class FirstLineOutput {
        int startIndex;
        ScreenParams screen;
    }

}
