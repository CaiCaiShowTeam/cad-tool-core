/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_bplead_cad_jni_CaxaIntegrationIfc */

#ifndef _Included_com_bplead_cad_jni_CaxaIntegrationIfc
#define _Included_com_bplead_cad_jni_CaxaIntegrationIfc
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_bplead_cad_jni_CaxaIntegrationIfc
 * Method:    connect
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_bplead_cad_jni_CaxaIntegrationIfc_connect
  (JNIEnv *, jobject);

/*
 * Class:     com_bplead_cad_jni_CaxaIntegrationIfc
 * Method:    disconnect
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_bplead_cad_jni_CaxaIntegrationIfc_disconnect
  (JNIEnv *, jobject);

/*
 * Class:     com_bplead_cad_jni_CaxaIntegrationIfc
 * Method:    getCurrentCadDetail
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_bplead_cad_jni_CaxaIntegrationIfc_getCurrentCadDetail
  (JNIEnv *, jobject);

/*
 * Class:     com_bplead_cad_jni_CaxaIntegrationIfc
 * Method:    getCurrentCadPath
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_bplead_cad_jni_CaxaIntegrationIfc_getCurrentCadPath
  (JNIEnv *, jobject);

/*
 * Class:     com_bplead_cad_jni_CaxaIntegrationIfc
 * Method:    getCurrentCadTitle
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_bplead_cad_jni_CaxaIntegrationIfc_getCurrentCadTitle
  (JNIEnv *, jobject);

/*
 * Class:     com_bplead_cad_jni_CaxaIntegrationIfc
 * Method:    getCurrentTechnology
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_bplead_cad_jni_CaxaIntegrationIfc_getCurrentTechnology
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif