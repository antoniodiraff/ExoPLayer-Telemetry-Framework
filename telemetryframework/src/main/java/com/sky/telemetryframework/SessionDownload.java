package com.sky.telemetryframework;

import android.content.Context;
import android.content.SharedPreferences;

import com.sky.telemetryframework.Model.*;

import java.io.IOException;
import java.util.Timer;

import static com.sky.telemetryframework.EventElementBuilder.addToEventList;
import static com.sky.telemetryframework.EventElementBuilder.asi;
import static com.sky.telemetryframework.EventElementBuilder.sessionError;
import static com.sky.telemetryframework.Listener.createNewEvent;
import static com.sky.telemetryframework.Listener.event;
import static com.sky.telemetryframework.Listener.fromObjectToJSON;
import static com.sky.telemetryframework.Listener.send;
import static com.sky.telemetryframework.Listener.stampaJSON;

/**
 * Created by antoniodiraffaele on 30/11/17.
 */

public final class SessionDownload {


    /*
    DOWNLOAD : During the download of VOD chunks. (**)
    SDP : Session Download Pause
    SDR : Session Download Resume
    SDD : Session Download Delete (download removed)

    */

   /* protected static void sessionStartDownloadVod(String drm_time, String buffering_time, String ip_server, String manifest_uri, String manifest_dwnl_byte, String manifest_dwnl_time,
                                                  String http_response, String offer_id, String asset_title, String asset_source) {

        SSDVOD ssdvod = new SSDVOD(drm_time, buffering_time, ip_server, manifest_uri, manifest_dwnl_byte,
                manifest_dwnl_time, http_response, offer_id, asset_title, asset_source);

        ssdvod.setSessionId(sessionID = getSessionId());
        ssdvod.setStartTime(getCurrentTimeStamp());
        //REGION  ATTRIBUTE
        //ssdvod.setDrm_time("drm_time");
        //   ssdvod.setBuffering_time(bufferingTimeStamp);
        // ssdvod.setIp_server(playerMonitor.serverURL);
        //  ssdvod.setIp_server(ip_server);
        // ssdvod.setHttp_response("Http_Response");
        //ssdvod.setManifest_uri("Maniest URI");
        //ssdvod.setManifest_dwnl_byte("Maniest dwnl byte");
        //ssdvod.setManifest_dwnl_time("Maniest dwnl time");
        // ssdvod.setOffer_id(vodID);
        //   ssdvod.setAsset_title(VODTitle);
        // ssdvod.setAsset_source(assetPath);
        //ENDREGION

        ssdvod.update();
        addToEventList(ssdvod, "SSDVOD", 1);
    }
*/
private static String source_;
private static String user_extid_;
    private static String url_;

    public static void sessionStartDownloadVod(Context c, String url,String currentTimeStamp, String offerID, String assetTitle, String assetSource, String ipServer,String source, String user_extid) {

        int i ;
        createNewEvent(c);
        source_=source;
        url_=url;
        event.setSource(source_);
        user_extid_=user_extid;
        event.device_info.setUser_extid(user_extid_);
        if (asi != null) {
            event.events_list.add(0, new EventElement("ASI", asi.getPayload()));
            i = 1;
            asi = null;
        } else {
            i = 0;
        }

        SSDVOD ssdvod = new SSDVOD();
        String sessionID = EventElementBuilder.getSessionId();
        ssdvod.setSessionId(sessionID);

        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("sessionID_SSDVOD", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SessionIdSSDVOD", sessionID);
        editor.commit();

        ssdvod.setStartTime(currentTimeStamp);
        ssdvod.setIpServer(ipServer);
        ssdvod.setOfferId(offerID);
        ssdvod.setAssetTitle(assetTitle);
        ssdvod.setAssetSource(assetSource);
        ssdvod.update();
        addToEventList(ssdvod,"SSDVOD",i);
    }

    public static void sessionDownloadPause(Context c,PauseCause pauseCause, String bufferedPercentage) {
        //SDP : Session Download Pause
        SDP sdp = new SDP();
        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("sessionID_SSDVOD", Context.MODE_PRIVATE);
      //  sdp.setSessionId(EventElementBuilder.sessionID);
        sdp.setSessionId(sharedPreferences.getString("SessionIdSSDVOD",null));
        sdp.setPauseTime(Listener.getCurrentTimeStamp());
        sdp.setPauseCause(pauseCause.getPauseCode());
        sdp.setDownloadPerc(bufferedPercentage);
        sdp.update();
        EventElementBuilder.addToEventList(sdp, "SDP");
        try {
            sendEventAndEmptyOutEvent(c);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sessionDownloadResume(Context c) {
        //SDR : Session Download Resume
        SDR sdr = new SDR();
        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("sessionID_SSDVOD", Context.MODE_PRIVATE);
        sdr.setSessionId(sharedPreferences.getString("SessionIdSSDVOD",null));

       // sdr.setSessionId(EventElementBuilder.sessionID);
        sdr.setResumeTime(Listener.getCurrentTimeStamp());
        sdr.update();
        EventElementBuilder.addToEventList(sdr, "SDR");
        try {
            sendEventAndEmptyOutEvent(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sessionDownloadDelete(Context c) {
        //SDD : Session Download Delete (download removed)
        SDD sdd = new SDD();
        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("sessionID_SSDVOD", Context.MODE_PRIVATE);
        sdd.setSessionId(sharedPreferences.getString("SessionIdSSDVOD",null));
       // sdd.setSessionId(EventElementBuilder.sessionID);

        sdd.setDeleteTime(Listener.getCurrentTimeStamp());
        sdd.update();
        EventElementBuilder.addToEventList(sdd, "SDD");
        try {
            sendEventAndEmptyOutEvent(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sessionDownloadCompleted(Context c) {
        //Session downloaded completed
        SDC sdc = new SDC();
      //  sdc.setSessionId(EventElementBuilder.sessionID);
        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("sessionID_SSDVOD", Context.MODE_PRIVATE);
        sdc.setSessionId(sharedPreferences.getString("SessionIdSSDVOD",null));

        sdc.setCompleted_time(Listener.getCurrentTimeStamp());
        sdc.update();
        EventElementBuilder.addToEventList(sdc, "SDC");
        try {
            sendEventAndEmptyOutEvent(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sessionsTreamingDownload(String ChunkUri, String ChunkIndex, String ChunkType, String bitrate, String FpsDecoded, String httpResponse,
                                                String ResponseTime, String ipServer, String dwnTime, String dwnlByte,String bufferSize) {
        STD std = new STD();
        std.setSessionId(EventElementBuilder.sessionID);
        std.setLayer(bitrate);
        std.setDwnlByte(dwnlByte);
        std.setDwnlTime(dwnTime);
        //secondi rimanenti del video già bufferizzato
        std.setBufferSize(bufferSize);
        std.setChunkIndex(ChunkIndex);
        std.setChunkType(ChunkType);
        std.setChunkUri(ChunkUri);
        std.setFpsDecoded(FpsDecoded);
        std.setResponseTime(ResponseTime);
        std.setIpServer(ipServer);
        std.setHttpResponse(httpResponse);
        std.update();
        EventElementBuilder.addToEventList(std, "STD");
    }

    public static void sessionsTreamingDownloadVOD(Context c,String ChunkUri, String ChunkIndex, String ChunkType, String bitrate, String FpsDecoded, String httpResponse,
                                                String ResponseTime, String ipServer, String dwnTime, String dwnlByte,String bufferSize){

        SharedPreferences sharedPreferences = c.getApplicationContext().getSharedPreferences("sessionID_SSDVOD", Context.MODE_PRIVATE);

        STD std = new STD();
        std.setSessionId(sharedPreferences.getString("SessionIdSSDVOD",null));
        std.setLayer(bitrate);
        std.setDwnlByte(dwnlByte);
        std.setDwnlTime(dwnTime);
        //secondi rimanenti del video già bufferizzato
        std.setBufferSize(bufferSize);
        std.setChunkIndex(ChunkIndex);
        std.setChunkType(ChunkType);
        std.setChunkUri(ChunkUri);
        std.setFpsDecoded(FpsDecoded);
        std.setResponseTime(ResponseTime);
        std.setIpServer(ipServer);
        std.setHttpResponse(httpResponse);
        std.update();
        EventElementBuilder.addToEventList(std, "STD");

        try {
            sendEventAndEmptyOutEvent(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void notifyError(Context c, String error_text, String errorType, String error_code, String chunk_uri, String channel_id,
                            String event_id, String vod_id, String player_version, String event_name, String error_message) {
        sessionError(error_text, errorType, error_code, chunk_uri, channel_id, event_id, vod_id, player_version, event_name, error_message);
        try {
            sendEventAndEmptyOutEvent(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  public static void sendEventAndEmptyOutEvent(Context c) throws IOException {
        try {
            String json = null;
            if (event!=null){
                json = fromObjectToJSON(event);
            stampaJSON(json);
            createNewEvent(c);
            event.setSource(source_);
            event.device_info.setUser_extid(user_extid_);
            send(url_, json);
            }else {

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return;
    }





}
