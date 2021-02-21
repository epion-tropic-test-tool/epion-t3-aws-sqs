#  カスタム機能ドキュメント

このドキュメントは、 のカスタム機能が提供する、
Flow、コマンド、設定定義についての説明及び出力するメッセージの定義について説明する。

- Contents
  - [Information](#Information)
  - [Description](#Description)
  - [Flow List](#Flow-List)
  - [Command List](#Command-List)
  - [Configuration List](#Configuration-List)
  - [Message List](#Message-List)

## Information

本カスタム機能の基本情報は以下の通り。

AWSのSQSへのアクセスを行う機能を提供します。

- Name : `aws-sqs`
- Custom Package : `com.epion_t3.aws.sqs`

## Description
AmazonWebService（AWS）のSimpleQueueService（SQS）への各種アクセスを行う機能を提供します。事前データの配置、エビデンスの取得などに利用できるPUT、GETを代表とした機能です。

## Flow List

本カスタム機能が提供するFlowの一覧及び詳細。

|Name|Summary|
|:---|:---|


## Command List

本カスタム機能が提供するコマンドの一覧及び詳細。

|Name|Summary|Assert|Evidence|
|:---|:---|:---|:---|
|[AwsSqsSendMessage](#AwsSqsSendMessage)|SQSに対してメッセージの送信を行います。  |||

------

### AwsSqsSendMessage
SQSに対してメッセージの送信を行います。
#### Command Type
- Assert : No
- Evidence : No

#### Functions
- Excelの全てのシートに対して変数をバインドします。
- 変数の参照記法「${スコープ.変数名}」が記載してあるセルの値を置換します。（部分置換を行えます）
- セルの型が「文字列」である場合にのみ置換します。
- 置換後のExcelファイルは、エビデンスとして保存します。
- エビデンスファイル名は、「BindVariables_ + 元ファイル名」となります。

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「AwsSqsSendMessage」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  target : 対象とするSQSのEndpoint
  value : 対象とするメッセージ
  messageGroupId : メッセージグループID
  messageDeduplicationId : メッセージ重複排除ID
  autoGenerateMessageGroupId : メッセージグループIDの自動生成フラグ
  autoMessageDeduplicationId : メッセージ重複排除IDの自動生成フラグ

```

1. 真偽値 &#96;true&#96; or &#96;false&#96; で指定します。
1. メッセージグループIDが設定されていた場合、そちらが優先されます。
1. メッセージグループIDの生成はUUIDで行われます。
1. 真偽値 &#96;true&#96; or &#96;false&#96; で指定します。
1. メッセージ重複排除IDが設定されていた場合、そちらが優先されます。
1. メッセージ重複排除IDの生成はUUIDで行われます。

## Configuration List

本カスタム機能が提供する設定定義の一覧及び詳細。

|Name|Summary|
|:---|:---|


## Message List

本カスタム機能が出力するメッセージの一覧及び内容。

|MessageID|MessageContents|
|:---|:---|
|com.epion_t3.aws.sqs.err.9001|対象（target）のEndpointへのメッセージ送信に失敗しました.SQS:{0}|
