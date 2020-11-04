package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.UrlFilter;
import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    //    private final String proxyServer;
    private Map<String, Integer> routes;
    private HttpOutboundHandler handler;
    private List<HttpRequestFilter> filters = new ArrayList<>();

    public HttpInboundHandler(Map<String, Integer> routes) {
        this.routes = Collections.unmodifiableMap(routes);
        handler = new HttpOutboundHandler();
        filters.add(new UrlFilter(new ArrayList<>(routes.keySet())));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            logger.info("get fullRequest: {}", fullRequest);
            logger.info("fullRequest url backend: {}", getBackendUrl(fullRequest.uri()));
            for (HttpRequestFilter httpRequestFilter : filters) {
                if (!httpRequestFilter.filter(fullRequest, ctx)) {
                    logger.info("request {} is invalid", fullRequest.uri());
                    handler.handle(fullRequest, ctx, getBackendUrl("/other"));
                    return;
                }
            }
            handler.handle(fullRequest, ctx, getBackendUrl(fullRequest.uri()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private String getBackendUrl(String partUrl) {
        return "http://localhost:" + routes.get(partUrl);
    }

}
