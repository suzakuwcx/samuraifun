"""
Note:
    This script is executing in blender project, not terminal

Usage:
    Select the object, then execute
    
"""

import os
import bpy
from mathutils import Quaternion
from math import sin, cos, pi

def xsel(content):
    os.system('echo -n "{}" | xsel -ib'.format(content))
    
def get_obj_player_head_transform(obj):
    obj.rotation_mode = 'XYZ'
    translation = obj.location
    left_rotation = obj.rotation_euler
    scale = obj.scale

    # mirror with Y-Z flat, to switch view between two hands
    translation.x = -translation.x
    
    left_rotation.y = -left_rotation.y
    left_rotation.z = -left_rotation.z

    # mirror with flat (0, -1 ,1)
    tmp = translation.y
    translation.y = translation.z
    translation.z = tmp
    
    left_rotation.x = - pi / 2 - left_rotation.x
    tmp_y = left_rotation.y
    tmp_z = left_rotation.z
    left_rotation.y = tmp_z
    left_rotation.z = pi - tmp_y

    # player head height is 1.8
    translation.y -= 1.8
    
    # Switch to quaternion
    left_rotation = left_rotation.to_quaternion()
    
    return translation, left_rotation, scale

def get_frame_range(obj):
    fmin, fmax = obj.animation_data.action.frame_range
    fmin = int(fmin)
    fmax = int(fmax)
    return range(fmin, fmax + 1)

def get_transformation(obj):
    obj.rotation_mode = 'QUATERNION'
    translation = obj.location
    left_rotation = obj.rotation_quaternion
    scale = obj.scale
    command = f"""
           transformation = new Transformation(
            new Vector3f({translation.x}f, {translation.y}f - 1.8f, {translation.z}f),
            new Quaternionf({left_rotation.x}f, {left_rotation.y}f, {left_rotation.z}f, {left_rotation.w}f),
            new Vector3f({scale.x}f, {scale.y}f, {scale.z}f),
            new Quaternionf(0f, 1f, 0f, 0f)
        ); 
    """
    xsel(command)

def main():
    if not len(bpy.context.selected_objects) == 1:
        xsel("No Object Selected")
        return

    command = f"""
    @Override
    public void run() {{
        Matrix4f mat;
        Transformation transformation;
        if (tick == 1) {{
            ItemStack item = new ItemStack(Material.GOLDEN_SWORD);
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(1);
            item.setItemMeta(meta);
            display.setItemStack(item);
        }}
        switch (tick) {{
    """

    obj = bpy.context.selected_objects[0]
    sce = bpy.context.scene
    for i, f in enumerate(get_frame_range(obj)):
        sce.frame_set(f)
        translation, left_rotation, scale = get_obj_player_head_transform(obj.copy())
        
        # Note, the display in minecraft is different with blender, so the right_rotation is (0, 1, 0, 0), not (0, 0, 0, 1)
        command += f"""
            case {i}:
                transformation = new Transformation(
                    new Vector3f({translation.x}f, {translation.y}f, {translation.z}f),
                    new Quaternionf({left_rotation.x}f, {left_rotation.y}f, {left_rotation.z}f, {left_rotation.w}f),
                    new Vector3f({scale.x}f, {scale.y}f, {scale.z}f),
                    new Quaternionf(0f, 1f, 0f, 0f)
                );
                display.setInterpolationDelay(0);
                display.setInterpolationDuration(1);
                display.setTransformation(transformation);
                break;
        """

    command += f"""
            default:
                display.remove();
                task_mapper.remove(player);
                return;
        }}
        ++tick;
        Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
    }}
    """

    xsel(command)

if __name__ == "__main__":
    main()
