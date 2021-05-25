package br.com.zupacademy.keymanagerrest.pix.remove

import br.com.zupacademy.keymanagergrpc.grpc.RemoveChavePixResponse

data class RemoveChavePixResponse(val clienteId: String, val chave: String) {
    constructor(grpcResponse: RemoveChavePixResponse) : this(grpcResponse.erpClienteId, grpcResponse.chave)
}