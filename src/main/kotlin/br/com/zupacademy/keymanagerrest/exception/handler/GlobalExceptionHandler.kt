package br.com.zupacademy.keymanagerrest.exception.handler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class GlobalExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>?, exception: StatusRuntimeException): HttpResponse<Any> {

        val statusCode = exception.status.code
        val statusDescription = exception.status.description

        val error = when(statusCode) {
            Status.Code.INVALID_ARGUMENT -> Pair(HttpStatus.BAD_REQUEST, statusDescription)
            Status.Code.ALREADY_EXISTS -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, statusDescription)
            Status.Code.NOT_FOUND -> Pair(HttpStatus.NOT_FOUND, statusDescription)
            else -> {
                logger.error("erro inesperado ${exception.javaClass.name} ao processar $request", exception)
                Pair(HttpStatus.INTERNAL_SERVER_ERROR, "não foi possível processar a request devido a: " +
                        "$statusDescription ($statusCode)")
            }
        }

        return HttpResponse
            .status<JsonError>(error.first)
            .body(JsonError(error.second))
    }
}