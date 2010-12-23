package com.giraffects.notifymyway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//import android.telephony.SmsMessage;
//import android.widget.Toast;

public class ReceiveSMS extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SMSNotifierAction action = new SMSNotifierAction(context);
		action.notify_user();
	}

}
