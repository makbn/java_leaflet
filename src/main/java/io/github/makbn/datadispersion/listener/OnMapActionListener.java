package io.github.makbn.datadispersion.listener;

public interface OnMapActionListener {

    void click(double lat, double lng);

    void move();

    void moveEnd();

    void zoom();


}
