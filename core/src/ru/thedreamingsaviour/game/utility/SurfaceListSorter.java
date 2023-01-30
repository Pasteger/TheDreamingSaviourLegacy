package ru.thedreamingsaviour.game.utility;

import ru.thedreamingsaviour.game.gameobject.Surface;

import java.util.ArrayList;
import java.util.List;

public class SurfaceListSorter {
    private SurfaceListSorter(){}
    public static List<Surface> sortSurfaceList(List<Surface> surfaceList){
        List<Surface> newSurfaceList = new ArrayList<>();
        surfaceList.stream().filter(surface -> surface.getEffect().equals("none")).forEach(newSurfaceList::add);
        surfaceList.stream().filter(surface -> surface.getEffect().equals("gravity")).forEach(newSurfaceList::add);
        surfaceList.stream().filter(surface -> surface.getEffect().equals("draw_under")).forEach(newSurfaceList::add);
        surfaceList.stream().filter(surface -> surface.getEffect().equals("solid")).forEach(newSurfaceList::add);
        surfaceList.stream().filter(surface -> surface.getEffect().equals("death")).forEach(newSurfaceList::add);
        surfaceList.stream().filter(surface -> surface.getEffect().equals("draw_over")).forEach(newSurfaceList::add);
        surfaceList.stream().filter(surface -> !newSurfaceList.contains(surface)).forEach(newSurfaceList::add);

        return newSurfaceList;
    }
}
