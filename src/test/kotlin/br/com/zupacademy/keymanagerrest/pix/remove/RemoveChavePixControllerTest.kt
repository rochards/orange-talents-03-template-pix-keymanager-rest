package br.com.zupacademy.keymanagerrest.pix.remove

import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerRemoveServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.RemoveChavePixRequest
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import br.com.zupacademy.keymanagergrpc.grpc.RemoveChavePixResponse as GrpcRemoveChavePixResponse

@MicronautTest
internal class RemoveChavePixControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var grpcClient: KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub

    @AfterEach
    internal fun tearDown() {
        reset(grpcClient)
    }

    @Test
    fun `deve remover uma chave pix`() {
        val clienteId = UUID.randomUUID().toString()
        val pixId = 1L
        val httpRequest = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteId/pix/$pixId")

        `when`(
            grpcClient.removeChavePix(
                RemoveChavePixRequest.newBuilder()
                    .setChaveId(pixId)
                    .setErpClienteId(clienteId)
                    .build()
            )
        )
            .thenReturn(
                GrpcRemoveChavePixResponse.newBuilder()
                    .setChave("pix@gmail.com")
                    .setErpClienteId(clienteId)
                    .build()
            )


        val response = httpClient.toBlocking().exchange(httpRequest, Any::class.java)


        with(response) {
            assertEquals(HttpStatus.OK, this.status)
            assertTrue(this?.body().toString().contains("clienteId=$clienteId"))
            assertTrue(this?.body().toString().contains("chave=pix@gmail.com"))
        }
    }

    @Singleton
    @Replaces(bean = KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub::class)
    fun grpcClient() = mock(KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub::class.java)
}