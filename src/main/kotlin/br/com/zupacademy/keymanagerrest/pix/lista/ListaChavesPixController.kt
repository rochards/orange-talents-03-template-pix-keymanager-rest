package br.com.zupacademy.keymanagerrest.pix.lista

import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerListaServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.ListaChavesPixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/api/v1/clientes")
class ListaChavesPixController(private val grpcClient: KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub) {

    @Get("/{clienteId}")
    fun lista(clienteId: String): HttpResponse<ListaChavesPixResponse>? {
        val grpcRequest = buildGrpcRequest(clienteId)

        val grpcResponse = grpcClient.listaChavesPix(grpcRequest)

        return HttpResponse.ok(ListaChavesPixResponse(grpcResponse))
    }

    private fun buildGrpcRequest(clienteId: String): ListaChavesPixRequest {
        return ListaChavesPixRequest.newBuilder()
            .setErpClienteId(clienteId)
            .build()
    }
}