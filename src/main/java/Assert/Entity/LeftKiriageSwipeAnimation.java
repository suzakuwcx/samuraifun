package Assert.Entity;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;

public class LeftKiriageSwipeAnimation extends SwordSwipeAnimation {
    public LeftKiriageSwipeAnimation(Location location) {
        super(location);

        ItemDisplay display = super.getEntity();
        Transformation mation = display.getTransformation();
        display.setTransformation(
            new Transformation(
                mation.getTranslation().mul(-1).add(0, 0.4f, 0),
                mation.getLeftRotation().rotateLocalZ((float) (Math.PI - 0.3)),
                mation.getScale(),
                mation.getRightRotation()
            )
        );
    }
}
