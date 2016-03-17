# 项目结构

## 语言
Java、Groovy
```text
src
├─main
│  ├─groovy
│  └─java
└─test
    ├─groovy
    └─java
```
在 src/main/java 目录放纯 Java 代码，这里的代码不能调用 src/main/groovy 里的代码。

在 src/main/groovy 目录放 Groovy 和 Java 代码，这里的代码（包括 Java 代码）可以调用java、groovy中的代码。

test 等其他 source set 与此类似。

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

上层模块的外部依赖比下层多，例如 client:android 模块依赖于 Android 平台的 API，而其他模块不依赖于 Android。

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

## GUI
系统 GUI 使用 [MVP](https://zh.wikipedia.org/wiki/Model_View_Presenter) 模式实现。

View **接口**定义、Presenters 在 facade 模块，View 的实现在各自平台的 client 模块。

Presenters 保证在 UI 线程调用 View 的方法， View 接口的实现不必是线程安全的。
