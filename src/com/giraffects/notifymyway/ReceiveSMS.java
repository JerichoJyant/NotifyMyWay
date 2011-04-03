package com.giraffects.notifymyway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

//import android.telephony.SmsMessage;
//import android.widget.Toast;

public class ReceiveSMS extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SmsMessage[] msgs = null;
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				SMSNotifierAction action = new SMSNotifierAction(context, msgs[i]);
				action.notify_user();
			}
		} else {
			StaticHelper.w("Null bundle when receiving SMS");
		}
	}

}
