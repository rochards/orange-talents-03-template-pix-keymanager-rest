package br.com.zupacademy.keymanagerrest.pix.lista

import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerListaServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.TipoChave
import br.com.zupacademy.keymanagergrpc.grpc.TipoConta
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import br.com.zupacademy.keymanagergrpc.grpc.ListaChavesPixRequest as GrpcListaChavesPixRequest
import br.com.zupacademy.keymanagergrpc.grpc.ListaChavesPixResponse as GrpcListaChavesPixResponse

@MicronautTest
internal class ListaChavesPixControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var grpcClient: KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub

    private val clienteId = UUID.randomUUID().toString()

    @AfterEach
    internal fun tearDown() {
        reset(grpcClient)
    }

    @Test
    fun `deve retornar todas as chaves pix dado um clienteId`() {

        val httpRequest = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId")
        val grpcRequest = buildListaChavesPixRequest()
        val grpcResponse = buildListaChavesPixResponse()

        `when`(grpcClient.listaChavesPix(grpcRequest))
            .thenReturn(grpcResponse)


        val httpResponse = httpClient.toBlocking().exchange(httpRequest, ListaChavesPixResponse::class.java)
        
        with(httpResponse) {
            assertEquals(HttpStatus.OK, this.status)
            assertNotNull(this.body())
            assertFalse(this.getBody(ListaChavesPixResponse::class.java).isEmpty)
        }
    }

    private fun buildListaChavesPixRequest(): GrpcListaChavesPixRequest? {
        return GrpcListaChavesPixRequest.newBuilder()
            .setErpClienteId(clienteId)
            .build()
    }

    private fun buildListaChavesPixResponse(): GrpcListaChavesPixResponse {
        return GrpcListaChavesPixResponse.newBuilder()
            .setErpClienteId(clienteId)
            .addAllChaves(
                listOf(
                    GrpcListaChavesPixResponse.ChavePixResponse.newBuilder()
                        .setPixId(1L)
                        .setChave("parker.aranha@gmail.com")
                        .setTipoChave(TipoChave.EMAIL)
                        .setTipoConta(TipoConta.CONTA_CORRENTE)
                        .setCriadaEm(buildTimestamp())
                        .build(),
                    GrpcListaChavesPixResponse.ChavePixResponse.newBuilder()
                        .setPixId(2L)
                        .setChave(UUID.randomUUID().toString())
                        .setTipoChave(TipoChave.RANDOM)
                        .setTipoConta(TipoConta.CONTA_CORRENTE)
                        .setCriadaEm(buildTimestamp())
                        .build()
                )
            )
            .build()
    }

    private fun buildTimestamp(): Timestamp {
        return LocalDateTime.now().let {
            val instant = it.atZone(ZoneId.of("UTC")).toInstant()
            Timestamp.newBuilder()
                .setSeconds(instant.epochSecond)
                .setNanos(instant.nano)
                .build()
        }
    }

    @Singleton
    @Replaces(bean = KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub::class)
    fun grpcClient() = mock(KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub::class.java)
}