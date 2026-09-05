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
    private final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public SelectionItem(T value, String name, boolean available, String unavailableReason) {
        this.value = value;
        this.name = name;
        this.available = available;
        this.unavailableReason = unavailableReason;
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

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }
}
