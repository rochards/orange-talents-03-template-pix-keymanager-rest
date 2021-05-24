package br.com.zupacademy.keymanagerrest.pix

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class TipoConta {
    @JsonEnumDefaultValue UNKNOWN_CONTA, CONTA_CORRENTE, CONTA_POUPANCA
}
