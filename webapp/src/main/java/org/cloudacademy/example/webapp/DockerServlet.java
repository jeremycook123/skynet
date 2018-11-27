package org.cloudacademy.example.webapp;

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

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

@WebServlet(name = "DockerServlet", urlPatterns = { "/app", "/action1", "/action2", "/action3" }, loadOnStartup = 1)
public class DockerServlet extends HttpServlet {
    final static String CONTAINER_NETWORK_NAME = "ec2-user_jenkins";

    // final static Log logger = LogFactory.getLog(DockerServlet.class);

    private static Logger logger = LogManager.getLogger(DockerServlet.class);
    // final static Logger logger = LogManager.getLogger("CONSOLE_JSON_APPENDER");
    // logger.debug("Debug message");

    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withDockerHost("tcp://socat:2375").build();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get the docker client
        DockerClient client = DockerClientBuilder.getInstance(config).build();
        // prepare command to retrieve the list of (running) containers
        ListContainersCmd listContainersCmd = client.listContainersCmd().withStatusFilter("running");
        // and set the generic filter regarding name
        listContainersCmd.getFilters().put("name", Arrays.asList("webapp"));
        // finally, run the command
        List<Container> containerList = listContainersCmd.exec();

        String containerId = null;
        String containerIp = null;

        Iterator<Container> containerIterator = containerList.iterator();
        while (containerIterator.hasNext()) {
            Container container = containerIterator.next();

            containerId = container.getId();
            containerIp = container.getNetworkSettings().getNetworks().get(CONTAINER_NETWORK_NAME).getIpAddress();

            request.setAttribute("containerid", containerId);
            request.setAttribute("containerip", containerIp);
        }

        String path = request.getServletPath() != null ? request.getServletPath() : "";
        switch (path) {
        case "/app":
            request.getRequestDispatcher("response.jsp").forward(request, response);
            break;
        case "/action1":
            logger.debug("action 1 called...");
            response.getWriter().println("action->1");
            response.getWriter().println(containerId);
            response.getWriter().println(containerIp);
            break;
        case "/action2":
            logger.debug("action 2 called...");
            response.getWriter().println("action->2");
            response.getWriter().println(containerId);
            response.getWriter().println(containerIp);
            break;
        case "/action3":
            logger.debug("action 3 called...");
            response.getWriter().println("action->3");
            response.getWriter().println(containerId);
            response.getWriter().println(containerIp);
            break;
        }
    }
}