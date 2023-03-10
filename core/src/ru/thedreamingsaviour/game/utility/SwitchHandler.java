package ru.thedreamingsaviour.game.utility;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.thedreamingsaviour.game.gameobject.Surface;
import ru.thedreamingsaviour.game.gameobject.Switch;

import java.util.ArrayList;
import java.util.List;

public class SwitchHandler {
    private final List<Switch> switches;
    private final List<Surface> surfaces;
    private final long surfacesId;

    public SwitchHandler(List<Switch> switches, List<Surface> surfaceList, long surfacesId) {
        this.surfaces = new ArrayList<>();
        this.switches = switches;
        this.surfacesId = surfacesId;
        surfaceList.stream().filter(surface -> surface.id == surfacesId).forEach(this.surfaces::add);
    }

    public SwitchHandler(long surfacesId) {
        this.surfaces = new ArrayList<>();
        this.switches = new ArrayList<>();
        this.surfacesId = surfacesId;
    }

    public void handle(SpriteBatch batch) {
        switches.forEach(aSwitch -> aSwitch.draw(batch));

        if (switches.stream().allMatch(Switch::isActive)) {
            surfaces.stream().filter(surface -> surface.id == 1).forEach(surface -> {
                surface.setEffect("none");
                surface.setStandardColor("1;0.8902;0.6941;1");
            });
        } else {
            surfaces.stream().filter(surface -> surface.id == 1).forEach(surface -> {
                surface.setEffect("gravity");
                surface.setStandardColor("0.30;0.56;0.87;1");
            });
        }

        if (switches.stream().allMatch(Switch::isActive)) {
            surfaces.stream().filter(surface -> surface.id == 2).forEach(surface -> {
                surface.setEffect("none");
                surface.setStandardColor("1;0.8902;0.6941;1");
            });
        } else {
            surfaces.stream().filter(surface -> surface.id == 2).forEach(surface -> {
                surface.setEffect("solid");
                surface.setStandardColor("0;0;0;1");
            });
        }

        if (switches.stream().allMatch(Switch::isActive)) {
            surfaces.stream().filter(surface -> surface.id == 11).forEach(surface -> {
                surface.setEffect("none");
                surface.setStandardColor("1;0.8902;0.6941;1");
            });
        } else {
            surfaces.stream().filter(surface -> surface.id == 11).forEach(surface -> {
                surface.setEffect("solid");
                surface.setStandardColor("1.0;0.84313726;0.0;1");
            });
        }

        if (switches.stream().allMatch(Switch::isActive)) {
            surfaces.stream().filter(surface -> surface.id == 12).forEach(surface -> {
                surface.setEffect("none");
                surface.setStandardColor("1;0.8902;0.6941;1");
            });
        } else {
            surfaces.stream().filter(surface -> surface.id == 12).forEach(surface -> {
                surface.setEffect("solid");
                surface.setStandardColor("0.8980392;0.16862746;0.3137255;1");
            });
        }

        if (switches.stream().allMatch(Switch::isActive)) {
            surfaces.stream().filter(surface -> surface.id == 13).forEach(surface -> {
                surface.setEffect("none");
                surface.setStandardColor("1;0.8902;0.6941;1");
            });
        } else {
            surfaces.stream().filter(surface -> surface.id == 13).forEach(surface -> {
                surface.setEffect("solid");
                surface.setStandardColor("0.4;0.4;1.0;1");
            });
        }

        if (switches.stream().allMatch(Switch::isActive)) {
            surfaces.stream().filter(surface -> surface.id == 3).forEach(surface -> {
                surface.setEffect("gravity");
                surface.setStandardColor("0.30;0.56;0.87;1");
            });
        } else {
            surfaces.stream().filter(surface -> surface.id == 3).forEach(surface -> {
                surface.setEffect("solid");
                surface.setStandardColor("0;0;0;1");
            });
        }
    }

    public List<Switch> getSwitches() {
        return switches;
    }

    public long getSurfacesId() {
        return surfacesId;
    }
}
