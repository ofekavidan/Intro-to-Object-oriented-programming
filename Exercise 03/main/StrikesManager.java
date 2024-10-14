package bricker.main;

import bricker.gameobjects.Heart;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A sub-manager of the BrickerGameManager, that manages the strikes display to the player.
 * Includes a graphic and numeric display.
 */
public class StrikesManager {

    private static final int MAX_NUMBER_OF_STRIKES = 4;
    private static final int MIN_NUMBER_OF_STRIKES = 1;
    private static final int INIT_NUMBER_OF_STRIKES = 3;

    private final BrickerGameManager brickerGameManager;
    private final Heart[] hearts;  // for graphic display
    private final Renderable heartImage;  // for graphic display
    private final int spaceBetweenStrikesObjects;
    private final Vector2 basicTopLeftCorner;
    private final Vector2 basicDimensions;
    private final TextRenderable numberImage;  // for numeric display
    private int curNumberOfStrikes;


    /**
     * Constructs a new StrikesManager object.
     * Initializes the StrikesManager with the provided parameters and initializes its variables.
     *
     * @param brickerGameManager         The BrickerGameManager instance associated with this StrikesManager.
     * @param heartSize                  The size of each heart in the graphic representation.
     * @param spaceBetweenStrikesObjects The spacing between each heart in the graphic representation.
     */
    public StrikesManager(BrickerGameManager brickerGameManager, float heartSize,
                          int spaceBetweenStrikesObjects) {
        Controller controller = brickerGameManager.getController();

        // Assigning parameters and initializing variables
        this.brickerGameManager = brickerGameManager;
        this.spaceBetweenStrikesObjects = spaceBetweenStrikesObjects;
        this.curNumberOfStrikes = 0;
        this.basicTopLeftCorner = new Vector2(brickerGameManager.getLeftRightBordersLimits()[0],
                controller.windowController.getWindowDimensions().y() - heartSize
                        - spaceBetweenStrikesObjects);
        this.basicDimensions = new Vector2(heartSize, heartSize);

        // Initializing graphic representation:
        this.heartImage = controller.imageReader.readImage(Constants.HEART_IMAGE_PATH,
                true);
        this.hearts = new Heart[MAX_NUMBER_OF_STRIKES];
        while (this.curNumberOfStrikes < INIT_NUMBER_OF_STRIKES) {
            addHeart();
        }

        // Initializing numeric representation:
        this.numberImage = new TextRenderable(Constants.EMPTY_STRING);
        setNumber(this.curNumberOfStrikes);
        this.brickerGameManager.addGameObject(new GameObject(basicTopLeftCorner,
                basicDimensions, this.numberImage), Layer.UI);
    }

    /*
     * Calculates the color associated with a given number of strikes.
     *
     * @param number The number of strikes.
     * @return The Color associated with the provided number of strikes.
     */
    private static Color calcColor(int number) {
        switch (number) {
            case 1:
                return Color.red;
            case 2:
                return Color.yellow;
            default:
                return Color.green;
        }
    }

    /*
     * Updates the visual representation of the current number of strikes.
     *
     * @param newNumber The new number of strikes to be displayed.
     */
    private void setNumber(int newNumber) {
        numberImage.setString(Integer.toString(newNumber));
        numberImage.setColor(calcColor(newNumber));
    }

    /*
     * Adds a new heart (strike indicator), if legal, to the visual representation of strikes.
     */
    private void addHeart() {
        if (curNumberOfStrikes < MAX_NUMBER_OF_STRIKES) {
            Vector2 heartTopLeftCorner = basicTopLeftCorner.add(
                    new Vector2((curNumberOfStrikes + 1) * (basicDimensions.x() +
                            spaceBetweenStrikesObjects), 0)
            );
            Heart newHeart = new Heart(heartTopLeftCorner, basicDimensions, heartImage);
            this.hearts[curNumberOfStrikes++] = newHeart;
            brickerGameManager.addGameObject(newHeart, Layer.UI);
        }
    }

    /**
     * Adds a strike to the current count of strikes, if it is less than the max value, and updates the
     * numeric and graphic representation accordingly.
     */
    public void addStrike() {
        addHeart();
        setNumber(curNumberOfStrikes);
    }


    /**
     * Removes a strike from the current count of strikes, if there is any, and updates the numeric and
     * graphic representation accordingly.
     */
    public void removeStrike() {
        if (MIN_NUMBER_OF_STRIKES <= curNumberOfStrikes) {
            Heart heartToRemove = hearts[--curNumberOfStrikes];
            hearts[curNumberOfStrikes] = null;
            brickerGameManager.removeGameObject(heartToRemove, Layer.UI);
            setNumber(curNumberOfStrikes);
        }
    }

    /**
     * Retrieves the current number of strikes.
     *
     * @return The current number of strikes.
     */
    public int getNumberOfStrikes() {
        return curNumberOfStrikes;
    }

    /**
     * Retrieves the minimum number of strikes allowed.
     *
     * @return The minimum number of strikes allowed.
     */
    public int getMinNumberOfStrikes() {
        return MIN_NUMBER_OF_STRIKES;
    }

}
