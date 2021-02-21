/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.aws.sqs.command.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * SQSに登録されたメッセージを表すオブジェクト.
 *
 * @author Nozomu Takashima.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SqsMessageInfo implements Serializable {
    private String messageId;
    private String receiptHandle;
    private String body;
    private Map<String, String> attributes;
    private String md5OfBody;
    private String md5OfMessageAttributes;
}
