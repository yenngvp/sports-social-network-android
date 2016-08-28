package vn.datsan.datsan.models.chat;

/**
 * Created by yennguyen on 7/31/16.
 */
public enum MessageType {
    TEXT_MESSAGE("TEXT_MESSAGE"),
    PHOTO_MESSAGE("PHOTO_MESSAGE"),
    LOCATION_MESSAGE("LOCATION_MESSAGE"),
    CONTACT_MESSAGE("CONTACT_MESSAGE");

    private final String text;

    /**
     * @param text
     */
    private MessageType(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
