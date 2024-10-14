package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.Color;
import java.util.function.Supplier;

/**
 * Represents the energy display in the game world.
 * The energy display is a user interface element used to visually represent the energy level of the player.
 */
public class EnergyDisplay {

    private static final String TAG = "energy";

    private static final String FORMAT = "%d%%";
    private static final Color COLOR = Color.WHITE;
    private static final Vector2 DIMENSIONS = Vector2.of(100, 50);

    /**
     * Creates an energy display game object.
     *
     * @param energyGetter A supplier function to retrieve the current energy level.
     * @return The energy display game object.
     */
    public static GameObject create(Supplier<Float> energyGetter) {
        TextRenderable energyImage = new TextRenderable(String.format(FORMAT,
                (int)(float) energyGetter.get()));
        energyImage.setColor(COLOR);
        GameObject energy = new GameObject(Vector2.ZERO, DIMENSIONS, energyImage);
        energy.setTag(TAG);
        energy.addComponent((float deltaTime) ->
                energyImage.setString(String.format(FORMAT, (int)(float) energyGetter.get())));
        return energy;
    }

}
