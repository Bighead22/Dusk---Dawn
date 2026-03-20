import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Launcher
{
    public static void main (String[] args) {
        // To start a LibGDX program, this method:
        // (1) creates an instance of the game
        // (2) creates a new application with game instance and window settings as arguments
        MyGame game = new MyGame();
        Lwjgl3Application launcher = new Lwjgl3Application(game, getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        
        configuration.setTitle("Game Dev");

        //Set window size
        configuration.setWindowedMode(800, 500);

        //Set window icon
        configuration.setWindowIcon("assets/libgdx128.png", "assets/libgdx64.png", "assets/libgdx32.png", "assets/libgdx16.png");
        
        // Vsync limits the frames per second to what your hardware can display, and helps eliminate
        // screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        
        // Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        // refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        // If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        // useful for testing performance, but can also be very stressful to some hardware.
        // You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.

        return configuration;
    }
}