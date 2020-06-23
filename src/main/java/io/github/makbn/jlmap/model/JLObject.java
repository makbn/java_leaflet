package io.github.makbn.jlmap.model;

import io.github.makbn.jlmap.listener.OnJLObjectActionListener;

/**
 * by: Mehdi Akbarian Rastaghi (@makbn)
 */
public abstract class JLObject<T extends JLObject> {
    private OnJLObjectActionListener<T> listener;

    public OnJLObjectActionListener<T> getOnActionListener() {
        return listener;
    }

    public void setOnActionListener(OnJLObjectActionListener<T> listener) {
        this.listener = listener;
    }

    public abstract int getId();

    public void update(Object... params) {

    }
}
