package com.takizuka.apigatewaylambdaspring.functions

import com.takizuka.apigatewaylambdaspring.model.Input
import org.slf4j.LoggerFactory.getLogger
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import java.util.function.Function

class ApiGatewayEventFunction : Function<Message<Input>, Message<String>> {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = getLogger(javaClass.enclosingClass)
    }

    override fun apply(inputMessage: Message<Input>): Message<String> {
        logger.info("Message : " + inputMessage.payload.test)
        return MessageBuilder
            .withPayload("Completed!")
            .build()
    }
}