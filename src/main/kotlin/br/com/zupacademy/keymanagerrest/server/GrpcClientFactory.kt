package br.com.zupacademy.keymanagerrest.server

import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerRegistraServiceGrpc
import br.com.zupacademy.keymanagergrpc.grpc.KeyManagerRemoveServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun registraChaveClienteStub(@GrpcChannel("keymanagergrpc") channel: ManagedChannel)
            : KeyManagerRegistraServiceGrpc.KeyManagerRegistraServiceBlockingStub? {
        return KeyManagerRegistraServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun removeChaveClienteStub(@GrpcChannel("keymanagergrpc") channel: ManagedChannel)
            : KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub? {
        return KeyManagerRemoveServiceGrpc.newBlockingStub(channel)
    }
}