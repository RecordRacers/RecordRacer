#ifndef Header_SuperpoweredExample
#define Header_SuperpoweredExample

#include <math.h>
#include <pthread.h>
#include <android/log.h>

#include "SuperpoweredExample.h"
#include "../../../../../Superpowered/SuperpoweredAdvancedAudioPlayer.h"
#include "../../../../../Superpowered/SuperpoweredFilter.h"
#include "../../../../../Superpowered/SuperpoweredResampler.h"
#include "../../../../../Superpowered/SuperpoweredRoll.h"
#include "../../../../../Superpowered/SuperpoweredFlanger.h"
#include "../../../../../Superpowered/SuperpoweredAndroidAudioIO.h"
#include "../../../../../Superpowered/SuperpoweredSimple.h"

#define APP_NAME "RecordRacer"
#define NUM_BUFFERS 2
#define HEADROOM_DECIBEL 3.0f
static const float headroom = powf(10.0f, -HEADROOM_DECIBEL * 0.025);

class SuperpoweredExample {
public:

	SuperpoweredExample(const char *path, int *params);
	~SuperpoweredExample();

	bool process(short int *output, unsigned int numberOfSamples);
	void onPlayPause(bool play);
	void onCrossfader(int value);
	void onFxSelect(int value);
	void onFxOff();
	void onFxValue(int value);
	void onResamplerValue(int value);



private:
    pthread_mutex_t mutex;
    SuperpoweredAndroidAudioIO *audioSystem;
    SuperpoweredAdvancedAudioPlayer *playerA;
    SuperpoweredRoll *roll;
    SuperpoweredFilter *filter;
    SuperpoweredFlanger *flanger;
    SuperpoweredResampler *resampler;
    float *stereoBuffer;
    unsigned char activeFx;
    float crossValue, volA;
};

#endif
