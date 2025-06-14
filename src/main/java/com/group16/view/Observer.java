package com.group16.view;

import com.group16.model.Subject;

/**
 * An interface for observing {@link Subject} instances in an implementation of the Observer pattern.
 * Classes that implement this interface will be notified when the Subject's state changes.
 */
public interface Observer {

    /**
     * Called by the {@link Subject} to notify the observer of a state change.
     *
     * @param s the subject that has changed
     */
    void update(Subject s);
}
