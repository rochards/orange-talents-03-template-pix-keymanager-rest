package br.com.zupacademy.keymanagerrest.server

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
}