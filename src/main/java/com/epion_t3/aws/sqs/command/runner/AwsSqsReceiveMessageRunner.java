/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.aws.sqs.command.runner;

import com.epion_t3.aws.core.configuration.AwsCredentialsProviderConfiguration;
import com.epion_t3.aws.core.holder.AwsCredentialsProviderHolder;
import com.epion_t3.aws.sqs.command.model.AwsSqsReceiveMessage;
import com.epion_t3.aws.sqs.command.model.SqsMessageInfo;
import com.epion_t3.aws.sqs.messages.AwsSqsMessages;
import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.exception.SystemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.io.FileOutputStream;
import java.util.stream.Collectors;

/**
 * メッセージ受信実行処理.
 */
@Slf4j
public class AwsSqsReceiveMessageRunner extends AbstractCommandRunner<AwsSqsReceiveMessage> {

    /**
     * 変換処理用
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(AwsSqsReceiveMessage command, Logger logger) throws Exception {

        var awsCredentialsProviderConfiguration = (AwsCredentialsProviderConfiguration) referConfiguration(
                command.getCredentialsConfigRef());

        var credentialsProvider = AwsCredentialsProviderHolder.getInstance()
                .getCredentialsProvider(awsCredentialsProviderConfiguration);

        // SQSクライアントを生成
        var sqs = SqsClient.builder().credentialsProvider(credentialsProvider).build();

        // リクエスト構築処理を生成
        var requestBuilder = ReceiveMessageRequest.builder().queueUrl(command.getTarget());

        // 初期値は1とする
        requestBuilder
                .maxNumberOfMessages(command.getMaxNumberOfMessages() == null ? 1 : command.getMaxNumberOfMessages());

        // 可視性タイムアウトの設定は指定されているときのみ適用
        if (command.getVisibilityTimeout() != null) {
            requestBuilder.visibilityTimeout(command.getVisibilityTimeout());
        }

        // Waitタイムアウトの設定は指定されているときのみ適用
        if (command.getWaitTimeSeconds() != null) {
            requestBuilder.waitTimeSeconds(command.getWaitTimeSeconds());
        }

        var evidencePath = getEvidencePath("receiveMessages.json");

        // 送信
        try {
            var response = sqs.receiveMessage(requestBuilder.build());
            logger.info("receive complete message. num:{}",
                    response.messages() != null ? response.messages().size() : 0);
            var receiveMessages = response.messages()
                    .stream()
                    .map(x -> new SqsMessageInfo(x.messageId(), x.receiptHandle(), x.body(), x.attributesAsStrings(),
                            x.md5OfBody(), x.md5OfMessageAttributes()))
                    .collect(Collectors.toList());

            // ファイルエビデンス書き出し
            try (var fos = new FileOutputStream(evidencePath.toFile())) {
                objectMapper.writeValue(fos, receiveMessages);
            }

            // オブジェクトエビデンス登録
            registrationObjectEvidence(receiveMessages);

            // ファイルエビデンス登録
            registrationFileEvidence(evidencePath);

            // メッセージの削除を行う場合は削除処理を実施
            if (command.isDeleteMessage() && response.hasMessages()) {
                logger.info("delete message. target num : {}",
                        response.messages() != null ? response.messages().size() : 0);
                var deleteMessageRequest = DeleteMessageBatchRequest.builder()
                        .queueUrl(command.getTarget())
                        .entries(response.messages()
                                .stream()
                                .map(x -> DeleteMessageBatchRequestEntry.builder()
                                        .receiptHandle(x.receiptHandle())
                                        .build())
                                .collect(Collectors.toList()))
                        .build();
                var deleteMessageBatchResponse = sqs.deleteMessageBatch(deleteMessageRequest);
                if (deleteMessageBatchResponse.hasFailed()) {
                    deleteMessageBatchResponse.failed().forEach(x -> {
                        logger.error("fail delete message. id : {}, code : {}, message : {}", x.id(), x.code(),
                                x.message());
                    });
                    throw new SystemException(AwsSqsMessages.AWS_SQS_ERR_9003, command.getTarget());
                }
            }
        } catch (Exception e) {
            // 送信エラー
            throw new SystemException(e, AwsSqsMessages.AWS_SQS_ERR_9002, command.getTarget());
        }
        return CommandResult.getSuccess();
    }
}
