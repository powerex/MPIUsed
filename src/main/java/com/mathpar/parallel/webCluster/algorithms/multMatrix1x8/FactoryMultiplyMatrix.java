/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.parallel.webCluster.algorithms.multMatrix1x8;

import com.mathpar.number.Ring;
import com.mathpar.parallel.ddp.engine.AbstractFactoryOfObjects;
import com.mathpar.parallel.ddp.engine.AbstractTask;


public class FactoryMultiplyMatrix extends AbstractFactoryOfObjects{
    
    
    @Override
    public AbstractTask CreateTask(int type) {
        switch (type){
            default: return new TaskMultiplyMatrix();
        }
    }

    @Override
    public void InitGraphs() {
        GraphMultiplyMatrix g0=new GraphMultiplyMatrix();
        AddGraphOfTask(0, g0);
    }


}
