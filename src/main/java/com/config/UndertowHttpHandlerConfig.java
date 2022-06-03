package com.config;

import com.config.proxy.ReverseProxyClient;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.proxy.ProxyHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class UndertowHttpHandlerConfig implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    final String jobRunnerDashboardUrl = "http://localhost:1000";
    final List<String> dashboardPaths = Arrays.asList("/dashboard", "/api/servers", "/api/problems",
            "/api/version", "/api/jobs", "/api/recurring-jobs", "/sse");

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
                    final HttpHandler defaultHttpHandler = bootstrap(deploymentInfo);

                    deploymentInfo.addInitialHandlerChainWrapper(httpHandler -> ProxyHandler.builder()
                            .setProxyClient(new ReverseProxyClient(defaultHttpHandler, jobRunnerDashboardUrl, dashboardPaths))
                            .setReuseXForwarded(true)
                            .setNext(ResponseCodeHandler.HANDLE_404)
                            .build());
                });
    }

    private HttpHandler bootstrap(DeploymentInfo deploymentInfo) {
        DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(deploymentInfo);
        deploymentManager.deploy();

        HttpHandler defaultHttpHandler = null;

        try {
            defaultHttpHandler = deploymentManager.start();
        } catch (Exception e) {
            log.error("Proxy: Failed to start reverse proxy!");
            log.error("Proxy: ");
        }

        return defaultHttpHandler;
    }
}
