# Nginx作为代理服务

## 正向代理和反向代理
> 客户端request ----> 代理request ----> 服务端
> 即客户端请求代理服务器, 代理服务器再去请求服务端。然后获取到响应之后服务端将响应返回给代理服务器, 进而代理服务器再将响应返回给客户端。

当Nginx作为代理服务时, 可以支持如下很多种协议  
协议|服务端
:---|:---
HTTP|Http Server
ICMP, POP, IMAP|Mail Server
HTTPS|Http Server
RTMP|Media Server
其中HTTP协议的代理是最常用的, 当Nginx作为正向代理时  
**客户端<----->Nginx**<----->服务端  
当Nginx作为反向代理时  
客户端<----->**Nginx<----->服务端**  
正向代理和反向代理的区别就在于代理的对象不一样
正向代理的代理对象是客户端, 而反向代理的对象是服务端  
**反向代理或正向代理的配置**
```
语法: proxy_pass URL;
默认: -
上下文: location, if in location, limit_except
```
**缓冲区的配置**
```
语法: proxy_buffering on | off;
默认: proxy_buffering on;
上下文: http, server, location
```
扩展: proxy_buffer_size、proxy_buffers、proxy_busy_buffers_size
更多信息可以参阅[官方文档](http://nginx.org/en/docs/http/ngx_http_proxy_module.html)。  
**跳转重定向的配置**
```
语法: proxy_redirect default;
      proxy_redirect off;
      proxy_redirect redirect replacement;
默认: proxy_redirect default;
上下文: http, server, location
```

**头信息的配置**
```
语法: proxy_set_header field value;
默认: proxy_set_header HOST $proxy_host;
      proxy_set_header Connection close;
上下文: http, server, location
```
扩展: proxy_hide_header、 proxy_set_body

**超时配置**(Nginx作为代理连接到后端服务器的超时时间)
```
语法: proxy_connect_timeout time;
默认: proxy_connect_timeout 60s;
上下文: http, server, location
```
扩展: 
- proxy_read_timeout  
已建立好连接时, Nginx作为代理获取web Server响应的超时时间(即web server接收到请求后的请求处理时间)。是Nginx 两次reading的间隔时间。
- proxy_send_timeout  
服务端请求处理完毕后, Nginx将响应发送给客户端的超时时间。

**配置示例**
```
location / {
    # 代理
    proxy_pass http://127.0.0.1:8080;
    # 后端返回301重定向, default即可, 如果需要改写可以自定义。
    proxy_redirect default;
    
    
    # Nginx代理向后端Web Server发送信息时所添加的头信息
    proxy_set_header Host $http_host;
    proxy_set_header X-Real-IP $remote_addr;
    
    # tcp请求超时时间 30s
    proxy_connect_timeout 30;
    # Nginx将资源响应给客户端的超时时间
    proxy_send_timeout 60;
    # Web Server处理请求超时时间(是Nginx两次reading的时间)
    proxy_read_timeout 60;
    
    # Nginx 保存请求头信息的缓存区大小
    proxy_buffer_size 32k;
    # 启用来自代理服务器的响应缓冲
    proxy_buffering on;
    # 4个缓冲区, 大小为128k
    proxy_buffers 4 128k;
    # 当缓冲区填满Nginx忙于响应时, 可以将web server响应填入此缓冲区
    proxy_busy_buffers_size 256k;
    # 当busy区也满后可以写入临时文件中
    proxy_max_temp_file_size 256k;
}
考虑到除 proxy_pass 字段之外的其他字段共用性可能比较强。可以将其他配置全部书写到一个文件中如/etc/nginx/conf.d/proxy_conf  然后直接使用
location / {
    proxy_pass http://127.0.0.1:8080;
    include /etc/nginx/conf.d/proxy_conf;
}
```
