package org.om.lib.twisted;

import android.content.Intent;

public interface IActivityResultHandler {
	void handle(int result, Intent intent);
	int getRequestCode();
	void setRequestCode(int requestCode);
	void clear();
}

