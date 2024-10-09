import java.util.Random;

/**
 * An edited version of the base file for the ChatterBot exercise.
 * The bot's <code>replyTo</code> method receives a statement and return an appropriate response to it,
 * from an array of replies as supplied to it via its constructor, according to the legality of the statement
 * - that is whether it starts with the constant <code>REQUEST_PREFIX</code> or not.
 * The response may use the given statement, by replacing it with one of the constants:
 * <code>PLACEHOLDER_FOR_REQUESTED_PHRASE</code> or <code>PLACEHOLDER_FOR_ILLEGAL_REQUEST</code>, if they
 * appear in the selected reply.
 *
 * @author Dan Nirel
 * @editor Roei Dahuki
 */
class ChatterBot {
    /* Constants: */
    /**
     * The prefix of a legal given statement for the ChatterBot.
     */
    public static final String REQUEST_PREFIX = "say ";

    /**
     * The string that should be replaced in formatted replies to legal requests.
     */
    public static final String PLACEHOLDER_FOR_REQUESTED_PHRASE = "<phrase>";

    /**
     * The string that should be replaced in formatted replies to illegal requests.
     */
    public static final String PLACEHOLDER_FOR_ILLEGAL_REQUEST = "<request>";

    /* Fields: */
    private String name;
    private Random rand = new Random();
    private String[] legalRequestsReplies;
    private String[] repliesToIllegalRequest;

    /**
     * Constructs an object of the <code>ChatterBot</code> class.
     *
     * @param name                    the name of this bot.
     * @param repliesToLegalRequest   an array of possible replies for the bot to answer to legal requests.
     * @param repliesToIllegalRequest an array of possible replies for the bot to answer to illegal requests.
     */
    public ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
        this.name = name;
        this.legalRequestsReplies = new String[repliesToLegalRequest.length];
        System.arraycopy(repliesToLegalRequest, 0,
                this.legalRequestsReplies, 0, repliesToLegalRequest.length);
        this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
        System.arraycopy(repliesToIllegalRequest, 0,
                this.repliesToIllegalRequest, 0, repliesToIllegalRequest.length);
    }

    /**
     * Getter for the ChatterBot's name.
     *
     * @return the object's name field.
     */
    public String getName() {
        return name;
    }

    /**
     * Generate and return the reply of a ChatterBot to the statement it receives.
     *
     * @param statement a string to reply to.
     * @return the generated response.
     */
    public String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            return replyToLegalRequest(statement);
        }
        return replyToIllegalRequest(statement);
    }

    /**
     * Generates the response of the ChatterBot in case of legal given request
     * (a.k.a. <code>REQUEST_PREFIX+phrase</code>), according to the array <code>repliesToLegalRequest</code>
     * given in the constructor.
     *
     * @param statement the legal given request.
     * @return the generated response.
     */
    public String replyToLegalRequest(String statement) {
        // we don’t repeat the request prefix, so delete it from the replied phrase:
        String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
        return replacePlaceholderInARandomPattern(legalRequestsReplies, PLACEHOLDER_FOR_REQUESTED_PHRASE,
                phrase);
    }

    /**
     * Generates the response of the ChatterBot in case of illegal given request
     * (a.k.a. <code>statement</code>), according to the array <code>repliesToIllegalRequest</code>
     * given in the constructor.
     *
     * @param statement the illegal given request.
     * @return the generated response.
     */
    public String replyToIllegalRequest(String statement) {
        return replacePlaceholderInARandomPattern(repliesToIllegalRequest, PLACEHOLDER_FOR_ILLEGAL_REQUEST,
                statement);
    }

    /**
     * Generates a response - first chooses randomly a format out of the given <code>repliesPatterns</code>,
     * then replace all <code>placeholder</code>'s appearances with the given string <code>replacer</code>.
     *
     * @param repliesPatterns an array of the patterns to choose randomly a format from.
     * @param placeholder     the string that may appear in the random pattern and shall be replaced.
     * @param replacer        the new string to replace with all the appearances of <code>placeholder</code>.
     * @return the generated response.
     */
    public String replacePlaceholderInARandomPattern(String[] repliesPatterns, String placeholder,
                                                     String replacer) {
        int randomIndex = rand.nextInt(repliesPatterns.length);
        String responsePattern = repliesPatterns[randomIndex];  // choosing randomly a response
        // replace the placeholder with given replacer string:
        return responsePattern.replaceAll(placeholder, replacer);
    }
}
