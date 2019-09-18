# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-target 1.6
-optimizations method/*,!code/simplification/arithmetic,code/*
-optimizationpasses 2
-allowaccessmodification
-mergeinterfacesaggressively
-useuniqueclassmembernames
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-renamesourcefileattribute SourceFile
-adaptresourcefilenames **.properties
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF
-verbose


-keep class android.support.v4.*

-keep public class * {
    public protected <fields>;
    public <fields>;
    public protected <methods>;
    public <methods>;
}

-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# android
-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.Fragment

-keep public class * extends LinearLayout

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

-keepclassmembers class * extends android.os.Parcelable {
    static ** CREATOR;
}

#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface
#    <methods>;
#}

-keep public abstract class * extends @android.os.IInterface * {
    public <fields>;
    public <methods>;
}

# Keep - Library. Keep all public and protected classes, fields, and methods.
-keep public class * {
    public protected <fields>;
    public protected <methods>;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# -dontwarn com.pax.api.**
-keep class com.pax.api.** {
    <fields>;
    <methods>;
}

# -dontwarn pax.util.**
-keep class pax.util.** {
    <fields>;
    <methods>;
}

#add 190311
#-keep public class  com.pax.order.util.**
#-keep public class  com.pax.order.orderserver.Impl.**

-dontwarn com.pax.ecrsdk.**
-keep class com.pax.ecrsdk.** { *; }

-dontwarn com.pax.posdk.**
-keep class com.pax.posdk.** { *; }

-dontwarn com.pax.poslink.**
-keep class com.pax.poslink.** { *; }

-dontwarn com.j256.ormlite.**
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keepclassmembers class * {
@com.j256.ormlite.field.DatabaseField *;
}

-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-dontwarn net.sqlcipher.**
-keep class net.sqlcipher.** {*;}


-dontwarn android.os.**
-dontwarn android.net.**
-dontwarn okio.**
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

-keep class com.pax.order.orderserver.**
-keepclassmembers class com.pax.order.orderserver.** { *; }
-keep interface com.pax.order.orderserver.**
-keepclassmembers interface com.pax.order.orderserver.** { *; }

-keep class com.pax.order.pay.edc.**
-keepclassmembers class com.pax.order.pay.edc.** { *; }

-keep class com.pax.order.pay.posdk.**
-keepclassmembers class com.pax.order.pay.posdk.** { *; }

-keep class com.pax.order.pay.poslink.**
-keepclassmembers class com.pax.order.pay.poslink.** { *; }

#Gson
-dontwarn com.google.gson.**
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }

#JJWT
-keepnames class com.fasterxml.jackson.databind.** { *; }
-dontwarn com.fasterxml.jackson.databind.*
-keepattributes InnerClasses

-keep class org.bouncycastle.** { *; }
-keepnames class org.bouncycastle.** { *; }

-dontwarn org.bouncycastle.**
-keep class io.jsonwebtoken.** { *; }

-keepnames class io.jsonwebtoken.* { *; }
-keepnames interface io.jsonwebtoken.* { *; }

-dontwarn javax.xml.bind.DatatypeConverter
-dontwarn io.jsonwebtoken.impl.Base64Codec

-keepnames class com.fasterxml.jackson.** { *; }
-keepnames interface com.fasterxml.jackson.** { *; }

#dom4j
-dontwarn org.dom4j.**
-keep class org.dom4j.**{*;}
-dontwarn org.xml.sax.**
-keep class org.xml.sax.**{*;}

-dontwarn com.fasterxml.jackson.**
-keep class com.fasterxml.jackson.**{*;}

-dontwarn com.pax.market.api.sdk.java.base.util.**
-keep class com.pax.market.api.sdk.java.base.util.**{*;}

-dontwarn org.w3c.dom.**
-keep class org.w3c.dom.**{*;}


#dto
-dontwarn com.pax.market.api.sdk.java.base.dto.**
-keep class com.pax.market.api.sdk.java.base.dto.**{*;}

-dontwarn javax.xml.transform.**
-keep class javax.xml.transform.**{*;}

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# AndroidEventBus
-keep class org.simple.** { *; }
-keep interface org.simple.** { *; }
-keepclassmembers class * {
    @org.simple.eventbus.Subscriber <methods>;
}

# EventBus
-keepattributes *Annotation*
-keep @**annotation** class * {*;}
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent

