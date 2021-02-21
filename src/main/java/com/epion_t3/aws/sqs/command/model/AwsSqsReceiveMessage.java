/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.aws.sqs.command.model;

import com.epion_t3.aws.sqs.command.runner.AwsSqsReceiveMessageRunner;
import com.epion_t3.core.common.annotation.CommandDefinition;
import lombok.Getter;
import lombok.Setter;

/**
 * SQSからのメッセージ受信コマンド.
 */
@Getter
@Setter
@CommandDefinition(id = "AwsSqsReceiveMessage", runner = AwsSqsReceiveMessageRunner.class)
public class AwsSqsReceiveMessage extends AwsSqsBase {
    private Integer maxNumberOfMessages = 1;
    private Integer visibilityTimeout;
    /**
     * For Long Polling
     */
    private Integer waitTimeSeconds;
    private boolean deleteMessage = false;
}
