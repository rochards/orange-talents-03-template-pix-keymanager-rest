package br.com.zupacademy.keymanagerrest.pix.registra

import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerRegistraServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.RegistraChavePixResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes")
class RegistraChavePixController(private val grpcClient: KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub) {

    @Post("/{clienteId}/pix")
    fun registra(clienteId: String, @Valid @Body request: NovaChavePixRequest): HttpResponse<Any> {
        val grpcRequest = request.toGrpcRequest(clienteId)

        val grpcResponse: RegistraChavePixResponse = grpcClient.registraChavePix(grpcRequest)

        return HttpResponse.created(location(clienteId, grpcResponse.id))
    }

    private fun location(clienteId: String, pixId: Long) = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/$pixId")
}