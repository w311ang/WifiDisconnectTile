# WifiDisconnectTile

一个 Android 快速设置磁贴（Quick Settings Tile）应用，点击即可断开当前 Wi-Fi 连接。

## 功能

- 📶 **Wi-Fi 已连接**：Tile 显示**启用状态**（高亮），点击后断开 Wi-Fi
- 📵 **Wi-Fi 未连接**：Tile 显示**停用状态**（灰色），点击后提示未连接
- 🔄 **实时刷新**：Wi-Fi 状态变化时 Tile 自动更新

## 系统要求

- Android 7.0 (API 24) 及以上
- 支持 Android 10+ 的断开逻辑

## 使用方法

1. 安装 APK
2. 下拉通知栏 → 点击右上角**编辑**图标
3. 找到 **"Wi-Fi 断开磁贴"**，拖入快捷面板
4. 点击 Tile 即可断开当前 Wi-Fi

## 权限说明

| 权限 | 用途 |
|---|---|
| `ACCESS_NETWORK_STATE` | 检测当前网络状态 |
| `ACCESS_WIFI_STATE` | 获取 Wi-Fi 连接信息 |
| `CHANGE_WIFI_STATE` | 断开 Wi-Fi 连接 |

## 项目结构

```
app/src/main/
├── AndroidManifest.xml
├── java/com/example/wifidisconnect/
│   └── WifiDisconnectTileService.kt   # 核心 Tile 服务
└── res/
    ├── drawable/
    │   ├── ic_wifi_on.xml             # Wi-Fi 已连接图标
    │   └── ic_wifi_off.xml            # Wi-Fi 未连接图标
    └── values/
        └── strings.xml               # 文案资源
```
