# Nginx作为静态资源web服务

## 静态资源类型
Nginx作为静态资源的HTTP server, 它可以接收客户端类似jpeg, html, flv等不需要服务端动态生成的**静态资源请求**, 然后**直接通过静态资源的存储获取到这些文件返回给客户端**。
这是一种典型的高效传输模式。常常会应用在对于**静态资源处理请求**, **动静分离**的场景。  
常见的静态资源如下:  

类型 | 种类
---|---
浏览器端渲染 | HTML, CSS, JS
图片 | JPEG, GIF, PNG
视频 | FLV, MPEG
文件 | TXT, XLSX, PDF

## CDN场景
```
语法: sendfile on | off;
默认: sendfile off;
上下文: http, server, location, if in location
```
随着Nginx版本的不断升级和Linux内核的升级引用到了一些新的技术比如异步文件读取
编译参数**--with-file-nio**, 但是效果并不是特别明显。
```
语法: tcp_nopush on | off;
默认: tcp_nopush off;
上下文: http, server, location
```
该配置项的作用是在sendfile开启的情况下, 对响应包添加缓冲。提高网络包的传输效率
```
语法: tcp_nodelay on | off;
默认: tcp_nodelay on;
上下文: http, server, location
```
对于响应包不进行等待而是直接响应发送, 适用于实时性要求较高的场景。与tcp_nopush相对。该配置项需要支持keepalive情况下开启, 提高网络包的传输实时性。

```
语法: gzip on | off;
默认: gzip off;
上下文: http, server, location, if in location

语法: gzip_comp_level level;
默认: gzip_comp_level 1;
上下文: http, server, location

语法: gzip_http_version 1.0 | 1.1;
默认: gzip_http_version 1.1;
上下文: http, server, location
```
前一个配置项的作用是对响应进行压缩传输以节省带宽。并且通过压缩减小了传输文件的大小, 从而实现传输的实时性。  
第二个配置项主要配合第一个使用, 设置压缩等级, 压缩的比例越高, 最后压缩包也就越小传输的文件也就越小, 但是压缩这个操作本身就会消耗服务端的性能并且压缩后的文件也不可能无限小, 所以此处需要根据实际情况进行设置。  
第三个配置项是设置HTTP协议版本, 当前主流的版本还是 HTTP 1.1。

---
在使用rpm包安装Nginx时, Nginx会默认安装一个模块**http_gzip_static_module**  
这个模块是预读gzip的功能实现。比如我们需要读取 `1.html` 这个文件, Nginx会在家目录寻找`1.html.gz`这个文件检查是否存在如果存在那么就直接返回。由于会预先查看磁盘是否存在对应的 gz压缩包所以会对磁盘有一定要求, 但是换来的好处是有可能节省掉压缩操作所带来的性能开销。  
另外Nginx会安装的一个模块是**http_gunzip_module**  
该模块应用场景很少, 是当某些浏览器无法支持解压gzip压缩格式的文件时进行使用。


总配置示例
```
server {
    listen 80;
    server_name 127.0.0.1;

    sendfile on;
    access_log /var/log/nginx/log/static_access.log main;
    
    location ~ .*\.(jpg|gif|png)$ {
        root /opt/app/code/images;
    } 
    
    location ~ .*\.(txt|xml)$ {
        root /opt/app/code/doc;
    }
    
    location ~ ^/download {
        gzip_static on;
        tcp_nopush on;
        root /opt/app/code;
    }
}
```

## 浏览器缓存
浏览器缓存是基于HTTP协议定义的缓存机制(如: Expires; Cache-Control等)  
> 若浏览器无缓存  
> 浏览器请求 -> 无缓存 -> 请求web服务器 -> 服务器响应, 协商 -> 内容呈现
>  
> 若浏览器有缓存
> 浏览器请求 -> 有缓存 -> 校验过期... -> 内容呈现

校验过期机制

检验是否过期|Expires、Cache-Control(max-age)
:---|:---
**协议中Etag头信息校验**|**Etag**
**Last-Modified头信息校验**|**Last-Modi**

##### Nginx设置静态资源的过期
添加Cache-Control、 Expires头
```
语法: expires [modified] time;
      expires epoch | max | off;
默认: expires off;
上下文: http, server, location, if in location
``` 
配置示例
```
location ~ .*\.(htm|html)$ {
    expires 24h;
    root /opt/app/code;
}
```
## 跨域访问
- 浏览器为什么禁止跨域访问  
  不安全, 容易出现CSRF攻击
- Nginx如何解决跨域  
  通过添加Access-Control-Allow-Origin头信息
```
语法: add_header name value [always];
默认: -
上下文: http, server, location, if in location
```
配置示例
```
location ~ .*\.(htm|html)$ {
    add_header Access-Control-Allow-Origin http://127.0.0.1:8080;
    add_header Access-Control-Allow-Methods GET,POST,PUT,DELETE,OPTIONS;
    root /opt/app/code;
}
```
## 防盗链
- 作用: 防止资源被盗用  
- 防盗链的设置思路
    - 首要方式: 区别哪些请求是非正常的用户请求
- Nginx的简单防盗链实现: 基于http_refer防盗链配置模块
```
语法: valid_referers none | blocked | server_names | string...;
默认: -
上下文: server, location

其中valid_referers中支持正则如 "~/google\./"
```
配置示例
```
location ~ .*\.(htm|html)$ {
    valid_referers none blocked 127.0.0.1;
    if($invalid_referer) {
        return 403;
    }
    root /opt/app/code;
}
```
以上配置限制只有当请求头不携带"Referer"字段。或者请求头携带"Referer"字段但是其值已被防火墙或者代理服务器删除。是一些不携带"http://"或者"https://"的值, 或者referer为127.0.0.1时才可以访问服务器资源。除此之外都会返回403的HTTP状态码。具体可以参考[官方文档](http://nginx.org/en/docs/http/ngx_http_referer_module.html)。




