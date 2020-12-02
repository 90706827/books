# Flutter 配置

# Windows环境配置

### Flutter SDK 下载

- [Flutter SDK](https://flutter.dev/docs/get-started/install/windows) 

  下载后放置目录：D:\flutter

- 配置系统变量

  Path ： D:\flutter\bin

### Android Studio 安装
- Android Studio 下载 [DOWNLOAD ANDROID STUDIO ](https://developer.android.com/studio)

- 执行安装文件

  android-studio-ide-201.6953283-windows.exe

- 配置Plugins Flutter Dart  Kotlin

  ![](img\flutter-pz1.png)

- 配置Android Virtual Device Manager

  ![](img\flutter-pz2.png)

  ![flutter-pz3](img\flutter-pz3.png)

  ![flutter-pz4](img\flutter-pz4.png)

  ![flutter-pz5](img\flutter-pz5.png)

- 创建Flutter工程

  ![](img\flutter-new1.png)

  ![flutter-new2](img\flutter-new2.png)

  ![flutter-new3](img\flutter-new3.png)

### Flutter Doctor

- 更新Flutter

  ```bash
  flutter upgrade
  ```

- Flutter Android 授权

  ```bash
  flutter doctor --android-licenses
  ```

- 检查Flutter

  ```bash
  flutter doctor
  ```

### 问题解决

- flutter pub get 慢

  配置环境变量：

  ```bash
  PUB_HOSTED_URL == https://pub.flutter-io.cn
  FLUTTER_STORAGE_BASE_URL == https://storage.flutter-io.cn
  ```

  

- flutter tools配置

  D:\flutter\packages\flutter_tools\gradle

  修改flutter.gradle文件

  ```groovy
  buildscript {
      repositories {
          // 使用国内阿里云的代理
  		// google()
  		// jcenter()
          maven { url 'https://maven.aliyun.com/repository/google' }
          maven { url 'https://maven.aliyun.com/repository/jcenter' }
          maven { url 'https://maven.aliyun.com/nexus/content/groups/public' }
      }
      dependencies {
          classpath 'com.android.tools.build:gradle:3.5.0'
      }
  }
  
  ```

  





