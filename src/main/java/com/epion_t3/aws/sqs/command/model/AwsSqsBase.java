/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.aws.sqs.command.model;

import com.epion_t3.core.common.bean.scenario.Command;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwsSqsBase extends Command {
    private String credentialsConfigRef;
    private String sdkHttpClientConfigRef;
}
