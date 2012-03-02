package org.om.lib.twisted;

import java.util.HashMap;
import java.util.HashSet;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

public class Reactor {
	static int nextRequestCode = 0;
	static HashMap<Integer, ActivityResultHandler> arHandlers =
			new HashMap<Integer, ActivityResultHandler>();
	
	static HashMap<Activity, HashSet<BroadcastReceiver>> bcReceivers =
			new HashMap<Activity, HashSet<BroadcastReceiver>>();
	
	public static void handleActivityResult(int requestCode, int resultCode, Intent intent) throws NoSuchRequestCodeError {
		ActivityResultHandler handler = arHandlers.get(requestCode);
		if (handler == null && !arHandlers.containsKey(requestCode)) {
			throw new NoSuchRequestCodeError();
		}
		handler.handle(resultCode, intent);
	}
	
	public static void startActivityForResult(Intent intent, ActivityResultHandler handler, Activity activity) {
		int requestCode = nextRequestCode++;
		arHandlers.put(requestCode, handler);
		handler.setRequestCode(requestCode);
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter,
			Activity activity) {
		activity.registerReceiver(receiver, filter);
		HashSet<BroadcastReceiver> receivers = bcReceivers.get(activity);
		if (receivers == null) {
			receivers = new HashSet<BroadcastReceiver>();
			bcReceivers.put(activity, receivers);
		}
		// For later unregister.
		receivers.add(receiver);
	}
	
	public static void unregisterReceiver(Activity activity) {
		HashSet<BroadcastReceiver> receivers = bcReceivers.get(activity);
		if (receivers != null) {
			for (BroadcastReceiver receiver : receivers) {
				activity.unregisterReceiver(receiver);
			}
			receivers.remove(activity);
		}
	}
	
	public static void clearActivityResultHandler(
			ActivityResultHandler activityResultHandler) {
		arHandlers.remove(activityResultHandler.getRequestCode());
	}

}
