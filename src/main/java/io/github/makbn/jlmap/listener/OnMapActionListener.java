package io.github.makbn.jlmap.listener;

public interface OnMapActionListener {

    void click(double lat, double lng);

    void move();

    void moveEnd();

    void zoom();


}
