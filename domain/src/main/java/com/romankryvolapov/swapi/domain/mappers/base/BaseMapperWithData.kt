/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.domain.mappers.base

abstract class BaseMapperWithData<From, Data, To> {

    abstract fun map(from: From, data: Data?): To

    open fun mapList(fromList: List<From>, data: Data?): List<To> {
        return fromList.map { map(it, data) }
    }

}