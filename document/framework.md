# 项目结构

## 语言
Java、Groovy

## 平台
目前支持Android

## 框架
整体轮廓遵循[The Clean Architecture](https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html)。

## 模块
```text
+-------------------------+
|                         |
|         client          |
|                         |
+-------------------------+
|                         |
|         facade          |
|                         |
+-------------------------+
|                         |
|   business  impl-moefm  |
|                         |
+-------------------------+
|                         |
|         domain          |
|                         |
+-------------------------+
```
上层模块依赖于下层模块。

### domain
领域模型
* 领域实体类

### business
业务模型
* 数据获取
* 播放逻辑（Player接口、PlayList等）

impl-moefm是business中定义的一些接口的**其中一种**实现。项目可以因为服务平台、功能划分等因素有多个实现（目前只有一个）。

### facade
门面
* Presenter
* View接口

### client
客户端。可以针对不同平台有多个实现（目前只有Android实现）。
* View接口的实现
* Player接口实现
* 平台特定代码
