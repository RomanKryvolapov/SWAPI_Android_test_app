/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.models.common

data class InputLockState(
    val state: LockState,
    var timeLeftMilliseconds: Long?,
)

enum class LockState {
    LOCKED,
    UNLOCKED,
}