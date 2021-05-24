package br.com.zupacademy.keymanagerrest.pix.registra

import br.com.zupacademy.keymanagergrpc.grpc.RegistraChavePixRequest
import br.com.zupacademy.keymanagerrest.pix.TipoChave
import br.com.zupacademy.keymanagerrest.pix.TipoConta
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull
import br.com.zupacademy.keymanagergrpc.grpc.TipoChave as GrpcTipoChave
import br.com.zupacademy.keymanagergrpc.grpc.TipoConta as GrpcTipoConta

@Introspected
data class NovaChavePixRequest(
    @field:NotNull
    val chave: String?,
    @field:NotNull
    val tipoChave: TipoChave?,
    @field:NotNull
    val tipoConta: TipoConta?
) {


    fun toGrpcRequest(clienteId: String): RegistraChavePixRequest? {

        return RegistraChavePixRequest.newBuilder()
            .setErpClienteId(clienteId)
            .setChave(chave)
            .setTipoChave(tipoChave?.let { GrpcTipoChave.valueOf(it.name) })
            .setTipoConta(tipoConta?.let { GrpcTipoConta.valueOf(it.name) })
            .build()
    }
}
