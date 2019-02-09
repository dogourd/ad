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
        2. sub模块
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