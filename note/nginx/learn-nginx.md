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
        <td colspan="2">events</td>
        <table>
            <tr>
                <td>worker_connections</td>
                <td>每个进程允许最大连接数</td>
            </tr>
            <tr>
                <td>use</td>
                <td>工作进程数</td>
            </tr>
        </table>
    </tr>
</table>
http模块中的配置语法  

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
