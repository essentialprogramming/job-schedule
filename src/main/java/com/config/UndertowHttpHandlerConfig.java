package com.config;

import com.config.proxy.ReverseProxyClient;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
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
    public void customize(final UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo ->
                deploymentInfo.addInitialHandlerChainWrapper(defaultHttpHandler ->
                        ProxyHandler.builder()
                                .setProxyClient(new ReverseProxyClient(defaultHttpHandler, jobRunnerDashboardUrl, dashboardPaths))
                                .setReuseXForwarded(true)
                                .setNext(defaultHttpHandler)
                                .build()));

        factory.addBuilderCustomizers(builder ->
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));

        factory.setIoThreads(2);
        factory.setWorkerThreads(10);
    }

    private HttpHandler bootstrap(final DeploymentInfo deploymentInfo) {
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
