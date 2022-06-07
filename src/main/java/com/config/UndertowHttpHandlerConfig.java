package com.config;

import com.config.proxy.ReverseProxyClient;
import io.undertow.UndertowOptions;
import io.undertow.server.handlers.proxy.ProxyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class UndertowHttpHandlerConfig implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    private final String jobRunrDashboardUrl = "http://localhost:1000";
    private final List<String> dashboardPaths = Arrays.asList("/dashboard", "/api/servers", "/api/problems",
            "/api/version", "/api/jobs", "/api/recurring-jobs", "/sse");

    @Override
    public void customize(final UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo ->
                deploymentInfo.addInitialHandlerChainWrapper(defaultHttpHandler ->
                        ProxyHandler.builder()
                                .setProxyClient(new ReverseProxyClient(defaultHttpHandler, jobRunrDashboardUrl, dashboardPaths))
                                .setReuseXForwarded(true)
                                .setNext(defaultHttpHandler)
                                .build()));

        factory.addBuilderCustomizers(builder ->
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));

        factory.setIoThreads(2);
        factory.setWorkerThreads(10);
    }
}
