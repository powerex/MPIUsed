/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
//package com.mathpar.students.ukma17i41.bosa.parallel.engine;
package com.mathpar.parallel.firtree;
import com.mathpar.matrix.MatrixS;
import com.mathpar.number.Element;
import com.mathpar.number.Ring;

/**
 *
 * @author alla
 */
public class MultiplyExtended extends Multiply {
    public MultiplyExtended() {
        inData = new Element[2];
        outData = new Element[2];
        numberOfMainComponents = 1;
        state = 0;
        arcs = new int[][] {{1, 0, 0, 1, 4, 1, 2, 1, 0, 2, 6, 1, 3, 0, 0, 3, 5, 1, 4, 1, 0, 4, 7, 1,
            5, 2, 0, 5, 4, 1, 6, 3, 0, 6, 6, 1, 7, 2, 0, 7, 5, 1, 8, 3, 0, 8, 7, 1},
        {2, 0, 2}, {9, 0, 0}, {4, 0, 2}, {9, 0, 1}, {6, 0, 2}, {9, 0, 2}, {8, 0, 2}, {9, 0, 3}, {}};
        type = 6;
        resultForOutFunctionLength = 4;
        inputDataLength = 2;
    }

    @Override
    public void sequentialCalc(Ring ring) {
        MatrixS b = ((MatrixS) inData[0]).transpose();
        outData[1] = b;
        outData[0] = inData[1].add(b.multiply(inData[0], ring).negate(ring), ring);
    }

    @Override
    public MatrixS[] inputFunction(Element[] input) {

        MatrixS ms = ((MatrixS) input[0]).transpose();
        MatrixS ms1 = (MatrixS) input[0];

        MatrixS[] res = Tools.concatTwoArrays(ms.split(), ms1.split());
        MatrixS[] masResult = new MatrixS[res.length + 1];
        System.arraycopy(res, 0, masResult, 0, res.length);
        masResult[res.length] = ms;

        for (MatrixS m : masResult) {
            System.out.println(m.toString());
        }
        return masResult;
    }

    @Override
    public MatrixS[] outputFunction(Element[] input, Ring ring) {

        MatrixS[] resmat = new MatrixS[input.length];
        for (int i = 0; i < input.length; i++) {
            resmat[i] = (MatrixS) input[i];
        }
        Element[] res = new Element[] {input[4].add(MatrixS.join(resmat).negate(ring),ring), input[5]};
        System.out.println("res in outputFunction = " + res[0]);
        return (MatrixS[]) res;

    }

}
