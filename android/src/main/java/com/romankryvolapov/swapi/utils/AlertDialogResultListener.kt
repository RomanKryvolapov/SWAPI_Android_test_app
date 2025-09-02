/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.utils

import com.romankryvolapov.swapi.models.common.AlertDialogResult

fun interface AlertDialogResultListener {

    fun onAlertDialogResultReady(result: AlertDialogResult)

}