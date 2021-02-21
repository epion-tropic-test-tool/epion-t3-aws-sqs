/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.aws.sqs.command.model;

import com.epion_t3.aws.sqs.command.runner.AwsSqsSendMessageRunner;
import com.epion_t3.core.common.annotation.CommandDefinition;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@CommandDefinition(id = "AwsSqsSendMessage", runner = AwsSqsSendMessageRunner.class)
public class AwsSqsSendMessage extends AwsSqsBase {
    private String messageGroupId;
    private String messageDeduplicationId;
    private boolean autoGenerateMessageGroupId = false;
    private boolean autoMessageDeduplicationId = false;
    private Map<String, String> messageAttributes;
}
