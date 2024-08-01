package com.evan.dino.manager;

import android.widget.ImageView;

public class BackgroundManager {
    private ImageView backgroundOne;
    private ImageView backgroundTwo;

    public BackgroundManager(ImageView backgroundOne, ImageView backgroundTwo) {
        this.backgroundOne = backgroundOne;
        this.backgroundTwo = backgroundTwo;
    }

    public ImageView getBackgroundOne() {
        return backgroundOne;
    }

    public ImageView getBackgroundTwo() {
        return backgroundTwo;
    }
}
