/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.mappers

import com.romankryvolapov.swapi.domain.defaultApplicationInfo
import com.romankryvolapov.swapi.domain.mappers.base.BaseReverseMapper
import com.romankryvolapov.swapi.domain.models.application.ApplicationInfo
import com.romankryvolapov.swapi.domain.models.application.ApplicationInfoNullable

class ApplicationInfoMapper : BaseReverseMapper<ApplicationInfo, ApplicationInfoNullable>() {

    override fun reverse(to: ApplicationInfoNullable): ApplicationInfo {
        return with(to) {
            ApplicationInfo(
                version = version ?: defaultApplicationInfo.version,
                searchText = searchText ?: defaultApplicationInfo.searchText,
                accessToken = accessToken ?: defaultApplicationInfo.accessToken,
                applicationLanguage = applicationLanguage
                    ?: defaultApplicationInfo.applicationLanguage,
                author = author ?: defaultApplicationInfo.author,
            )
        }
    }

    override fun map(model: ApplicationInfo): ApplicationInfoNullable {
        return with(model) {
            ApplicationInfoNullable(
                author = author,
                version = version,
                searchText = searchText,
                accessToken = accessToken,
                applicationLanguage = applicationLanguage,
            )
        }
    }


}