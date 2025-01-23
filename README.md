# ServerCommand

一个简化跨服传送命令的Velocity插件。

## 功能

### 命令
- `/sc <server>` 或 `/服务器 <服务器>` - 传送至指定服务器
  - 需要权限: `servercommand.use`
  - 支持服务器别名（在配置文件中设置）

- `/hub` 或 `/大厅` - 快速返回主服务器
  - 无需特殊权限
  - 目标服务器可在配置中自定义

- 直接传送命令
  - `/生存` - 直接传送到生存服务器
  - `/创造` - 直接传送到创造服务器
  - `/空岛` - 直接传送到空岛服务器
  - 无需特殊权限

### 配置文件
```yaml
# hub命令目标服务器名称
hub-server: "hub"

# 服务器别名配置
server-aliases:
  hub: "lobby-1"      # 大厅服务器
  lobby: "lobby-1"    # 大厅服务器
  "大厅": "lobby-1"   # 大厅服务器
  
  survival: "srv-1"   # 生存服务器
  "生存": "srv-1"     # 生存服务器
  
  creative: "crt-1"   # 创造服务器
  "创造": "crt-1"     # 创造服务器
  
  skyblock: "sky-1"   # 空岛服务器
  "空岛": "sky-1"     # 空岛服务器
```