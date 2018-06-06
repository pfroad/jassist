package com.hawk.agent.jassist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletMonitor {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ServletMonitor.class);

    public static void begin(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("start", System.currentTimeMillis());
        System.out.printf("URI: %s\n", request.getRequestURI());
    }

    public static void end(HttpServletRequest request, HttpServletResponse response) {
        long end = System.currentTimeMillis();
        long start = (long) request.getAttribute("start");
        System.out.printf("Takes %d ms", (end - start));
//        response.
    }
}
