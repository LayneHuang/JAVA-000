# netty-gateway
修改了 HttpInboundHandler 类, 网关 HttpOutboundHandler 还是保留老师的做法，使用线程池处理  

client 中 url 为 "/hl" 的 通过 网关中 routes 会走到 NettyHttpServer  
![第一类请求](.\src\main\resources\pic1.png)

其他 url 走经过 filters 的过滤 走 8088 端口（测试的时候用的是 nio01 中的 HttpServer01）  
![第二类请求](.\src\main\resources\pic2.png)

```
没理解的地方：
给出的 Demo 中 Routes 为何是 List 中返回 String 的接口  
```