/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.web.controller;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageParam {
}
