package com.group16.model;

import com.group16.view.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the logic for the title screen menu.
 * Implements the Subject interface to notify observers of state changes.
 */
public class TitleLogic implements Subject {

    /**
     * Represents the two possible states of the title menu.
     */
    public enum MenuState {
        START,
        MAP_SELECT
    }

    private MenuState menuState = MenuState.START;
    private int selectedOption = 0;

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Gets the current menu state.
     *
     * @return the current menu state
     */
    public MenuState getMenuState() {
        return menuState;
    }

    /**
     * Sets the menu state and notifies observers.
     *
     * @param menuState the new menu state
     */
    public void setMenuState(MenuState menuState) {
        this.menuState = menuState;
        notifyObserver();
    }

    /**
     * Gets the currently selected menu option index.
     *
     * @return the selected option index
     */
    public int getSelectedOption() {
        return selectedOption;
    }

    /**
     * Sets the selected menu option index and notifies observers.
     *
     * @param selectedOption the new selected option index
     */
    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
        notifyObserver();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}
