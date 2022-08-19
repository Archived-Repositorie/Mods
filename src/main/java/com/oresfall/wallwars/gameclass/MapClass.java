package com.oresfall.wallwars.gameclass;

import com.oresfall.wallwars.utls.Utils;
import com.sk89q.worldedit.extent.clipboard.Clipboard;

public class MapClass {
    private final String file;
    private final Clipboard map;

    public MapClass(String file, Clipboard map) {
        this.file = file;
        this.map = map;
    }

    public MapClass(String file) {
        this.file = file;
        this.map = Utils.readSchem(file);
    }

    public Clipboard getMap() {
        return map;
    }

    public String getFile() {
        return file;
    }
}
