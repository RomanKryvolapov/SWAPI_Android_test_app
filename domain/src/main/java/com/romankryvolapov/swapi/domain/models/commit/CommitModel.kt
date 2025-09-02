package com.romankryvolapov.swapi.domain.models.commit

data class CommitModel(
    val sha: String = "",
    val message: String = "",
    val authorName: String = "",
    val committerDate: String = "",
)
