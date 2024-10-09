
/**
 * A factory for generating players, using specific implementations of the generic interface "Player".
 * This helps to ensure the validity of the principle of individual responsibility.
 */
public class PlayerFactory {

    /**
     * Generates a player of specific type, according to a given string.
     *
     * @param type the string represents the wanted type of player.
     * @return a generated player.
     */
    public Player buildPlayer(String type) {
        Player player = null;
        switch (type) {
            case "human":
                player = new HumanPlayer();
                break;
            case "whatever":
                player = new WhateverPlayer();
                break;
            case "clever":
                player = new CleverPlayer();
                break;
            case "genius":
                player = new GeniusPlayer();
                break;
        }
        return player;
    }
}
