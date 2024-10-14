package bricker.main;

import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;

/**
 * Controller class responsible for managing window, input, image, and sound resources.
 */
public class Controller {

    /**
     * Instance of WindowController for managing the game window.
     */
    public final WindowController windowController;

    /**
     * Instance of ImageReader for reading image resources.
     */
    public final ImageReader imageReader;

    /**
     * Instance of SoundReader for reading sound resources.
     */
    public final SoundReader soundReader;

    /**
     * Instance of UserInputListener for handling user input.
     */
    public final UserInputListener inputListener;

    /**
     * Constructs a Controller object.
     *
     * @param imageReader     The ImageReader instance.
     * @param soundReader     The SoundReader instance.
     * @param inputListener   The UserInputListener instance.
     * @param windowController The WindowController instance.
     */
    public Controller(ImageReader imageReader, SoundReader soundReader,
                      UserInputListener inputListener, WindowController windowController) {
        this.windowController = windowController;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
    }
}
