package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Terrain;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;

import java.util.List;
import java.util.function.Supplier;

/**
 * The main game manager class for the Pepse game.
 * Initializes and manages the game elements such as terrain, sky, avatar, day-night cycle, etc.
 */
public class PepseGameManager extends GameManager {

    private static final int TARGET_FRAMERATE = 30;
    private static final float DAY_LENGTH = 30f;
    private static final float MIDDLE_FACTOR = 0.5f;

    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int GROUND_LAYER = Layer.STATIC_OBJECTS;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int ENERGY_LAYER = Layer.UI;
    private static final int TRUNK_LAYER = Layer.STATIC_OBJECTS;
    private static final int LEAVES_LAYER = TRUNK_LAYER + 1;
    private static final int FRUITS_LAYER = LEAVES_LAYER + 1;


    /**
     * Initializes the game with required elements.
     *
     * @param imageReader    The image reader for loading game resources.
     * @param soundReader    The sound reader for loading game audio resources.
     * @param inputListener  The user input listener for capturing player input.
     * @param windowController The window controller for managing the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        windowController.setTargetFramerate(TARGET_FRAMERATE);  // for passing pre-submit
        Vector2 windowDimensions = windowController.getWindowDimensions();

        initializeSky(windowDimensions);
        Terrain terrain = initializeTerrain(windowDimensions);
        initializeNight(windowDimensions);
        initializeSun(windowDimensions);
        Avatar avatar = initializeAvatar(windowDimensions, imageReader, inputListener, terrain);
        initializeEnergyDisplay(avatar::getEnergy);
        initializeTrees(terrain, avatar, windowDimensions);

        // set collided layers so the avatar will be able to eat the fruits:
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, FRUITS_LAYER, true);
    }
    /*
     * Initializes trees within the game environment.
     *
     * @param terrain          The terrain object providing ground height information.
     * @param avatarInitialX   The initial X position of the avatar.
     * @param avatar           The avatar object to interact with trees.
     * @param windowDimensions The dimensions of the game window.
     */
    private void initializeTrees(Terrain terrain, Avatar avatar, Vector2 windowDimensions) {
        float avatarInitialX = avatar.getTopLeftCorner().x();
        Flora flora = new Flora(terrain::groundHeightAt,
                Vector2.of(avatarInitialX, avatarInitialX + avatar.getDimensions().x()),
                avatar::addEnergy, DAY_LENGTH);
        List<Tree> trees = flora.createInRange(0, (int) windowDimensions.x());
        for (var tree : trees) {
            avatar.registerObserver(tree);
            gameObjects().addGameObject(tree.getTrunk(), TRUNK_LAYER);
            for (var leaf : tree.getLeaves()) {
                gameObjects().addGameObject(leaf, LEAVES_LAYER);
            }
            for (var fruit : tree.getFruits()) {
                gameObjects().addGameObject(fruit, FRUITS_LAYER);
            }
        }
    }

    /*
     * Initializes the energy display in the game.
     *
     * @param energyGetter A supplier function to get the current energy level.
     */
    private void initializeEnergyDisplay(Supplier<Float> energyGetter) {
        GameObject energy = EnergyDisplay.create(energyGetter);
        gameObjects().addGameObject(energy, ENERGY_LAYER);
    }

    /*
     * Initializes the avatar in the game.
     *
     * @param imageReader    The image reader object to read the avatar image.
     * @param inputListener  The user input listener for controlling the avatar.
     * @param avatarInitialX The initial X position of the avatar.
     * @param terrain        The terrain object.
     * @return The initialized avatar object.
     */
    private Avatar initializeAvatar(Vector2 windowDimensions, ImageReader imageReader,
                                    UserInputListener inputListener, Terrain terrain) {
        float avatarInitialX = windowDimensions.x() * MIDDLE_FACTOR;
        Avatar avatar = new Avatar(
                Vector2.of(avatarInitialX, terrain.groundHeightAt(avatarInitialX) - Block.SIZE),
                inputListener, imageReader
        );
        gameObjects().addGameObject(avatar, AVATAR_LAYER);
        return avatar;
    }

    /*
     * Initializes the sun and its halo in the game.
     *
     * @param windowDimensions The dimensions of the game window.
     */
    private void initializeSun(Vector2 windowDimensions) {
        GameObject sun = Sun.create(windowDimensions, DAY_LENGTH);
        gameObjects().addGameObject(sun, SUN_LAYER);
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, SUN_HALO_LAYER);
    }

    /*
     * Initializes the night scene in the game.
     *
     * @param windowDimensions The dimensions of the game window.
     */
    private void initializeNight(Vector2 windowDimensions) {
        GameObject night = Night.create(windowDimensions, DAY_LENGTH);
        gameObjects().addGameObject(night, NIGHT_LAYER);
    }

    /*
     * Initializes the terrain in the game.
     *
     * @param windowDimensions The dimensions of the game window.
     * @return The initialized terrain object.
     */
    private Terrain initializeTerrain(Vector2 windowDimensions) {
        Terrain terrain = new Terrain(windowDimensions, 0);
        List<Block> ground = terrain.createInRange(0, (int) windowDimensions.x());
        for (var block : ground) {
            gameObjects().addGameObject(block, GROUND_LAYER);
        }
        return terrain;
    }

    /*
     * Initializes the sky in the game.
     *
     * @param windowDimensions The dimensions of the game window.
     */
    private void initializeSky(Vector2 windowDimensions) {
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, SKY_LAYER);
    }

    /**
     * Main method to start the game.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
