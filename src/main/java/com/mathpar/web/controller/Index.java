/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.web.controller;

import org.springframework.stereotype.Controller;
import com.mathpar.web.servlets.MathparUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Index {
    @RequestMapping(value = "/{lang:en|ru|ua|iw|am}/", method = RequestMethod.GET)
    public void indexLang(HttpServletResponse resp, @PathVariable String lang) throws IOException {
        resp.sendRedirect("index.html");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void index(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final MathparUtils mu = new MathparUtils(req, resp);
        resp.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
        resp.setHeader("Pragma", "no-cache"); //HTTP 1.0
        resp.setDateHeader("Expires", -1);

        String cookieFirstVisit = mu.getCookieValue("Mathpar.firstVisit");
        if (cookieFirstVisit != null && cookieFirstVisit.equals("false")) {
            resp.sendRedirect("/en/index.html");
        } else {
            resp.sendRedirect("/en/welcome.html");
        }
    }
}
