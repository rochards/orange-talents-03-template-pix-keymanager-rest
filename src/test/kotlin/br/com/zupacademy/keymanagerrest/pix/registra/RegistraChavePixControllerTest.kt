package br.com.zupacademy.keymanagerrest.pix.registra

import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerRegistraServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.RegistraChavePixRequest
import br.com.zupacademy.keymanagergrpc.grpc.RegistraChavePixResponse
import br.com.zupacademy.keymanagerrest.pix.TipoChave
import br.com.zupacademy.keymanagerrest.pix.TipoConta
import br.com.zupacademy.keymanagerrest.server.GrpcClientFactory
import io.grpc.Status
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@MicronautTest
internal class RegistraChavePixControllerTest {

    @Inject
    @field:Client("/") // não funciona sem o @field
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var grpcClient: KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub

    private val clienteId = UUID.randomUUID().toString()

    @AfterEach
    internal fun tearDown() {
//      necessária para que os testes, pois antes quem fazia esse passo era o @MockBean
        reset(grpcClient)
    }

    @Test
    fun `deve registrar uma nova chave pix`() {

        val novaChavePixRequest = NovaChavePixRequest("", TipoChave.RANDOM, TipoConta.CONTA_CORRENTE)
        val httpRequest = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", novaChavePixRequest)

        `when`(grpcClient.registraChavePix(novaChavePixRequest.toGrpcRequest(clienteId)))
            .thenReturn(
                RegistraChavePixResponse.newBuilder()
                    .setId(1L)
                    .build()
            )


        val response = httpClient.toBlocking().exchange(httpRequest, Any::class.java)


        with(response) {
            assertEquals(HttpStatus.CREATED, this.status)
            assertTrue(this.headers.contains("Location"))
            assertTrue(this.header("Location")!!.matches("/api/v1/clientes/$clienteId/pix/\\d".toRegex()))
        }
    }

    @Test
    fun `deve usar os valores default dos enums quando passados incorretamente`() {

        val httpRequest = HttpRequest
            .POST("/api/v1/clientes/$clienteId/pix",
                mapOf(
                    "chave" to "teste@gmail.com",
                    "tipoChave" to "ERRADA",
                    "tipoConta" to "ERRADA"
                )
            )

        `when`(grpcClient.registraChavePix(any(RegistraChavePixRequest::class.java)))
            .thenThrow(Status.INVALID_ARGUMENT.asRuntimeException())


        assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(httpRequest, Any::class.java)
        }


        verify(grpcClient).registraChavePix(
            RegistraChavePixRequest.newBuilder()
                .setErpClienteId(clienteId)
                .setChave("teste@gmail.com")
                .setTipoChave(br.com.zupacademy.keymanagergrpc.grpc.TipoChave.UNKNOWN_CHAVE)
                .setTipoConta(br.com.zupacademy.keymanagergrpc.grpc.TipoConta.UNKNOWN_CONTA)
                .build()
        )
    }

    @Test
    fun `nao deve aceitar uma request com campos nulos`() {

        val httpRequest = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", NovaChavePixRequest(null, null, null))

        val exception = assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(httpRequest, Any::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }

//    não conseguimos mockar o grpc pelo @MockBean
//    @MockBean(KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub::class)
//    fun grpcClient(): KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub {
//        return mock(KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub::class.java)
//    }

    @Singleton
    @Replaces(bean = KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub::class) // qdo o Micronaut
    // estiver subindo o contexto, essa anotação diz a ele para substituir a factory especificada, que foi criada por mim, pelo o mock abaixo
    fun grpcClient() = mock(KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub::class.java)
}