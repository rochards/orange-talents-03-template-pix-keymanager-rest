package br.com.zupacademy.keymanagerrest.pix.consulta

import br.com.zupacademy.keymanagergrpc.grpc.ConsultaChavePixRequest
import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerConsultaServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/api/v1/clientes")
class ConsultaChavePixController(private val grpcClient: KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub) {


    @Get("/pix/{pix}")
    fun consulta(pix: String): HttpResponse<ConsultaChavePixResponse> {
        val grpcRequest = buildGrpcRequest(pix)

        val grpcResponse = grpcClient.consultaChavePix(grpcRequest)

        return HttpResponse.ok(ConsultaChavePixResponse(grpcResponse))
    }

    @Get("/{clienteId}/pix/{pixId}")
    fun consulta(clienteId: String, pixId: Long): HttpResponse<ConsultaChavePixResponse> {
        val grpcRequest = buildGrpcRequest(clienteId, pixId)

        val grpcResponse = grpcClient.consultaChavePix(grpcRequest)

        return HttpResponse.ok(ConsultaChavePixResponse(grpcResponse))
    }

    private fun buildGrpcRequest(pix: String): ConsultaChavePixRequest {
        return ConsultaChavePixRequest.newBuilder()
            .setChave(pix)
            .build()
    }

    private fun buildGrpcRequest(clienteId: String, pixId: Long): ConsultaChavePixRequest {
        return ConsultaChavePixRequest.newBuilder()
            .setChavePix(
                ConsultaChavePixRequest.ChavePix.newBuilder()
                    .setId(pixId)
                    .setErpClienteId(clienteId)
                    .build()
            )
            .build()
    }


}