/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.parallel.webCluster.engine;

import java.io.Serializable;

/**
 *
 * @author r1d1
 * <br><br>
 * Объекты этого класса используются в качестве результатов запросов,
 * создаваемых с помощью класса QueryCreator.
 */
public class QueryResult implements Serializable{
    Integer resultState;
    Object []data;

    public Object[] getData() {
        return data;
    }

    public Integer getState(){
        return resultState;
    }
}
