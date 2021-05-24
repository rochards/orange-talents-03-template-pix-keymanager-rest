package br.com.zupacademy.keymanagerrest.exception.handler

import io.grpc.Status
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    private val request = HttpRequest.GET<Any>("/")

    @Test
    fun `deve retornar 400 quando o retorno da chamada gRPC for INVALID_ARGUMENT`() {

        val mensagem = "dados da requisição inválidos"
        val excecao = Status.INVALID_ARGUMENT.withDescription(mensagem).asRuntimeException()

        val resposta = GlobalExceptionHandler().handle(request, excecao)

        with(resposta) {
            assertEquals(HttpStatus.BAD_REQUEST, this.status)
            assertNotNull(resposta.body())
            assertEquals(mensagem, (resposta.body() as JsonError).message)
        }
    }

    @Test
    fun `deve retornar 404 quando o retorno da chamada gRPC for NOT_FOUND`() {

        val mensagem = "cliente não encontrado"
        val excecao = Status.NOT_FOUND.withDescription(mensagem).asRuntimeException()

        val resposta = GlobalExceptionHandler().handle(request, excecao)

        with(resposta) {
            assertEquals(HttpStatus.NOT_FOUND, this.status)
            assertNotNull(resposta.body())
            assertEquals(mensagem, (resposta.body() as JsonError).message)
        }
    }

    @Test
    fun `deve retornar 422 quando o retorno da chamada gRPC for ALREADY_EXISTS`() {

        val mensagem = "cliente já cadastrado"
        val excecao = Status.ALREADY_EXISTS.withDescription(mensagem).asRuntimeException()

        val resposta = GlobalExceptionHandler().handle(request, excecao)

        with(resposta) {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.status)
            assertNotNull(resposta.body())
            assertEquals(mensagem, (resposta.body() as JsonError).message)
        }
    }

    @Test
    fun `deve retornar 500 quando o retorno da chamada gRPC for diferente das descritas acima`() {

        val mensagem = "algo inesperado aconteceu"
        val excecao = Status.INTERNAL.withDescription(mensagem).asRuntimeException()

        val resposta = GlobalExceptionHandler().handle(request, excecao)

        with(resposta) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, this.status)
            assertNotNull(resposta.body())
            assertTrue((resposta.body() as JsonError).message.contains("INTERNAL"))
        }
    }
}