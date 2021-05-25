package br.com.zupacademy.keymanagerrest.server

import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerConsultaServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerListaServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerRegistraServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerRemoveServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("keymanagergrpc") private val channel: ManagedChannel) {

    @Singleton
    fun registraChaveClienteStub(): KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub? {
        return KeyManagerRegistraServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun removeChaveClienteStub(): KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub? {
        return KeyManagerRemoveServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun consultaChaveClientStub(): KeyManagerConsultaServiceGrpc.KeyManagerConsultaServiceBlockingStub? {
        return KeyManagerConsultaServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun listaChavesClientStub(): KeyManagerListaServiceGrpc.KeyManagerListaServiceBlockingStub? {
        return KeyManagerListaServiceGrpc.newBlockingStub(channel)
    }
}