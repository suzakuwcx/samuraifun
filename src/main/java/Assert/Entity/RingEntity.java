package Assert.Entity;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import net.kyori.adventure.text.Component;

public class RingEntity extends SpawnEntity<TextDisplay> {
    private static final char base = 0xea24;

    public RingEntity(Location location, int status) {
        super(location, TextDisplay.class);

        TextDisplay display = super.getEntity();
        display.setBrightness(new Brightness(15, 15));
        display.setBillboard(Billboard.VERTICAL);
        display.setDefaultBackground(false);
        display.text(Component.text((char)(base + status)));
        display.setBackgroundColor(Color.fromRGB(0, 0, 0).setAlpha(0));
        display.setInterpolationDelay(0);
        display.setTeleportDuration(1);
        Transformation mation = display.getTransformation();
        display.setTransformation(
            new Transformation(
                mation.getTranslation().add(0, 1.4f, 0.3f),
                mation.getLeftRotation(),
                new Vector3f(1.7f, 1.7f, 1.7f),
                mation.getRightRotation()
            )
        );
    }
}
