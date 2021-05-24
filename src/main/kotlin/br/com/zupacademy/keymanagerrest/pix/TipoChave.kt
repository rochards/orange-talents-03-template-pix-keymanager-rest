package br.com.zupacademy.keymanagerrest.pix

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class TipoChave {
    @JsonEnumDefaultValue UNKNOWN_CHAVE, RANDOM, CPF, TELEFONE_CELULAR, EMAIL
}
