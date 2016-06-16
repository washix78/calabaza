開発を始める前に以下を実施する。

# JREの準備

1. JREをダウンロードし、以下に配置する。
   `calabaza/src/main/resources/`
1. ファイル`calabaza/build.properties`の以下の行を書き換える。
   `dir.jre=${jre directory-name}`
1. ファイル`calabaza/src/main/resources/calabaza.bat`の以下の行を書き換える。
   `set JRE_HOME=./bin/${jre directory-name}`

# 画像ファイルの準備

1. 画像ファイルを以下に配置する。
   `calabaza/webapp/mini/image/`

# Apache Antの実行

1. ターゲット`develop`を実行する。
   `ant develop`

# プロパティファイルの書き換え

1. ファイル`calabaza/main/resources/calabaza.properties`を環境に合わせた内容で書き換える。
