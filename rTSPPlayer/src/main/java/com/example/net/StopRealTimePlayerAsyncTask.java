package com.example.net;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;
import org.videolan.libvlc.VLCInstance;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

public class StopRealTimePlayerAsyncTask extends AsyncTask<String, Integer, String>{

	private String TAG = "StopRealTimePlayerAsyncTask";
	
	private String nameSpace = "http://sipserviceconsumer.monitor.videomonitor.direction/";
	private String methodName = "stopRealtimeStreamByDeviceId";
	private String EndPointness = "http://10.46.4.12:9100/VideoMonitor/services/ClientVodServicePublish";
	private String ip;
	private String cameraCode;
	private int port;
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		// ���������ռ�ͷ����õ�SoapObject����
		SoapObject soapObject = new SoapObject(nameSpace,methodName);
		String soapAction = nameSpace+methodName;
		Log.i(TAG, "soapAction = " + soapAction);
		
		ip = params[0];
		cameraCode = params[1];
		port = Integer.valueOf(params[2]);
		
		Log.i(TAG, "ip = "+ip);
		Log.i(TAG, "cameraCode = "+ cameraCode);
		Log.i(TAG, "port = " + port);
		
//		// ���������WebService�ӿ���Ҫ�����������,���ﴫ��ʱҪע��,��ʱ����ط�����,�ڴ��������ʱ,Ҫ��wsdl�ļ��ϵķ����Ĳ�����,�����п��ܱ���		
		soapObject.addProperty("arg0", cameraCode);
		soapObject.addProperty("arg1", ip);
		soapObject.addProperty("arg2", port);
		
		//initalize httptransport service
		HttpTransportSE httpSE = new HttpTransportSE(EndPointness);
		httpSE.debug = true;
		
		// ͨ��SOAP1.1Э��õ�envelop����,������Ϣ
		SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelop.bodyOut = soapObject;
		
		// ��ʼ����Զ�̷���
		try {
			httpSE.call(null, envelop);
			
		} catch (IOException e) {
			e.printStackTrace();
			return "IOException";
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return "XmlPullParserException";
		}
		
		return null;
	}

}
