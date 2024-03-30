"""
Note:
    This script is executing in blender project, not terminal

Usage:
    Select the object, then execute
    
"""

import os
import bpy
from mathutils import Quaternion, Vector
from math import sin, cos, pi

def xsel(content):
    os.system('echo -n "{}" | xsel -ib'.format(content))
    
def get_rotate_quaternion(degree, axis):
    return Quaternion([cos(degree / 2), sin(degree / 2) * (axis[0]), sin(degree / 2) * (axis[1]), sin(degree / 2) * (axis[2])])

def get_geometry_center(obj):
    """
    { @cite: https://blender.stackexchange.com/questions/62040/get-center-of-geometry-of-an-object }
    """
    x, y, z = [ sum( [v.co[i] for v in obj.data.vertices] ) for i in range(3)]
    count = float(len(obj.data.vertices))
    center = obj.matrix_world @ (Vector( (x, y, z ) ) / count )
    return center
    
def get_obj_player_head_transform(obj):
    obj.rotation_mode = 'XYZ'
    translation = get_geometry_center(obj)
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
    
    # Minecraft render
    left_rotation.rotate(get_rotate_quaternion(pi, (1, 0, 0)))
    
    # Switch to quaternion
    left_rotation = left_rotation.to_quaternion()
    
    return translation, left_rotation, scale

def get_obj_player_head_transform_debug(obj):
    translation, left_rotation, scale = get_obj_player_head_transform(obj)
    command = f"""
            transformation = new Transformation(
            new Vector3f({translation.x}f, {translation.y}f, {translation.z}f),
            new Quaternionf({left_rotation.x}f, {left_rotation.y}f, {left_rotation.z}f, {left_rotation.w}f),
            new Vector3f({scale.x}f, {scale.y}f, {scale.z}f),
            new Quaternionf(0f, 0f, 0f, 1f)
        ); 
    """
    xsel(command)

def get_frame_range(obj):
    fmin, fmax = obj.animation_data.action.frame_range
    fmin = int(fmin)
    fmax = int(fmax)
    return range(fmin, fmax + 1)

def get_transformation(obj):
    obj.rotation_mode = 'XYZ'
    translation = obj.location
    left_rotation = obj.rotation_euler.to_quaternion()
    scale = obj.scale
    
    left_rotation.rotate(get_rotate_quaternion(pi, (0, 1, 0)))
    
    command = f"""
            transformation = new Transformation(
            new Vector3f({translation.x}f, {translation.y}f - 1.8f, {translation.z}f),
            new Quaternionf({left_rotation.x}f, {left_rotation.y}f, {left_rotation.z}f, {left_rotation.w}f),
            new Vector3f({scale.x}f, {scale.y}f, {scale.z}f),
            new Quaternionf(0f, 0f, 0f, 1f)
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
        
        command += f"""
            case {i}:
                transformation = new Transformation(
                    new Vector3f({translation.x}f, {translation.y}f, {translation.z}f),
                    new Quaternionf({left_rotation.x}f, {left_rotation.y}f, {left_rotation.z}f, {left_rotation.w}f),
                    new Vector3f({scale.x}f, {scale.y}f, {scale.z}f),
                    new Quaternionf(0f, 0f, 0f, 1f)
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
