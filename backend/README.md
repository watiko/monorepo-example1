# API

## tips

### Doma の Interface しか存在しないクラスが Autowire できないと言われる

アノテーションプロセッサーで実装を生成しているので一回ビルドする。
この時 IntelliJ からではなく gradle のタスクを経由してビルドを行う必要がある。  
(kapt をサポートしていないため)

```bash
$ ./gradlew build
```

### IntelliJ IDEA のフォーマット設定を ktlint に合わせる

```bash
$ ./gradlew ktlintApplyToidea
```

### ファイルを変更した際にアプリケーションを再起動する on IntelliJ IDEA

クラスファイルが更新されるとそれを Spring Boot のクラスローダで読み出すので、
IntelliJ が変更したファイルをコンパイルするように設定する。

#### 方法 1

手動で Build->Build Project を選択する。予期せぬタイミングでの再起動が起こらないのでオススメ

#### 方法 2

1. Enable "Build project automatically" from Settings->Build, Execution, Deployment->Compiler
2. Go to Registry(Ctrl+A, type "Registry") and enable compiler.automake.allow.when.app.running and ide.windowSystem.autoShowProcessPopup

2 に関しては IntelliJ IDEA の外で bootRun していれば設定せずとも問題ない。

#### 方法 3

gradle 経由で起動している時は `./gradle classes` でも良い
