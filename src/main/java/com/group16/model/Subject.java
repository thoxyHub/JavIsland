package com.group16.model;

import com.group16.view.Observer;

/**
 * Represents the Subject part of the Observer pattern.
 * Classes implementing this interface can be observed by Observers and notify them of changes.
 */
public interface Subject {

    /**
     * Registers an observer to receive updates from this subject.
     *
     * @param o the observer to register
     */
    void registerObserver(Observer o);

    /**
     * Removes a previously registered observer.
     *
     * @param o the observer to remove
     */
    void removeObserver(Observer o);

    /**
     * Notifies all registered observers of a change in this subject.
     */
    void notifyObserver();
}
