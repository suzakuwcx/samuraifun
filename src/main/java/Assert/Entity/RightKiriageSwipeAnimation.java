package Assert.Entity;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;

public class RightKiriageSwipeAnimation extends SwordSwipeAnimation {
    public RightKiriageSwipeAnimation(Location location) {
        this(location, 1);
    }

    public RightKiriageSwipeAnimation(Location location, int type) {
        super(location, type);

        ItemDisplay display = super.getEntity();
        Transformation mation = display.getTransformation();
        display.setTransformation(
            new Transformation(
                mation.getTranslation().add(0, -0.4f, 0),
                mation.getLeftRotation().rotateLocalZ((float)0.3),
                mation.getScale(),
                mation.getRightRotation()
            )
        );
    }
}
