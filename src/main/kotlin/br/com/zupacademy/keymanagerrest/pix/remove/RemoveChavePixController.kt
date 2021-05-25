package br.com.zupacademy.keymanagerrest.pix.remove

import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerRemoveServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete

@Controller("/api/v1/clientes")
class RemoveChavePixController(private val grpcClient: KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub) {

    @Delete("/{clienteId}/pix/{pixId}")
    fun remove(clienteId: String, pixId: Long): HttpResponse<Any> {
        val grpcRequest = buildGrpcRequest(clienteId, pixId)

        val grpcResponse = grpcClient.removeChavePix(grpcRequest)

        return HttpResponse.ok(RemoveChavePixResponse(grpcResponse))
    }

    private fun buildGrpcRequest(clienteId: String, pixId: Long): RemoveChavePixRequest {
        return RemoveChavePixRequest.newBuilder()
            .setChaveId(pixId)
            .setErpClienteId(clienteId)
            .build()
    }
}