package com.codely.admin.primaryadapter.rest.register

import com.fasterxml.jackson.annotation.JsonProperty

data class RegisterAdminDTO(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("password")
    val password: String
)
