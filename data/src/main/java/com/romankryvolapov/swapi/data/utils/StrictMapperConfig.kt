/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.utils

import org.mapstruct.MapperConfig
import org.mapstruct.ReportingPolicy

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface StrictMapperConfig