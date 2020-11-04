package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Collections;
import java.util.List;

public class UrlFilter implements HttpRequestFilter {

    private final List<String> keywords;

    public UrlFilter(List<String> keywords) {
        this.keywords = Collections.unmodifiableList(keywords);
    }

    @Override
    public boolean filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        for (String word : keywords) {
            if (fullRequest.uri().equals(word)) {
                return true;
            }
        }
        return false;
    }
}
