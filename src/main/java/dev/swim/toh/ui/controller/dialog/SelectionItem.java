package dev.swim.toh.ui.controller.dialog;

import javafx.beans.property.SimpleBooleanProperty;

/**
 * One row in {@link SelectionDialogController}: a value the user can pick, its display name,
 * and - if it isn't currently allowed (e.g. an unmet feat prerequisite) - a reason why. Rows are
 * shown regardless of availability, matching how this app already lets the player add feats
 * whose prerequisites aren't (yet) met, rather than blocking the choice outright.
 */
public class SelectionItem<T> {

    private final T value;
    private final String name;
    private final boolean available;
    private final String unavailableReason;
    private final String infoTitle;
    private final String infoBody;
    private final boolean header;
    private final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
    // nesting depth for display, e.g. a feat shown indented under the feat it builds on - 0
    // (the default) means "top level", set by the caller after construction since it depends on
    // where the item ends up among the other items, not on the item itself
    private int depth = 0;

    public SelectionItem(T value, String name, boolean available, String unavailableReason) {
        this(value, name, available, unavailableReason, null, null);
    }

    /**
     * @param infoTitle short lead-in text for the "more info" popup, e.g. a feat's short
     *                  description; {@code null} (together with infoBody) hides the info button
     *                  for values that don't have anything to show, e.g. classes today.
     * @param infoBody  the full text shown below infoTitle in the popup.
     */
    public SelectionItem(T value, String name, boolean available, String unavailableReason,
                          String infoTitle, String infoBody) {
        this(value, name, available, unavailableReason, infoTitle, infoBody, false);
    }

    private SelectionItem(T value, String name, boolean available, String unavailableReason,
                           String infoTitle, String infoBody, boolean header) {
        this.value = value;
        this.name = name;
        this.available = available;
        this.unavailableReason = unavailableReason;
        this.infoTitle = infoTitle;
        this.infoBody = infoBody;
        this.header = header;
    }

    /**
     * A non-selectable section heading row, e.g. "Kriegswaffen" grouping a weapon catalog the
     * same way the PHB table does - has no value, never ends up in
     * {@link SelectionDialogController#getSelectedValues()}.
     */
    public static <T> SelectionItem<T> header(String label) {
        return new SelectionItem<>(null, label, false, null, null, null, true);
    }

    public boolean isHeader() {
        return header;
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getUnavailableReason() {
        return unavailableReason;
    }

    public boolean hasInfo() {
        return infoTitle != null && infoBody != null;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public String getInfoBody() {
        return infoBody;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }
}
