/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.aws.sqs.command.model;

import com.epion_t3.aws.sqs.command.runner.AwsSqsPurgeQueueRunner;
import com.epion_t3.core.common.annotation.CommandDefinition;
import lombok.Getter;
import lombok.Setter;

/**
 * SQSからのメッセージクリア（Purge）コマンド.
 */
@Getter
@Setter
@CommandDefinition(id = "AwsSqsPurgeQueue", runner = AwsSqsPurgeQueueRunner.class)
public class AwsSqsPurgeQueue extends AwsSqsBase {
}
