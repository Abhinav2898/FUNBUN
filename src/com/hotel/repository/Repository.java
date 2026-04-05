package com.hotel.repository;

import java.util.ArrayList;
import java.util.List;

// WEEK 7 - Generics: Generic class that works for ANY type T
// WEEK 8 - Collections: Uses List interface and ArrayList
public class Repository<T> {

    // WEEK 8 - List Interface with ArrayList implementation
    protected List<T> items;

    public Repository() {
        // WEEK 8 - ArrayList from Collection Framework
        this.items = new ArrayList<>();
    }

    // WEEK 7 - Generic method: works for any type T
    public void add(T item) {
        items.add(item);
    }

    public void remove(T item) {
        items.remove(item);
    }

    // WEEK 8 - Returns List interface type (not ArrayList directly)
    public List<T> getAll() {
        return new ArrayList<>(items); // return copy
    }

    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    // WEEK 8 - Using iterator-style access
    public boolean contains(T item) {
        return items.contains(item);
    }

    // Replace all items (used when loading from file)
    public void setAll(List<T> newItems) {
        this.items = new ArrayList<>(newItems);
    }
}
