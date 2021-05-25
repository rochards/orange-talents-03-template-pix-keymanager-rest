package br.com.zupacademy.keymanagerrest.pix.consulta

import br.com.zupacademy.keymanagergrpc.grpc.*
import br.com.zupacademy.keymanagergrpc.grpc.ConsultaChavePixResponse
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ConsultaChavePixControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var grpcClient: KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub

    private val clienteId = UUID.randomUUID().toString()
    private val pixId = 1L

    @AfterEach
    internal fun tearDown() {
        reset(grpcClient)
    }

    @Test
    fun `deve retornar uma chave pix quando consultada por clienteId e pixId`() {

        val httpRequest = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")

        `when`(
            grpcClient.consultaChavePix(
                ConsultaChavePixRequest.newBuilder()
                    .setChavePix(
                        ConsultaChavePixRequest.ChavePix.newBuilder()
                            .setId(pixId)
                            .setErpClienteId(clienteId)
                            .build()
                    )
                    .build()
            )
        ).thenReturn(buildChavePixResponse())


        val httpResponse = httpClient.toBlocking().exchange(httpRequest,
            br.com.zupacademy.keymanagerrest.pix.consulta.ConsultaChavePixResponse::class.java
        )

        with(httpResponse) {
            assertEquals(HttpStatus.OK, this.status)
            assertNotNull(this.body())
        }

    }

    private fun buildChavePixResponse(): ConsultaChavePixResponse {
        return ConsultaChavePixResponse.newBuilder()
            .setChaveId(pixId.toString())
            .setErpClienteId(clienteId)
            .setTipoChave(TipoChave.RANDOM)
            .setChave(UUID.randomUUID().toString())
            .setTitular(
                ConsultaChavePixResponse.Titular.newBuilder()
                    .setNome("Peter Parker")
                    .setCpf("02467781054")
                    .build()
            )
            .setConta(
                ConsultaChavePixResponse.Conta.newBuilder()
                    .setNomeInstituicao("ITAÚ UNIBANCO S.A.")
                    .setAgencia("0001")
                    .setNumero("291900")
                    .setTipoConta(TipoConta.CONTA_CORRENTE)
                    .build()
            )
            .setCriadaEm(LocalDateTime.now().let {
                val instant = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(instant.epochSecond)
                    .setNanos(instant.nano)
                    .build()
            })
            .build()
    }

    @Singleton
    @Replaces(bean = KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub::class)
    fun grpcClient() = mock(KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub::class.java)
}