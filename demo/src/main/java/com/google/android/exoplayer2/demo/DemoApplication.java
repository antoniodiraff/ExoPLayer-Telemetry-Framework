/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.demo;

import android.app.Application;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.sky.telemetryframework.PauseCause;

import static com.sky.telemetryframework.EventElementBuilder.ApplicationStartupInfo;
import static com.sky.telemetryframework.EventElementBuilder.sessionError;
import static com.sky.telemetryframework.Listener.getCurrentTimeStamp;
import static com.sky.telemetryframework.SessionDownload.notifyError;
import static com.sky.telemetryframework.SessionDownload.sessionDownloadCompleted;
import static com.sky.telemetryframework.SessionDownload.sessionDownloadDelete;
import static com.sky.telemetryframework.SessionDownload.sessionDownloadPause;
import static com.sky.telemetryframework.SessionDownload.sessionDownloadResume;
import static com.sky.telemetryframework.SessionDownload.sessionStartDownloadVod;
import static com.sky.telemetryframework.SessionDownload.sessionsTreamingDownload;
import static com.sky.telemetryframework.SessionDownload.sessionsTreamingDownloadVOD;


/**
 * Placeholder application to facilitate overriding Application methods for debugging and testing.
 */
public class DemoApplication extends Application {

  protected String userAgent;

  @Override
  public void onCreate() {
    super.onCreate();
    userAgent = Util.getUserAgent(this, "ExoPlayerDemo");

    // ApplcationStartupInfo create the ASI event
    ApplicationStartupInfo("device_vendor", "device_model", "device_so" ,"client_code");


    // uncomment the line below to test the Session Download VOD
    // testSessionDownload();

  }

  public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultDataSourceFactory(this, bandwidthMeter,
        buildHttpDataSourceFactory(bandwidthMeter));
  }

  public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
    return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
  }

  public boolean useExtensionRenderers() {
    return BuildConfig.FLAVOR.equals("withExtensions");
  }


  public void testSessionDownload(){
    sessionStartDownloadVod(getApplicationContext(), "https://telemetria-lib-coll.skycdn.it/skymeter/collector", getCurrentTimeStamp(), "offerID","assetTitle","assetSource","IpServer","Source","userExtId");
    // listener.notifyError("error_test","","","","","","","","","");
    sessionsTreamingDownloadVOD(getApplicationContext(),"ChunkURI","ChunkIndex","ChunkType","bitrate","FpsDecoded","httpresponse","ResponseTime","Ipserver","dwnTime","dwnByte","bufferSize");
    sessionDownloadPause(getApplicationContext(), PauseCause.VoluntaryPause, "");
    sessionDownloadResume(getApplicationContext());
    sessionDownloadDelete(getApplicationContext());
    sessionDownloadCompleted(getApplicationContext());
    notifyError(getApplicationContext(), "error Text 1", "type", "", "", "", "", "", "", "event Name", "errormessage");


  }
}
