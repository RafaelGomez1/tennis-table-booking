package com.codely.members.primaryadapter.rest.update

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateMemberDTO(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("surname")
    val surname: String,
    @JsonProperty("phoneNumbers")
    val phoneNumbers: List<String>,
    @JsonProperty("type")
    val type: String,
    @JsonProperty("academyGroups")
    val academyGroups: List<String>? = null,
    @JsonProperty("team")
    val team: String? = null,
    @JsonProperty("idNumber")
    val idNumber: String? = null,
    @JsonProperty("address")
    val address: String? = null,
    @JsonProperty("city")
    val city: String? = null,
    @JsonProperty("postalCode")
    val postalCode: String? = null,
    @JsonProperty("dateOfBirth")
    val dateOfBirth: String? = null,
    @JsonProperty("email")
    val email: String? = null,
    @JsonProperty("memberSince")
    val memberSince: String? = null
)
