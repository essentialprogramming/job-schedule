package com.config.proxy;


import io.undertow.client.ClientCallback;
import io.undertow.client.ClientConnection;
import io.undertow.client.UndertowClient;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.proxy.ProxyCallback;
import io.undertow.server.handlers.proxy.ProxyClient;
import io.undertow.server.handlers.proxy.ProxyConnection;

import lombok.extern.slf4j.Slf4j;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;


@Slf4j
public class ReverseProxyClient implements ProxyClient {

    private static final ProxyTarget TARGET = new ProxyTarget() {};

    private final UndertowClient client;
    private final HttpHandler defaultHttpHandler;
    private final String targetUrl;
    private final List<String> apiPaths;


    public ReverseProxyClient(HttpHandler defaultHttpHandler, String targetUrl, List<String> apiPaths) {
        log.info("Initializing Proxy client...");

        this.defaultHttpHandler = defaultHttpHandler;
        this.targetUrl = targetUrl;
        this.apiPaths = apiPaths;
        this.client = UndertowClient.getInstance();
    }

    @Override
    public ProxyTarget findTarget(HttpServerExchange exchange) {
        return TARGET;
    }

    @Override
    public void getConnection(ProxyTarget target, HttpServerExchange exchange, ProxyCallback<ProxyConnection> callback, long timeout, TimeUnit timeUnit) {

        //No proxy needed ( default case )
        if (apiPaths.stream().noneMatch(path -> exchange.getRequestPath().startsWith(path))) {
            exchange.dispatch(requireNonNull(defaultHttpHandler));
            return;
        }

        final String targetUri = targetUrl + exchange.getRequestURI();
        final ClientCallback<ClientConnection> clientCallback = exchange.getRequestPath().contains("/sse")
                ? new ConnectNotifierSse(callback, exchange)
                : new ConnectNotifier(callback, exchange);

        exchange.setRelativePath(exchange.getRequestPath()); // need this otherwise proxy forwards to chopped off path
        client.connect(
                clientCallback,
                URI.create(targetUri),
                exchange.getIoThread(),
                exchange.getConnection().getByteBufferPool(),
                OptionMap.EMPTY);
    }
}
