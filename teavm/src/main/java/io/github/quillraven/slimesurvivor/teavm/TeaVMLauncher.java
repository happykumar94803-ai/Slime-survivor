package io.github.quillraven.slimesurvivor.teavm;

import com.github.xpenatan.gdx.backends.teavm.TeaApplication;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import io.github.quillraven.slimesurvivor.GdxGame;

/**
 * Launches the TeaVM/HTML application.
 */
public class TeaVMLauncher {
    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        //// If width and height are each greater than 0, then the app will use a fixed size.
        //config.width = 640;
        //config.height = 480;
        //// If width and height are both 0, then the app will use all available space.
        config.width = 0;
        config.height = 0;
        config.useGL30 = true; // needed to correctly render the wrapped background
        config.preloadListener = assetLoader -> assetLoader.loadScript("freetype.js");
        new TeaApplication(new GdxGame(), config);
    }
}
