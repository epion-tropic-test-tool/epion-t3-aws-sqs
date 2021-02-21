/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.aws.sqs.messages;

import com.epion_t3.core.message.Messages;

/**
 * aws-sqs用メッセージ定義Enum.<br>
 *
 * @author epion-t3-devtools
 */
public enum AwsSqsMessages implements Messages {

    /** 対象（target）のEndpointからのメッセージ受信に失敗しました.SQS:{0} */
    AWS_SQS_ERR_9002("com.epion_t3.aws.sqs.err.9002"),

    /** 対象（target）のEndpointからのメッセージ削除に失敗しました.SQS:{0} */
    AWS_SQS_ERR_9003("com.epion_t3.aws.sqs.err.9003"),

    /** 対象（target）のEndpointへのメッセージ送信に失敗しました.SQS:{0} */
    AWS_SQS_ERR_9001("com.epion_t3.aws.sqs.err.9001");

    /** メッセージコード */
    private String messageCode;

    /**
     * プライベートコンストラクタ<br>
     *
     * @param messageCode メッセージコード
     */
    private AwsSqsMessages(final String messageCode) {
        this.messageCode = messageCode;
    }

    /**
     * messageCodeを取得します.<br>
     *
     * @return messageCode
     */
    public String getMessageCode() {
        return this.messageCode;
    }
}
