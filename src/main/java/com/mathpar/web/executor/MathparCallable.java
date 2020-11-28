/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.web.executor;

import com.mathpar.func.Page;
import com.mathpar.parallel.webCluster.engine.AlgorithmsConfig;
import java.util.concurrent.Callable;

public class MathparCallable implements Callable<MathparResult> {
    private final Page page;
    private final String task;
    private final int sectionId;

    public MathparCallable(Page page, String task, int sectionId) {
        this.page = page;
        this.task = task;
        this.sectionId = sectionId;
    }

    @Override
    public MathparResult call() throws Exception {
        String result = "";
        String latex = "";
        try {
            result = page.execution(task, sectionId);
            latex = page.strToTexStrMain(page.data.section[0] + "\nout:\n"
                + page.data.section[1]);
        } catch (Exception exception) { result = exception.getStackTrace().toString(); }
        return new MathparResult(result, latex);
    }
}
