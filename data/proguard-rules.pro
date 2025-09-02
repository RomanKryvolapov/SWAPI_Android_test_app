# Created & Copyright 2025 by Roman Kryvolapov
# data/proguard-rules.pro

-dontobfuscate

-include ../domain/proguard-rules.pro

-keep class com.romankryvolapov.swapi.data.** { *; }
-keep class com.romankryvolapov.swapi.data.utils.CoroutineContextProvider { *; }