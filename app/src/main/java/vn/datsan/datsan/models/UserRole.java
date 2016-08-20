package vn.datsan.datsan.models;

/**
 * Created by yennguyen on 8/12/16.
 */
public enum UserRole {
    MEMBER("MEMBER"),
    MODERATOR("MODERATOR"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN");

    private final String text;

    /**
     * @param text
     */
    private UserRole(final String text) {
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
