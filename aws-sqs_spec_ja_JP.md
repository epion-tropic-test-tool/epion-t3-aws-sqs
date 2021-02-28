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
|[AwsSqsReceiveMessage](#AwsSqsReceiveMessage)|SQSからメッセージの取得を行います。  ||X|
|[AwsSqsSendMessage](#AwsSqsSendMessage)|SQSに対してメッセージの送信を行います。  |||

------

### AwsSqsReceiveMessage
SQSからメッセージの取得を行います。
#### Command Type
- Assert : No
- Evidence : __Yes__

#### Functions
- SQSからメッセージの取得を行います。
- 取得されたメッセージは、エビデンスとして保存します。ファイル名は「receiveMessages.json」で固定です。
- 受信したメッセージについて削除が必要な場合は削除処理をこのコマンド中に実施します。
- 可視性タイムアウトの設定が可能です。
- Waitタイムアウトの設定が可能です。ロングポーリングへの利用が想定している利用方法です。

#### Structure
```yaml
commands : 
  id : コマンドのID
  command : 「AwsSqsReceiveMessage」固定
  summary : コマンドの概要（任意）
  description : コマンドの詳細（任意）
  target : 対象とするSQSのEndpoint
  maxNumberOfMessages : 同時に受信するメッセージの数。デフォルトは1です。10までの値で指定してください。
  visibilityTimeout : 可視性タイムアウトの秒数を指定してください。
  waitTimeSeconds : Waitタイムアウトの秒数を指定してください。
  deleteMessage : 受信したメッセージをQueueから削除するかの指定。デフォルトは「false」となり削除しません。

```

------

### AwsSqsSendMessage
SQSに対してメッセージの送信を行います。
#### Command Type
- Assert : No
- Evidence : No

#### Functions
- SQSに対してメッセージの送信を行います。
- メッセージボディと属性を指定可能です。
- メッセージグループID、メッセージ重複IDの設定については任意の値の設定と自動設定もサポートします。
- それぞれの値について変数のバインドが適用可能です。

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
  autoGenerateMessageGroupId : メッセージグループIDの自動生成フラグ <1><2><3>
  autoMessageDeduplicationId : メッセージ重複排除IDの自動生成フラグ <4><5><6>

```

1. 真偽値 true or false で指定します。
1. メッセージグループIDが設定されていた場合、そちらが優先されます。
1. メッセージグループIDの生成はUUIDで行われます。
1. 真偽値 true or false で指定します。
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
|com.epion_t3.aws.sqs.err.9004|対象（target）のEndpointからのメッセージクリアに失敗しました.SQS:{0}|
|com.epion_t3.aws.sqs.err.9002|対象（target）のEndpointからのメッセージ受信に失敗しました.SQS:{0}|
|com.epion_t3.aws.sqs.err.9003|対象（target）のEndpointからのメッセージ削除に失敗しました.SQS:{0}|
|com.epion_t3.aws.sqs.err.9001|対象（target）のEndpointへのメッセージ送信に失敗しました.SQS:{0}|
