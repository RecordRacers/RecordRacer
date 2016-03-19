#include "SuperpoweredExample.h"
#include "SuperpoweredSimple.h"
#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <android/log.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_AndroidConfiguration.h>

static void playerEventCallbackA(void *clientData, SuperpoweredAdvancedAudioPlayerEvent event, void *value) {
    if (event == SuperpoweredAdvancedAudioPlayerEvent_LoadSuccess) {
    	SuperpoweredAdvancedAudioPlayer *playerA = *((SuperpoweredAdvancedAudioPlayer **)clientData);
        playerA->setBpm(126.0f);
        playerA->setFirstBeatMs(353);
        playerA->setPosition(playerA->firstBeatMs, false, false);
    };
    if (event == SuperpoweredAdvancedAudioPlayerEvent_EOF){

    }
}


static bool audioProcessing(void *clientdata, short int *audioIO, int numberOfSamples, int samplerate) {
	return ((SuperpoweredExample *)clientdata)->process(audioIO, numberOfSamples);
}

SuperpoweredExample::SuperpoweredExample(const char *path, int *params) : activeFx(0), crossValue(0.0f), volA(1.0f * headroom) {
    pthread_mutex_init(&mutex, NULL); // This will keep our player volumes and playback states in sync.
    unsigned int samplerate = params[2], buffersize = params[3];
    stereoBuffer = (float *)memalign(16, (buffersize + 16) * sizeof(float) * 2);

    playerA = new SuperpoweredAdvancedAudioPlayer(&playerA , playerEventCallbackA, samplerate, 0);
    playerA->open(path, params[0], params[1]);

    playerA->syncMode = SuperpoweredAdvancedAudioPlayerSyncMode_TempoAndBeat;

    roll = new SuperpoweredRoll(samplerate);
    filter = new SuperpoweredFilter(SuperpoweredFilter_Resonant_Lowpass, samplerate);
    flanger = new SuperpoweredFlanger(samplerate);
    resampler = new SuperpoweredResampler();
    audioSystem = new SuperpoweredAndroidAudioIO(samplerate, buffersize, false, true, audioProcessing, this, -1, SL_ANDROID_STREAM_MEDIA, buffersize * 2);
}

SuperpoweredExample::~SuperpoweredExample() {
    delete audioSystem;
    delete playerA;
    free(stereoBuffer);
    pthread_mutex_destroy(&mutex);
}

void SuperpoweredExample::onPlayPause(bool play) {
    pthread_mutex_lock(&mutex);
    if (!play) {
        playerA->pause();
    } else {
        bool masterIsA = (crossValue <= 0.5f);
        playerA->play(!masterIsA);
    };
    pthread_mutex_unlock(&mutex);
}

void SuperpoweredExample::onCrossfader(int value) {
    pthread_mutex_lock(&mutex);
    crossValue = float(value) * 0.01f;
    if (crossValue < 0.01f) {
        volA = 1.0f * headroom;
    } else if (crossValue > 0.99f) {
        volA = 0.0f;
    } else { // constant power curve
        volA = cosf(M_PI_2 * crossValue) * headroom;
    };
    pthread_mutex_unlock(&mutex);
}

void SuperpoweredExample::onFxSelect(int value) {
	__android_log_print(ANDROID_LOG_VERBOSE, "SuperpoweredExample", "FXSEL %i", value);
	activeFx = value;
}

void SuperpoweredExample::onFxOff() {
    filter->enable(false);
    roll->enable(false);
    flanger->enable(false);
}

#define MINFREQ 60.0f
#define MAXFREQ 20000.0f

static inline float floatToFrequency(float value) {
    if (value > 0.97f) return MAXFREQ;
    if (value < 0.03f) return MINFREQ;
    value = powf(10.0f, (value + ((0.4f - fabsf(value - 0.4f)) * 0.3f)) * log10f(MAXFREQ - MINFREQ)) + MINFREQ;
    return value < MAXFREQ ? value : MAXFREQ;
}

void SuperpoweredExample::onFxValue(int ivalue) {
    float value = float(ivalue) * 0.01f;
    switch (activeFx) {
        case 1:
            filter->setResonantParameters(floatToFrequency(1.0f - value), 0.2f);
            filter->enable(true);
            flanger->enable(false);
            roll->enable(false);
            break;
        case 2:
            if (value > 0.8f) roll->beats = 0.0625f;
            else if (value > 0.6f) roll->beats = 0.125f;
            else if (value > 0.4f) roll->beats = 0.25f;
            else if (value > 0.2f) roll->beats = 0.5f;
            else roll->beats = 1.0f;
            roll->enable(true);
            filter->enable(false);
            flanger->enable(false);
            break;
        default:
            flanger->setWet(value);
            flanger->enable(true);
            filter->enable(false);
            roll->enable(false);
    };
}

void SuperpoweredExample::onResamplerValue(int value){ ;
    __android_log_print(ANDROID_LOG_VERBOSE, APP_NAME, "The parameter value is: %d", value);
    unsigned int newSampleRate = (unsigned int)value * 500;
    playerA->setSamplerate(newSampleRate);
}

void SuperpoweredExample::onNewSong(const char *path, int *params) {
    __android_log_print(ANDROID_LOG_VERBOSE, APP_NAME, "To new song!");
    playerA->open(path, params[0], params[1]);
    playerA->syncMode = SuperpoweredAdvancedAudioPlayerSyncMode_TempoAndBeat;
    pthread_mutex_lock(&mutex);
    bool masterIsA = (crossValue <= 0.5f);
    playerA->play(!masterIsA);
    pthread_mutex_unlock(&mutex);
}


bool SuperpoweredExample::process(short int *output, unsigned int numberOfSamples) {
    float masterBpm = playerA->currentBpm;
    bool silence = !playerA->process(stereoBuffer, false, numberOfSamples, volA, masterBpm, playerA->msElapsedSinceLastBeat);

    if (!silence) {
        flanger->process(stereoBuffer, stereoBuffer, numberOfSamples);
        roll->process(stereoBuffer, stereoBuffer, numberOfSamples);
        filter->process(stereoBuffer, stereoBuffer, numberOfSamples);
    };

    // The stereoBuffer is ready now, let's put the finished audio into the requested buffers.
    if (!silence) SuperpoweredFloatToShortInt(stereoBuffer, output, numberOfSamples);
    return !silence;
}

extern "C" {
	JNIEXPORT void Java_com_sd2_recordracer_MainActivity_SuperpoweredExample(JNIEnv *javaEnvironment, jobject self, jstring apkPath, jlongArray offsetAndLength);
	JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onPlayPause(JNIEnv *javaEnvironment, jobject self, jboolean play);
	JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onCrossfader(JNIEnv *javaEnvironment, jobject self, jint value);
	JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onFxSelect(JNIEnv *javaEnvironment, jobject self, jint value);
	JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onFxOff(JNIEnv *javaEnvironment, jobject self);
	JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onFxValue(JNIEnv *javaEnvironment, jobject self, jint value);
    JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onResamplerValue(JNIEnv *javaEnvironment, jobject self, jint value);
    JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onNewSong(JNIEnv *javaEnvironment, jobject self, jstring apkPath, jlongArray offsetAndLength);
}

static SuperpoweredExample *example = NULL;

// Android is not passing more than 2 custom parameters, so we had to pack file offsets and lengths into an array.
JNIEXPORT void Java_com_sd2_recordracer_MainActivity_SuperpoweredExample(JNIEnv *javaEnvironment, jobject self, jstring apkPath, jlongArray params) {
	// Convert the input jlong array to a regular int array.
    jlong *longParams = javaEnvironment->GetLongArrayElements(params, JNI_FALSE);
    int arr[4];
    for (int n = 0; n < 4; n++) arr[n] = longParams[n];
    javaEnvironment->ReleaseLongArrayElements(params, longParams, JNI_ABORT);

    const char *path = javaEnvironment->GetStringUTFChars(apkPath, JNI_FALSE);
    example = new SuperpoweredExample(path, arr);
    javaEnvironment->ReleaseStringUTFChars(apkPath, path);

}

JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onPlayPause(JNIEnv *javaEnvironment, jobject self, jboolean play) {
	example->onPlayPause(play);
}

JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onCrossfader(JNIEnv *javaEnvironment, jobject self, jint value) {
	example->onCrossfader(value);
}

JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onFxSelect(JNIEnv *javaEnvironment, jobject self, jint value) {
	example->onFxSelect(value);
}

JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onFxOff(JNIEnv *javaEnvironment, jobject self) {
	example->onFxOff();
}

JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onFxValue(JNIEnv *javaEnvironment, jobject self, jint value) {
	example->onFxValue(value);
}

JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onResamplerValue(JNIEnv *javaEnvironment, jobject self, jint value) {
	example->onResamplerValue(value);
}

JNIEXPORT void Java_com_sd2_recordracer_MainActivity_onNewSong(JNIEnv *javaEnvironment, jobject self, jstring apkPath, jlongArray params) {
    // Convert the input jlong array to a regular int array.
    jlong *longParams = javaEnvironment->GetLongArrayElements(params, JNI_FALSE);
    int arr[4];
    for (int n = 0; n < 4; n++) arr[n] = longParams[n];
    javaEnvironment->ReleaseLongArrayElements(params, longParams, JNI_ABORT);

    const char *path = javaEnvironment->GetStringUTFChars(apkPath, JNI_FALSE);
    example->onNewSong(path, arr);
    javaEnvironment->ReleaseStringUTFChars(apkPath, path);
}