/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.aws.sqs.command.runner;

import com.epion_t3.aws.core.configuration.AwsCredentialsProviderConfiguration;
import com.epion_t3.aws.core.holder.AwsCredentialsProviderHolder;
import com.epion_t3.aws.sqs.command.model.AwsSqsPurgeQueue;
import com.epion_t3.aws.sqs.messages.AwsSqsMessages;
import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest;

/**
 * Queueのクリア実行処理.
 */
@Slf4j
public class AwsSqsPurgeQueueRunner extends AbstractCommandRunner<AwsSqsPurgeQueue> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(AwsSqsPurgeQueue command, Logger logger) throws Exception {

        var awsCredentialsProviderConfiguration = (AwsCredentialsProviderConfiguration) referConfiguration(
                command.getCredentialsConfigRef());

        var credentialsProvider = AwsCredentialsProviderHolder.getInstance()
                .getCredentialsProvider(awsCredentialsProviderConfiguration);

        // SQSクライアントを生成
        var sqs = SqsClient.builder().credentialsProvider(credentialsProvider).build();

        // リクエスト構築処理を生成
        var requestBuilder = PurgeQueueRequest.builder().queueUrl(command.getTarget());

        // 送信
        try {
            var response = sqs.purgeQueue(requestBuilder.build());
            logger.info("purge queue complete.");
            return CommandResult.getSuccess();
        } catch (Exception e) {
            // 送信エラー
            throw new SystemException(e, AwsSqsMessages.AWS_SQS_ERR_9004, command.getTarget());
        }
    }
}
