package com.codely.member.primaryadapter.rest.error

object MemberServerErrors {
    val INVALID_IDENTIFIERS = "INVALID_IDENTIFIERS" to "The provided identifier is not a valid UUID"
    val INVALID_NAME = "INVALID_NAME" to "The member name is invalid"
    val INVALID_SURNAME = "INVALID_SURNAME" to "The member surname is invalid"
    val INVALID_PHONE_NUMBERS = "INVALID_PHONE_NUMBERS" to "The phone numbers are invalid (1-2 required)"
    val INVALID_TYPE = "INVALID_TYPE" to "The member type is invalid"
    val MEMBER_NOT_FOUND = "MEMBER_NOT_FOUND" to "The member was not found"
    val INVALID_MEMBER_DATA = "INVALID_MEMBER_DATA" to "The provided member data is invalid"
}
