package br.com.zupacademy.keymanagerrest.pix.registra

import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerRegistraServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.RegistraChavePixResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes")
class RegistraChavePixController(private val grpcClient: KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub) {

    @Post("/{clienteId}/pix")
    fun registra(clienteId: String, @Valid @Body request: NovaChavePixRequest): HttpResponse<Any> {

        val grpcRequest = request.toGrpcRequest(clienteId)

        try {
            val grpcResponse: RegistraChavePixResponse = grpcClient.registraChavePix(grpcRequest)
            return HttpResponse.created(location(clienteId, grpcResponse.id))
        } catch (e: StatusRuntimeException) {

            val error = when (e.status.code) {
                Status.Code.INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST to e.status.description
                Status.Code.ALREADY_EXISTS -> HttpStatus.UNPROCESSABLE_ENTITY to e.status.description
                else -> HttpStatus.INTERNAL_SERVER_ERROR to e.status.description
            }

            throw HttpStatusException(error.first, error.second)
        }
    }

    private fun location(clienteId: String, pixId: Long) = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/$pixId")
}