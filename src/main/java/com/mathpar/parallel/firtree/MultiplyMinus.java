/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mathpar.parallel.firtree;
//package com.mathpar.students.ukma17i41.bosa.parallel.engine;

import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;

/**
 *
 * @author alla
 */
public class MultiplyMinus extends Multiply {
    public MultiplyMinus() {
        inData = new Element[2];
        outData = new Element[1];
        numberOfMainComponents = 2;
        state = 0;
        arcs = new int[][] {{1, 0, 0, 1, 4, 1, 2, 1, 0, 2, 6, 1, 3, 0, 0, 3, 5, 1, 4, 1, 0, 4, 7, 1,
            5, 2, 0, 5, 4, 1, 6, 3, 0, 6, 6, 1, 7, 2, 0, 7, 5, 1, 8, 3, 0, 8, 7, 1},
        {2, 0, 2}, {9, 0, 0}, {4, 0, 2}, {9, 0, 1}, {6, 0, 2}, {9, 0, 2}, {8, 0, 2}, {9, 0, 3}, {}};
        type = 2;
        resultForOutFunctionLength = 4;
        inputDataLength = 2;
    }

    @Override
    public void sequentialCalc(Ring ring) {
        outData[0] = inData[0].multiply(inData[1], ring).negate(ring);
        System.out.println(outData[0]);
    }

    @Override
    public MatrixS[] outputFunction(Element[] input, Ring ring) {
        MatrixS[] resmat = new MatrixS[input.length];
        for (int i = 0; i < input.length; i++) {
            resmat[i] = (MatrixS) input[i];
        }
        MatrixS[] res = new MatrixS[] {MatrixS.join(resmat).negate(ring)};
        return res;

    }

}
