package com.mathpar.Graphic2D.paintElements.GeometryEngine.interfaces;

import com.mathpar.Graphic2D.paintElements.GeometryEngine.constants.OperatorKind;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.error.OperatorException;
import com.mathpar.Graphic2D.paintElements.GeometryEngine.model.ScreenParams;
import com.mathpar.Graphic2D.paintElements.EngineImpl.register.ArgType;
import com.mathpar.number.Ring;

import java.util.List;

public interface Operator {

    // TODO: implement operators

    default OperatorKind kind() {
        return OperatorKind.FUNCTION;
    }

    /**
     * Returns operator's argument types in an ordered list.
     * <br><br>
     * Example: <br><br>
     *
     * <code>
     * tr = Triangle(2, 3, 4)
     * </code>
     * <br><br>
     * Operator <code>Triangle</code> must return
     * <br>
     * <code>List(ArgType.REAL_NUM, ArgType.REAL_NUM, ArgType.REAL_NUM)</code>
     * <br>
     * when {@link #getArgTypes()} is called
     *
     * @return list of operator argument types
     */
    List<ArgType> getArgTypes();

    /**
     * Returns operator's name as it is presented in code.
     * <br><br>
     * Example:
     * <br><br>
     * <code>
     * tr = Triangle(2, 3, 4)
     * </code>
     * <br>
     * Operator <code>Triangle</code> must return string 'Triangle'
     * when {@link #getFullName()} is called
     *
     * @return operator's name
     */
    String getFullName();

    /**
     * Applies operator to args. Args may or may not be modified by the operator.
     * Args are validated before they enter the operator
     *
     * @param args   arguments of the operator
     * @param screen screen size parameters
     * @param ring
     * @return operator's return value
     * @throws OperatorException error that arises during the operator's work
     */
    GeometryVar apply(List<GeometryVar> args, ScreenParams screen, Ring ring) throws OperatorException;

}
