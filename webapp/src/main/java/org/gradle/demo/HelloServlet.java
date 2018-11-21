package org.gradle.demo;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "HelloServlet", urlPatterns = {"/hello"}, loadOnStartup = 1) 
public class HelloServlet extends HttpServlet {
    //final static Log logger = LogFactory.getLog(HelloServlet.class);
    
    private static Logger logger = LogManager.getLogger(HelloServlet.class);
    //final static Logger logger = LogManager.getLogger("CONSOLE_JSON_APPENDER");
    //logger.debug("Debug message");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("here1!!");
        response.getWriter().print("Hello, World!");  
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        
        logger.debug("here2!!");
        String name = request.getParameter("name");
        if (name == null) name = "World";
        logger.debug(name);
        request.setAttribute("user", name);
        request.getRequestDispatcher("response.jsp").forward(request, response); 
    }
}