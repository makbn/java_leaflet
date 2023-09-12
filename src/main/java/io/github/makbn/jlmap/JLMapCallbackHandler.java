package io.github.makbn.jlmap;

import com.google.gson.Gson;
import io.github.makbn.jlmap.listener.OnJLMapViewListener;
import io.github.makbn.jlmap.listener.OnJLObjectActionListener;
import io.github.makbn.jlmap.listener.event.ClickEvent;
import io.github.makbn.jlmap.listener.event.MoveEvent;
import io.github.makbn.jlmap.listener.event.ZoomEvent;
import io.github.makbn.jlmap.model.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Mehdi Akbarian Rastaghi (@makbn)
 */
@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JLMapCallbackHandler implements Serializable {

    transient JLMapView mapView;
    transient HashMap<Class<? extends JLObject<?>>, HashMap<Integer, JLObject<?>>> jlObjects;
    transient Gson gson;
    HashMap<String, Class<? extends JLObject<?>>[]> classMap;

    public JLMapCallbackHandler(JLMapView mapView) {
        this.mapView = mapView;
        this.jlObjects = new HashMap<>();
        this.gson = new Gson();
        this.classMap = new HashMap<>();
        initClassMap();
    }
    @SuppressWarnings("unchecked")
    private void initClassMap() {

        classMap.put("marker", new Class[]{JLMarker.class});
        classMap.put("marker_circle", new Class[]{JLCircleMarker.class});
        classMap.put("polyline", new Class[]{JLPolyline.class, JLMultiPolyline.class});
        classMap.put("polygon", new Class[]{JLPolygon.class});
    }

    /**
     * @param functionName name of source function from js
     * @param param1       name of object class
     * @param param2       id of object
     * @param param3       additional param
     * @param param4       additional param
     * @param param5       additional param
     */
    @SuppressWarnings("unchecked")
    public void functionCalled(String functionName, Object param1, Object param2,
                               Object param3, Object param4, Object param5) {
        log.debug(String.format("function: %s \tparam1: %s \tparam2: %s " +
                        "\tparam3: %s param4: %s \tparam5: %s%n"
                , functionName, param1, param2, param3, param4, param5));
        try {
            //get target class of Leaflet layer in JL Application
            Class<?>[] targetClasses = classMap.get(param1);

            //function called by an known class
            if (targetClasses != null) {
                //one Leaflet class may map to multiple class in JL Application
                // like ployLine mapped to JLPolyline and JLMultiPolyline
                for (Class<?> targetClass : targetClasses) {
                    if (targetClass != null) {
                        //search for the other JLObject class if available
                        if (!jlObjects.containsKey(targetClass))
                            break;

                        JLObject<?> jlObject = jlObjects.get(targetClass)
                                .get(Integer.parseInt(String.valueOf(param2)));

                        //search for the other JLObject object if available
                        if (jlObject == null)
                            break;

                        if (jlObject.getOnActionListener() == null)
                            return;

                        //call listener and exit loop
                        if (callListenerOnObject(functionName,
                                (JLObject<JLObject<?>>) jlObject, param1,
                                param2, param3, param4, param5))
                            return;
                    }
                }
            } else if (param1.equals("main_map")
                    && mapView.getMapListener().isPresent()) {
                switch (functionName) {
                    case "move" -> mapView.getMapListener()
                            .get()
                            .onAction(new MoveEvent(OnJLMapViewListener.Action.MOVE,
                                    gson.fromJson(String.valueOf(param4), JLLatLng.class),
                                    gson.fromJson(String.valueOf(param5), JLBounds.class),
                                    Integer.parseInt(String.valueOf(param3))));
                    case "movestart" -> mapView.getMapListener()
                            .get()
                            .onAction(new MoveEvent(OnJLMapViewListener.Action.MOVE_START,
                                    gson.fromJson(String.valueOf(param4), JLLatLng.class),
                                    gson.fromJson(String.valueOf(param5), JLBounds.class),
                                    Integer.parseInt(String.valueOf(param3))));
                    case "moveend" -> mapView.getMapListener()
                            .get()
                            .onAction(new MoveEvent(OnJLMapViewListener.Action.MOVE_END,
                                    gson.fromJson(String.valueOf(param4), JLLatLng.class),
                                    gson.fromJson(String.valueOf(param5), JLBounds.class),
                                    Integer.parseInt(String.valueOf(param3))));
                    case "click" -> mapView.getMapListener()
                            .get()
                            .onAction(new ClickEvent(gson.fromJson(String.valueOf(param3),
                                    JLLatLng.class)));

                    case "zoom" -> mapView.getMapListener()
                            .get()
                            .onAction(new ZoomEvent(OnJLMapViewListener.Action.ZOOM,
                                    Integer.parseInt(String.valueOf(param3))));
                    default -> log.error(functionName + " not implemented!");
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private boolean callListenerOnObject(
            String functionName, JLObject<JLObject<?>> jlObject, Object... params) {
        switch (functionName) {
            case "move" -> {
                jlObject.getOnActionListener()
                        .move(jlObject, OnJLObjectActionListener.Action.MOVE);
                return true;
            }
            case "movestart" -> {
                jlObject.getOnActionListener()
                        .move(jlObject, OnJLObjectActionListener.Action.MOVE_START);
                return true;
            }
            case "moveend" -> {
                //update coordinate of the JLObject
                jlObject.update("moveend", gson.fromJson(String.valueOf(params[3]), JLLatLng.class));
                jlObject.getOnActionListener()
                        .move(jlObject, OnJLObjectActionListener.Action.MOVE_END);
                return true;
            }
            case "click" -> {
                jlObject.getOnActionListener()
                        .click(jlObject, OnJLObjectActionListener.Action.CLICK);
                return true;
            }
            default -> log.error(functionName + " not implemented!");
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void addJLObject(JLObject<?> object) {
        if (jlObjects.containsKey(object.getClass())) {
            jlObjects.get(object.getClass())
                    .put(object.getId(), object);
        } else {
            HashMap<Integer, JLObject<?>> map = new HashMap<>();
            map.put(object.getId(), object);
            jlObjects.put((Class<? extends JLObject<?>>) object.getClass(), map);
        }
    }

    public void remove(Class<? extends JLObject<?>> targetClass, int id) {
        if (!jlObjects.containsKey(targetClass))
            return;
        JLObject<?> object = jlObjects.get(targetClass).remove(id);
        if (object != null)
            log.error(targetClass.getSimpleName() + " id:" + object.getId() + " removed");
    }
}
