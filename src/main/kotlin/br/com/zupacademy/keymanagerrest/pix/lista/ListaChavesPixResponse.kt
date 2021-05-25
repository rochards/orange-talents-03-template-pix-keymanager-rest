package br.com.zupacademy.keymanagerrest.pix.lista

import br.com.zupacademy.keymanagergrpc.grpc.ListaChavesPixResponse
import br.com.zupacademy.keymanagerrest.pix.TipoChave
import br.com.zupacademy.keymanagerrest.pix.TipoConta
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ListaChavesPixResponse(
    val clienteId: String,
    val chavesPix: List<ChavePix>
) {
    constructor(grpcResponse: ListaChavesPixResponse) : this(
        grpcResponse.erpClienteId,
        grpcResponse.chavesList.map(::ChavePix)
    )

}

data class ChavePix(
    val pixId: Long,
    val chave: String,
    val tipoChave: TipoChave,
    val tipoConta: TipoConta,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING)
    val criadaEm: LocalDateTime
) {
    constructor(grpcChaveResponse: ListaChavesPixResponse.ChavePixResponse) : this(
        grpcChaveResponse.pixId,
        grpcChaveResponse.chave,
        TipoChave.valueOf(grpcChaveResponse.tipoChave.name),
        TipoConta.valueOf(grpcChaveResponse.tipoConta.name),
        grpcChaveResponse.criadaEm.let {
            LocalDateTime.ofEpochSecond(it.seconds, it.nanos, ZoneOffset.UTC)
        }
    )
}