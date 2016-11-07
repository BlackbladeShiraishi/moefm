[![Build Status](https://travis-ci.org/BlackbladeShiraishi/moefm.svg?branch=master)](https://travis-ci.org/BlackbladeShiraishi/moefm)
[![Bintray](https://img.shields.io/bintray/v/blackbladeshiraishi/generic/moefm.svg)](https://bintray.com/blackbladeshiraishi/generic/moefm/_latestVersion)

# 电台应用

一个 [moe.fm](http://moe.fm/) Android 客户端。

## 功能

- [x] 主页（热曲、新曲）
- [x] 浏览电台
- [x] 浏览专辑
- [x] 搜索音乐
- [x] 音乐播放
- [x] 播放列表
- [ ] 账户管理（登录、个人信息管理等）
- [ ] 收藏

## 下载

| Branch | Link |
| --- | --- |
| nightly | https://dl.bintray.com/blackbladeshiraishi/generic/fm/moe/android/nightly/apk/android-debug.apk |
| stable | TBD |

## build
1. 安装配置JDK、Android SDK等
2. 在 code/client/android 目录的 settings.properties 文件中配置 api key 等信息。默认配置仅用于测试。settings.properties.example 中包含各项配置的说明。
3. `cd code`
4. `./gradlew installDebug`

## 文档
项目结构等资料见document目录。

## License
Apache License, Version 2.0
```text
Copyright 2016 Blackblade

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
