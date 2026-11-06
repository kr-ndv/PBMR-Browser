# =====================================================================
# PBMR-Browser ProGuard & R8 Configuration
# =====================================================================

# 1. Mozilla GeckoView Engine Keep Rules
# Critical: Do not obfuscate or strip JNI entry points to libxul.so
-keep class org.mozilla.geckoview.** { *; }
-keep interface org.mozilla.geckoview.** { *; }
-keepclassmembers class org.mozilla.geckoview.** { *; }
-dontwarn org.mozilla.geckoview.**

# 2. Native JNI Bindings
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

# 3. Jetpack Compose & Kotlin Coroutines
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-dontwarn kotlinx.coroutines.**
-dontwarn androidx.compose.**

# 4. DataStore and Crypto (PBKDF2 & AES-GCM)
-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**
-keep class javax.crypto.** { *; }

# 5. Suppress harmless warnings for optional third-party integrations
-dontwarn java.lang.invoke.**
