# Section 3 rule: keep nothing that phones home.
-keep class com.forgebound.** { *; }
-keepnames class com.forgebound.data.** { *; }
-dontwarn androidx.room.**
-dontwarn androidx.compose.**
