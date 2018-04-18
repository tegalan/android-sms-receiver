package com.pringstudio.smsclient.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.pringstudio.smsclient.events.NewMessageEvent;

import org.greenrobot.eventbus.EventBus;

public class SMSReceiverService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Incoming Message
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Log.d("SMSReceiverService","Incoming SMS tut...tut..tut...");

            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msgFrom = "";
            String msgBody = "";
            StringBuilder stringBuilder = new StringBuilder();

            if (bundle != null){

                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msgFrom = msgs[i].getOriginatingAddress();
                        String msgPart = msgs[i].getMessageBody();

                        stringBuilder.append(msgPart);
//                        Log.d("Incoming Message", "SMS From " + msgFrom + ", Content: "+ msgPart);
                    }
                    // Dispatch Event
                    EventBus.getDefault().post(new NewMessageEvent(msgFrom, stringBuilder.toString()));

                    Log.d("Wrapped message", "Wrapped Message from "+msgFrom+" is: "+stringBuilder.toString());

//                    this.abortBroadcast();
                }catch(Exception e){
                    Log.d("Exception caught",e.getMessage());
                }
            }
        }else{
            Log.d("SMS Receiver", "Intent is not sms"+intent.getAction());
        }
    }
}
