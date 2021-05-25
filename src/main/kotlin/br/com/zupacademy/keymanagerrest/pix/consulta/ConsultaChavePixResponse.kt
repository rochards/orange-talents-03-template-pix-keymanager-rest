package br.com.zupacademy.keymanagerrest.pix.consulta

import br.com.zupacademy.keymanagergrpc.grpc.ConsultaChavePixResponse
import br.com.zupacademy.keymanagerrest.pix.TipoChave
import br.com.zupacademy.keymanagerrest.pix.TipoConta
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ConsultaChavePixResponse(
    val pixId: String,
    val clienteId: String,
    val tipoChave: TipoChave,
    val pix: String,
    val titular: Titular,
    val conta: Conta,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    val criadaEm: LocalDateTime

) {
    constructor(grpcResponse: ConsultaChavePixResponse) : this(
        grpcResponse.chaveId, grpcResponse.erpClienteId,
        TipoChave.valueOf(grpcResponse.tipoChave.name),
        grpcResponse.chave,
        Titular(grpcResponse.titular),
        Conta(grpcResponse.conta),
        LocalDateTime.ofEpochSecond(
            grpcResponse.criadaEm.seconds,
            grpcResponse.criadaEm.nanos,
            ZoneOffset.UTC
        )
    )
}

data class Titular(val nome: String, val cpf: String) {

    constructor(grpcTitularResponse: ConsultaChavePixResponse.Titular) : this(
        grpcTitularResponse.nome,
        grpcTitularResponse.cpf
    )
}

data class Conta(val nomeInstituicao: String, val agencia: String, val numero: String, val tipoConta: TipoConta) {
    constructor(grpcContaResponse: ConsultaChavePixResponse.Conta) : this(
        grpcContaResponse.nomeInstituicao,
        grpcContaResponse.agencia,
        grpcContaResponse.numero,
        TipoConta.valueOf(grpcContaResponse.tipoConta.name)
    )
}