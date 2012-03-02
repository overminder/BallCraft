package org.om.lib.twisted;

public abstract class ActivityResultHandler implements IActivityResultHandler {
	int requestCode;
	
	public int getRequestCode() {
		return requestCode;
	}
	
	public void setRequestCode(int requestCode) {
		this.requestCode = requestCode;
	}
	
	public void clear() {
		Reactor.clearActivityResultHandler(this);
	}
}
