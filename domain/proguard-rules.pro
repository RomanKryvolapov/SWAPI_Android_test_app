# Created & Copyright 2025 by Roman Kryvolapov
# domain/proguard-rules.pro

-dontobfuscate

-include ../common/proguard-rules.pro
-include ../llama-cpp-engine/proguard-rules.pro
-include ../mediapipe-engine/proguard-rules.pro

-keep class com.romankryvolapov.swapi.domain.** { *; }