/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.aws.sqs.command.runner;

import com.epion_t3.aws.core.configuration.AwsCredentialsProviderConfiguration;
import com.epion_t3.aws.core.holder.AwsCredentialsProviderHolder;
import com.epion_t3.aws.sqs.command.model.AwsSqsSendMessage;
import com.epion_t3.aws.sqs.messages.AwsSqsMessages;
import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * メッセージ送信実行処理.
 */
@Slf4j
public class AwsSqsSendMessageRunner extends AbstractCommandRunner<AwsSqsSendMessage> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(AwsSqsSendMessage command, Logger logger) throws Exception {

        var awsCredentialsProviderConfiguration = (AwsCredentialsProviderConfiguration) referConfiguration(
                command.getCredentialsConfigRef());

        var credentialsProvider = AwsCredentialsProviderHolder.getInstance()
                .getCredentialsProvider(awsCredentialsProviderConfiguration);

        // SQSクライアントを生成
        var sqs = SqsClient.builder().credentialsProvider(credentialsProvider).build();

        // リクエスト構築処理を生成
        var requestBuilder = SendMessageRequest.builder().queueUrl(command.getTarget());

        // メッセージグループIDの設定（値が設定されている場合には設定値を優先）
        if (command.isAutoGenerateMessageGroupId()) {
            if (StringUtils.isEmpty(command.getMessageGroupId())) {
                var messageGroupId = UUID.randomUUID().toString();
                logger.info("generate message group id : {}", messageGroupId);
                requestBuilder.messageGroupId(messageGroupId);
            } else {
                logger.info("set message group id : {}", command.getMessageGroupId());
                requestBuilder.messageGroupId(command.getMessageGroupId());
            }
        } else {
            if (StringUtils.isEmpty(command.getMessageGroupId())) {
                logger.info("not set message group id : null");
            } else {
                logger.info("set message group id : {}", command.getMessageGroupId());
                requestBuilder.messageDeduplicationId(command.getMessageGroupId());
            }
        }

        // メッセージ重複排除IDの設定（値が設定されている場合には設定値を優先）
        if (command.isAutoMessageDeduplicationId()) {
            if (StringUtils.isEmpty(command.getMessageDeduplicationId())) {
                var messageDeduplicationId = UUID.randomUUID().toString();
                logger.info("generate message deduplication id : {}", messageDeduplicationId);
                requestBuilder.messageDeduplicationId(messageDeduplicationId);
            } else {
                logger.info("set message deduplication id : {}", command.getMessageDeduplicationId());
                requestBuilder.messageDeduplicationId(command.getMessageDeduplicationId());
            }
        } else {
            if (StringUtils.isEmpty(command.getMessageDeduplicationId())) {
                logger.info("not set message deduplication id : null");
            } else {
                logger.info("set message deduplication id : {}", command.getMessageDeduplicationId());
                requestBuilder.messageDeduplicationId(command.getMessageDeduplicationId());
            }
        }

        // メッセージBodyを設定
        requestBuilder.messageBody(command.getValue());

        // メッセージ属性を設定
        if (command.getMessageAttributes() != null) {
            var msgAttrs = new LinkedHashMap<String, MessageAttributeValue>();
            command.getMessageAttributes().forEach((k, v) -> {
                var msgAttr = MessageAttributeValue.builder().stringValue(v).build();
                msgAttrs.put(k, msgAttr);
            });
            requestBuilder.messageAttributes(msgAttrs);
        }

        // 送信
        try {
            var response = sqs.sendMessage(requestBuilder.build());
            logger.info("send complete message. messageId:{}, sequenceNumber:{}", response.messageId(),
                    response.sequenceNumber());
            return CommandResult.getSuccess();
        } catch (Exception e) {
            // 送信エラー
            throw new SystemException(e, AwsSqsMessages.AWS_SQS_ERR_9001, command.getTarget());
        }
    }
}
