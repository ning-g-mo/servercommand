# ServerCommand

一个简化跨服传送命令的Velocity插件。

## 功能

### 命令
- `/sc <server>` - 传送至指定服务器
  - 需要权限: `servercommand.use`
  - 支持服务器别名（在配置文件中设置）

- `/hub` - 快速返回主服务器
  - 无需特殊权限
  - 目标服务器可在配置中自定义

### 配置文件
```yaml
#hub命令目标服务器名称
hub-server: "hub"
#服务器别名配置
server-aliases:
  hub: "lobby-1" # 使用 /sc hub 会传送到 lobby-1 服务器
  survival: "srv-1" # 使用 /sc survival 会传送到 srv-1 服务器
  creative: "crt-1" # 使用 /sc creative 会传送到 crt-1 服务器
```