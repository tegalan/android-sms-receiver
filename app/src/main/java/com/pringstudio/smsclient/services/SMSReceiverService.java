package com.pringstudio.smsclient.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiverService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Incoming Message
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Log.d("SMSReceiverService","Incoming SMS tut...tut..tut...");

            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msgFrom;

            if (bundle != null){

                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msgFrom = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        Log.d("Incoming Message", "SMS From " + msgFrom + ", Content: "+ msgBody);
                    }

                    this.abortBroadcast();
                }catch(Exception e){
                    Log.d("Exception caught",e.getMessage());
                }
            }
        }else{
            Log.d("SMS Receiver", "Intent is not sms"+intent.getAction());
        }
    }
}
