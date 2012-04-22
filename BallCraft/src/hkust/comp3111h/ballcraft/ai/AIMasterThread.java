package hkust.comp3111h.ballcraft.ai;

import android.util.Log;

public class AIMasterThread extends Thread {
	final static String TAG = "AIMasterThread";
	final static boolean D = true;
	final static int MAX_AI_RUNNING_TIME = 1000 / 30;

	private AIState state;
	private boolean running;

	public AIMasterThread(AIState state) {
		this.state = state;
		running = true;
	}

	public void run() {
		while (running) {
			state.waitForMove();  // blocking

			Thread worker = new AIWorkerThread(this, state);
			worker.start();
			try {
				Thread.sleep(MAX_AI_RUNNING_TIME);
			}
			catch (InterruptedException e) { }

			if (worker.isAlive()) {
				// still working -- kill it and let the AI do nothing
				worker.interrupt();
				if (D) Log.w(TAG, "AI killed because of calculation timeout.");
				state.clearMove();
			}

			// Send AI move to the physics server
			state.issueMove();
		}
	}
}

