package uk.endercraft.endercore;

public abstract class EnderMinigame extends EnderCore{
	
	protected GameState gameState = GameState.ENDING;

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState newState) {
		this.gameState = newState;
	}
	
	@Override
	public abstract String getMinigameName();

}
