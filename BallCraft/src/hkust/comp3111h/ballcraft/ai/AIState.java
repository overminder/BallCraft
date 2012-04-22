package hkust.comp3111h.ballcraft.ai;

import hkust.comp3111h.ballcraft.client.GameInput;
import hkust.comp3111h.ballcraft.server.Ball;
import hkust.comp3111h.ballcraft.server.Unit;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * AIState is ran under AIMasterThread and should only block itself
 * when waiting for move.
 */
public class AIState {
	private BlockingQueue<GameInput> queue
		= new LinkedBlockingQueue<GameInput>();
	private GameInput playerMove;
	private GameInput AIMove;
	private AIFlavor flavor;

	AIState(AIFlavor flavor) {
		this.flavor = flavor;
	}

	AIFlavor getFlavor() {
		return flavor;
	}

	synchronized void waitForMove() {
		try {
			playerMove = queue.take();
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void playerMoveReceived(GameInput newPlayerMove) {
		try {
			queue.put(newPlayerMove);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	void clearMove() {
		// TODO
	}

	void issueMove() {
		// TODO
	}

	GameInput getPlayerMove() {
		return playerMove;
	}

	ArrayList<Unit> getUnits() {
		return null;
	}

	ArrayList<Ball> getBalls() {
		return null;
	}

	void setAIMove(GameInput newAIMove) {
		AIMove = newAIMove;
	}
}

