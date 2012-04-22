package hkust.comp3111h.ballcraft.ai;

import hkust.comp3111h.ballcraft.client.GameInput;

public interface AIFlavor {
	GameInput generateMoveAgainst(AIState state);
}


