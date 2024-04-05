"""
Generate each frame of progress bar

Usage: ./generate_bar.py

Put the 'barbase.png' in the same folder as script
Then will generate all target image in './target' folder
"""

from PIL import Image, ImageDraw
import os

def redraw_border(draw):
    white_color = (255, 255, 255, 255)

    draw.rectangle((3, 1, 252, 1), fill=white_color)
    draw.rectangle((3, 5, 252, 5), fill=white_color)

    draw.rectangle((1, 2, 2, 4), fill=white_color)
    draw.point((3, 2), fill=white_color)
    draw.point((3, 4), fill=white_color)

    draw.rectangle((253, 2, 254, 4), fill=white_color)
    draw.point((252, 2), fill=white_color)
    draw.point((252, 4), fill=white_color)

if __name__ == "__main__":
    img = Image.open("barbase.png")
    draw = ImageDraw.Draw(img)
    (x, y) = img.size

    # to reverse order
    buff = []

    os.makedirs("target", exist_ok=True)
    for i in range(250, -1, -1):
        draw.rectangle([3 + i, 0, 253, y], fill=(0, 0, 0, 0))
        redraw_border(draw)
        img.save("target/bar_{}.png".format(i))
        buff.append(r'{{"type":"bitmap","file":"ui/bar_{}.png","height":4,"ascent":-5,"chars":["\u{}"]}}'.format(i, hex(0xebc0 + i)[2:]))

    f = open("font.json", "w+")
    for line in reversed(buff):
        f.write(line)
        f.write(",\n")

    f.close()
        