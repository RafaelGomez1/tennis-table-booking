package com.codely.member.primaryadapter.rest.update

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
    @JsonProperty("academyGroup")
    val academyGroup: String? = null,
    @JsonProperty("team")
    val team: String? = null
)
