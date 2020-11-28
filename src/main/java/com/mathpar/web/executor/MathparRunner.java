/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.web.executor;

import com.mathpar.func.Page;

public class MathparRunner {

    public MathparRunner() {
    }

    public MathparResult run(Page page, String task, int sectionId) {
        String result = page.execution(task, sectionId);
        String latex = page.strToTexStrMain(page.data.section[0] + "\nout:\n"
                + page.data.section[1])
                .replaceAll("\\\\unicode\\{xB0\\}", "^{\\\\circ}\\\\!")
                .replaceAll("'([^',\n;]+?)'", "\\\\hbox{$1}");
        return new MathparResult(result, latex);
    }
}
