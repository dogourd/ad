# Nginx 默认配置语法
<table>
    <tr>
        <td>user</td>
        <td>设置Nginx服务使用的系统用户</td>
    </tr>
    <tr>
        <td>worker_processes</td>
        <td>工作进程数</td>
    </tr>
    <tr>
        <td>error_log</td>
        <td>Nginx错误日志</td>
    </tr>
    <tr>
        <td>pid</td>
        <td>Nginx服务启动pid</td>
    </tr>
</table>
<table>
    <tr>
        <td rowspan="2">events</td>
        <td>worker_connections</td>
        <td>每个进程允许最大连接数</td>
    </tr>
    <tr>
        <td>use</td>
        <td>工作进程数</td>
    </tr>
</table>

- http模块中的配置语法  
```
http{
    ...
    server {
        listen 80;
        server_name localhost;
        location / {
            root /usr/share/nginx/html;
            index index.html index.htm;
        }
        error_page 500 502 503 504 /50.html;
        location = /50x.html {
            root /usr/share/nginx/html;
        }
    }
    server {
        ...
    }
}
```
- Nginx日志类型  
    - error.log 主要记录Nginx处理HTTP请求的错误状态以及Nginx本身运行服务的错误状态。会将其按照不同的级别记录在error.log中。
    - access_log 记录Nginx处理的HTTP请求的访问状态。用于分析请求和行为分析等。 
    - **log_format** 以上两种日志都要通过**log_format**来实现, log_format中可以标识记录很多信息, 每一个信息都对应了Nginx中的
    一个变量, log_format语法如下:
    
    ```
       语法: log_format name [escape=default|json] string...;
       默认值: log_format combined "...";
       上下文: http
    ```  
- Nginx变量
    - HTTP请求变量: arg_PARAMETER(HTTP请求携带的参数), http_HEADER(HTTP请求头信息, 如'$http_user_agent'), sent_http_HEADER
    (HTTP响应头信息) 
    - 内置变量: [Nginx内置](https://nginx.org/en/docs/http/ngx_http_core_module.html#var_status)
    - 自定义变量: 自定义(此处不作介绍, 后面Nginx + Lua 进行说明)   
- Nginx模块
    - Nginx官方模块    
        1. sub_status模块  
        <table>
            <tr>
                <td>编译选项</td>
                <td>作用</td>
            </tr>
            <tr>
                <td>--with-http_sub_status_module</td>
                <td>Nginx当前处理连接的状态</td>
            </tr>
        </table>
         
        ```
        语法: stub_status;
        默认: -
        上下文: server, location
        ```
        2. random index
        <table>
            <tr>
                <td>编译选项</td>
                <td>作用</td>
            </tr>
            <tr>
                <td>--with-http_random_index_module</td>
                <td>在目录中选择一个随机主页</td>
            </tr>
        </table>
        
        ```
         语法: random_index on | off;
         默认: random_index off;
         上下文: location
        ```
        3. sub模块
        <table>
            <tr>
                <td>编译选项</td>
                <td>作用</td>
            </tr>
            <tr>
                <td>--with-http_sub_module</td>
                <td>HTTP内容替换</td>
            </tr>
        </table>
        
        ```
        用作响应内容替换
        语法: sub_filter string replacement;
        默认: -
        上下文: http, server, location
        
        用作缓存
        语法: sub_filter_last_modified on | off;
        默认: sub_filter_last_modified off;
        上下文: http, server, location
        
        在响应内容替换时决定只匹配第一个还是全局替换
        语法: sub_filter_once on | off;
        默认: sub_filter once on;
        上下文: http, server, location
        ```
    - 第三方模块

- Nginx请求限制
    - 连接频率限制: **limit_conn_module**    
    - 请求频率限制: **limit_req_module**
    
    ```
    连接限制(预对连接进行限制则必要对其进行存储)
    
    开辟空间存储
    语法: limit_conn_zone key zone=name:size;
    默认: -
    上下文: http
    
    此处zone为上面声明的zone的name
    语法: limit_conn zone number;
    默认: -
    上下文: http, server, location
    
    请求限制
    
    语法: limit_req_zone key zone=name:size rate=rate;
    默认: -
    上下文: http
    
    语法: limit_req zone=name [burst=number] [nodelay];
    默认: -
    上下文: http, server, location
    ```
    
    例子(转载自[简书](https://www.jianshu.com/p/2cf3d9609af3)):  
    > 
       limit_req_zone $binary_remote_addr zone=mylimit:10m rate=10r/s;  
       server {  
           location /login/ {  
               limit_req zone=mylimit;  
               proxy_pass http://my_upstream;  
           }  
       }
    当limit_req 在它出现的环境中启用了限流（在上面的例子中，作用在所有对于/login/的请求上），则limit_req_zone指令定义了限流的参数。
    limit_req_zone指令一般定义在http块内部，使得该指令可以在多个环境中使用。该指令有下面三个参数：
      
    - **Key** — 在限流应用之前定义了请求的特征。在上面例子中，它是$binary_remote_addr（NGINX变量），该变量代表了某个客户端IP地址的
    二进制形式。这意味着我们可以将每个特定的IP地址的请求速率限制为第三个参数所定义的值。（使用这个变量的原因是因为它比用string代表客户端
    IP地址的$remote_addr变量消耗更少的空间。）
    
    - **Zone** — 定义了存储每个IP地址状态和它访问受限请求URL的频率的共享内存区域。将这些信息保存在共享内存中，意味着这些信息能够在NGINX
    工作进程之间共享。定义有两个部分：由zone=关键字标识的区域名称，以及冒号后面的区域大小。约16000个IP地址的状态信息消耗1M内存大小，
    因此我们的区域（zone）大概可以存储约160000个地址。当NGINX需要添加新的记录时，如果此时存储耗尽了，最老的记录会被移除。如果释放的
    存储空间还是无法容纳新的记录，NGINX返回 **503 (Service Temporarily Unavailable)** 状态码。此外，为了防止内存被耗尽，每次NGINX创建
    一个新的记录的同时移除多达两条前60秒内没有被使用的记录。
    
    - **Rate** — 设置最大的请求速率。在上面的例子中，速率不能超过10个请求每秒。NGINX事实上可以在毫秒级别追踪请求，因此这个限制对应了1个
    请求每100毫秒。因为我们不允许突刺（bursts，短时间内的突发流量，详细见下一部分。），这意味着如果某个请求到达的时间离前一个被允许的
    请求小于100毫秒，它会被拒绝。 
        
    limit_req_zone指令设置限流和共享内存区域的参数，但是该指令实际上并不限制请求速率。为了限制起作用，需要将该限制应用到某个特定的
    location或server块（block），通过包含一个limit_req指令的方式。在上面的例子中，我们将请求限制在/login/上。  

    所以现在对于/login/，每个特定的IP地址被限制为10个请求每秒— 或者更准确地说，不能在与前一个请求间隔100毫秒时间内发送请求。

- Nginx的访问控制
    - 基于IP的访问控制: http_access_module
        ```
        语法: allow address | CIDR | unix: | all;
        默认: -
        上下文: http, server, location, limit_except
        
        语法: deny address | CIDR | unix: | all;
        默认: -
        上下文: http, server, location, limit_except
        ```
        
        http_access_module的局限性: 对于我们ban掉的ip, 如果其通过代理来访问则无法对其进行限制。
        如配置  
        ```
        deny 192.168.1.4;  
        allow all;
        ```  
        则可以ban掉 **192.168.1.4**这个IP地址, 但若其通过代理来访问设为 *192.168.1.5*则其又可以访问服务器资源。
        因为http_access_module是通过**remote_addr**来限制的。  
      
        若: 客户端: IP1,   客户端代理: IP2,   Nginx: IP3。   
        那么当Nginx将IP1限制后客户端又通过代理IP2来访问则无法对其进行限制。
        因为无法获得原始客户端的ip地址。
        我们可以通过使用**http_x_forwarded_for**来进行限制, 此时若依然保持  
        
        客户端: IP1,   代理: IP2,  Nginx: IP3  
        那么最终会得到: x_forward_for = IP1, IP2即第一个值为原始客户端地址。这是解决http_access_module局限性的方案之一。 
        但是需要明白 x_forward_for只是一个协议要求并非所有的厂商都会按照要求对其进行实现。甚至可能被修改因为它只是一个头信息
        可以被客户端修改。 
        解决方式共有三种:  
            + 不采用remote_addr头, 而是采用其他HTTP头信息如: HTTP_X_FORWARD_FOR  
            + 结合Nginx geo模块使用  
            + 通过HTTP自定义变量进行传递  
    - 基于用户的信任登录: http_auth_basic_module  
    
        ```
        语法: auth_basic string | off;  
        默认: auth_basic off;  
        上下文: http, server, location, limit_except
        
        语法: auth_basic_user_file file;
        默认: -
        上下文: http, server, location, limit_except
        ```
        
        可以通过配合 htpasswd命令进行使用, CentOS可以通过
        `yum -y install httpd-tools`进行安装
        进入到/etc/nginx/目录下(可自定义, 在配置文件中填写
        正确路径即可)使用`htpasswd -c auth_conf $USERNAME`
        回车填写密码即可。
        [官方教程](http://nginx.org/en/docs/http/ngx_http_auth_basic_module.html)
        然后可以在配置文件中进行如下配置:  
        
        ```
        location ~ ^/admin.html {
            root /developer/html;
            auth_basic "Auth test, input your passwd~";
            auth_basic_user_file /etc/nginx/auth_conf;
            index index.html index.htm;
        }
        ```
        
        http_auth_basic_module的局限性:
        1. 用户信息依赖文件方式
        2. 操作管理比较机械, 效率低下  
        
        解决方案:  
        1. Nginx结合Lua高效验证  
        2. Nginx和LDAP打通, 利用nginx-auth-ldap模块
    