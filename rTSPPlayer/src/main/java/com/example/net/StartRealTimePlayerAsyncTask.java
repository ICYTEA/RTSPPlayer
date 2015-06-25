package com.example.net;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import org.ksoap2.SoapFault;

import com.example.rtspplayer.MainActivity;
import com.example.rtspplayer.VideoPlayerActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class StartRealTimePlayerAsyncTask extends AsyncTask<String, Integer, String> {

	private String TAG = "StartRealTimePlayerAsyncTask";
	private Context mContext;
	
	private String nameSpace = "http://sipserviceconsumer.monitor.videomonitor.direction/";
	private String methodName = "startRealtimeStream";
	private String EndPointness = "http://10.46.4.12:9100/VideoMonitor/services/ClientVodServicePublish";
	private String ip;
	private String cameraCode;
	private int port;
	private static String SoapFault = "SoapFault";
	
	public StartRealTimePlayerAsyncTask(Context con){
		this.mContext = con;
	}
	
	//Get Android Service IP
	private String intToIp(int i) {       
		 
		return (i & 0xFF ) + "." +       
				((i >> 8 ) & 0xFF) + "." +       
				((i >> 16 ) & 0xFF) + "." +       
				( i >> 24 & 0xFF) ;  
		}
	
    @Override  
    protected void onPreExecute() {  
    	super.onPreExecute();
        Log.i(TAG, "onPreExecute() called");  
    }  
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		Log.i(TAG, "doInBackground() called");
		
		// ���������ռ�ͷ����õ�SoapObject����
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		String soapAction = nameSpace+methodName;
		Log.i(TAG, "soapAction = " + soapAction);
		
		//Get Android IP		
		WifiManager wifiManager = (WifiManager)mContext.getSystemService(mContext.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();    
		String ip = intToIp(ipAddress);   
		Log.i(TAG, "ip = "+ip);		
		
		// ���������WebService�ӿ���Ҫ�����������,���ﴫ��ʱҪע��,��ʱ����ط�����,�ڴ��������ʱ,Ҫ��wsdl�ļ��ϵķ����Ĳ�����,�����п��ܱ���
//		cameraCode = "45010200051320000045";
		cameraCode = params[0];
		Log.i(TAG, "cameraCode = "+cameraCode);
		port = 30000;
		
		soapObject.addProperty("cameraCode", cameraCode);
//		soapObject.addProperty("cameraCode", "34020000001310000001");
		soapObject.addProperty("receiveIp", ip);
		soapObject.addProperty("receivePort", port);
		
		//initalize httptransport service
		HttpTransportSE httpSE = new HttpTransportSE(EndPointness);
		httpSE.debug = true;
		
		// ͨ��SOAP1.1Э��õ�envelop����,������Ϣ
		SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
		envelop.bodyOut = soapObject;
		//envelop.setOutputSoapObject(soapObject);
		
		try {
			httpSE.call(null, envelop);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// ���������ش�����Ϣ
		String temp = envelop.bodyIn.toString().substring(0, 9);
		if(temp.equals("SoapFault"))
			return envelop.bodyIn.toString();
		
		// �õ����������ص�����
		SoapObject resultObj = (SoapObject) envelop.bodyIn;
		String result = resultObj.getProperty(0).toString();
		Log.i(TAG, result);
		
		Intent i = new Intent(mContext, VideoPlayerActivity.class);
		i.putExtra("result", result);
		i.putExtra("ip", ip);
		i.putExtra("cameraCode", cameraCode);
		i.putExtra("port", port);
		
		((Activity)mContext).startActivityForResult(i, 1);
		
		return null;		
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result != null){
			Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
		}
	}

}
