package br.com.zupacademy.keymanagerrest

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zupacademy.keymanagerrest")
		.start()
}

