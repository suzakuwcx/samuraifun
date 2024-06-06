package Assert.Entity;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import Assert.Font.FontDatabase;
import net.kyori.adventure.text.Component;

public class DeadAnimationEntity extends SpawnEntity<TextDisplay> {
    public DeadAnimationEntity(Location location) {
        super(location, TextDisplay.class);

        TextDisplay display = super.getEntity();
        display.text(Component.text(FontDatabase.ANIMATION_DEAD));
        display.setBrightness(new Brightness(15, 15));
        display.setDefaultBackground(false);
        display.setBillboard(Billboard.VERTICAL);
        display.setBackgroundColor(Color.fromRGB(0, 0, 0).setAlpha(0));
        Transformation mation = display.getTransformation();
        display.setTransformation(
            new Transformation(
                mation.getTranslation().add(0, 2f, 0f),
                mation.getLeftRotation(),
                new Vector3f(1f, 1f, 1f),
                mation.getRightRotation()
            )
        );
    }
}
