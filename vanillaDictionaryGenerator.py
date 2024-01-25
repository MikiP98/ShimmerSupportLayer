import math
import colorsys

from PIL import Image


def get_colors_and_weight(texture_paths: list[str]):
    pixels = []

    for img_pixels in [list(Image.open(path).getdata().convert('RGBA')) for path in texture_paths]:
        pixels += img_pixels

    r_sum = 0
    g_sum = 0
    b_sum = 0
    total_weight = 0

    for pixel in pixels:
        r = pixel[0] / 255
        g = pixel[1] / 255
        b = pixel[2] / 255
        a = pixel[3] / 255

        h, s, v = colorsys.rgb_to_hsv(r, g, b)

        max_chan_val = max(r, max(g, b))

        weight = math.sqrt(r ** 2 + g ** 2 + b ** 2) * (max_chan_val ** 2) * (0.5 + (s * 0.5)) * v * a

        r_sum += r * weight
        g_sum += g * weight
        b_sum += b * weight
        total_weight += weight

    if total_weight == 0:
        print(f"\t\t// Total weight is 0 for texture paths: {texture_paths}")
        raise FileNotFoundError

    colors_and_weight = [
        r_sum,
        g_sum,
        b_sum,
        total_weight
    ]
    return colors_and_weight


if __name__ == '__main__':
    import os

    # print(get_colors_and_weight([
    #     'assets/minecraft/textures/block/blue_candle_lit.png',
    # ]))

    # Got through all textures in path 'assets/minecraft/textures' and any subsequent from it
    path = 'assets/minecraft/textures'

    for root, dirs, files in os.walk(path):
        for file in files:
            if file.endswith(".png"):
                name = f"{root}/{file}".replace("\\\\", '/').replace("//", '\\')
                try:
                    # print(f"{name}: {get_colors_and_weight([f'{name}'])}")
                    type = root.split('/')[-1].split('\\')[-1].upper()
                    print(f"{type}.put(\"{file.split('.')[0]}\", new double[]{{{str(get_colors_and_weight([f'{name}']))[1:-1]}}});")
                    text = f"{file}: {get_colors_and_weight([f'{name}'])}"
                except FileNotFoundError:
                    print(f"\t\t// File not found: {name}")
