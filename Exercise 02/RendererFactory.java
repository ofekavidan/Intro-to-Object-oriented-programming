
/**
 * A factory for generating renderers, using specific implementations of the generic interface "Renderer".
 * This helps to ensure the validity of the principle of individual responsibility.
 */
public class RendererFactory {

    /**
     * Generates a renderer of specific type, according to a given string.
     *
     * @param type the string represents the wanted type of renderer.
     * @param size the size of the board to be displayed by this renderer.
     * @return a generated renderer.
     */
    public Renderer buildRenderer(String type, int size) {
        Renderer renderer = null;
        switch (type) {
            case "none":
                renderer = new VoidRenderer();
                break;
            case "console":
                renderer = new ConsoleRenderer(size);
                break;
        }
        return renderer;
    }

}
