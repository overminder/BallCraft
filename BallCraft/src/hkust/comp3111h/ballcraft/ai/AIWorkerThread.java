package hkust.comp3111h.ballcraft.ai;

import hkust.comp3111h.ballcraft.client.GameInput;

public class AIWorkerThread extends Thread {
	private Thread master;
	private AIState state;

	public AIWorkerThread(Thread master, AIState state) {
		this.master = master;
		this.state = state;
	}

	public void run() {
		GameInput AIMove = state.getFlavor().generateMoveAgainst(state);
		state.setAIMove(AIMove);

		// Calculation finished.
		master.interrupt();
	}
}

