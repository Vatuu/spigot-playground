package dev.vatuu.test.util;

import lombok.Getter;

import java.util.*;

@Getter
public class TrackableQueue<T> extends ArrayList<T> {

    private int currentIndex = 0;

    public T getNext() {
        if(currentIndex + 1 >= size())
            return null;
        else
            return get(++currentIndex);
    }

    public T getPrevious() {
        if(currentIndex - 1 < 0)
            return null;
        else
            return get(--currentIndex);
    }

    public T getCurrent() {
        return get(currentIndex);
    }

    public void setIndex(int index) {
        this.currentIndex = index;
    }

    public void moveIndex(int amount) {
        this.currentIndex += amount;
    }
}
