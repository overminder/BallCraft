package hkust.comp3111h.ballcraft.ai;

import hkust.comp3111h.ballcraft.BallCraft;
import hkust.comp3111h.ballcraft.client.GameInput;
import hkust.comp3111h.ballcraft.server.Ball;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

public class SuicidalAIFlavor implements AIFlavor {
	public GameInput generateMoveAgainst(AIState state) {
		ArrayList<Ball> balls = state.getBalls();
		Ball thePlayer = balls.get(BallCraft.myself);
		Ball theAI = balls.get(BallCraft.enemy);
		Vec2 fromAIToPlayer = new Vec2(
				theAI.getPosition().x - thePlayer.getPosition().x,
				theAI.getPosition().y - thePlayer.getPosition().y);
		// Normalize it
		float maxOfXY = Math.max(fromAIToPlayer.x, fromAIToPlayer.y);
		fromAIToPlayer.x = fromAIToPlayer.x / maxOfXY * 10;
		fromAIToPlayer.y = fromAIToPlayer.y / maxOfXY * 10;
		GameInput AIMove = new GameInput();
		AIMove.acceleration = fromAIToPlayer;
		return AIMove;
	}
}

