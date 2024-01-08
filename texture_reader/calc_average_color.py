import math

from PIL import Image
import colorsys


def get_average_color(texture_paths: list[str]):
    pixels = []

    for img_pixels in [list(Image.open(path).getdata().convert('RGBA')) for path in texture_paths]:
        pixels += img_pixels

    r_sum = 0
    g_sum = 0
    b_sum = 0
    total_weight = 0

    for index in range(len(pixels)):
        pixel = pixels[index]

        r = pixel[0] / 255
        g = pixel[1] / 255
        b = pixel[2] / 255
        a = pixel[3] / 255

        h, s, v = colorsys.rgb_to_hsv(r, g, b)

        max_chan_val = max(r, max(g, b))

        weight = math.sqrt(r * r + g * g + b * b) * (max_chan_val * max_chan_val) * (0.5 + (s * 0.5)) * v * a

        r_sum += r * weight
        g_sum += g * weight
        b_sum += b * weight
        total_weight += weight

    average_color = [
        int((r_sum / total_weight) * 255),
        int((g_sum / total_weight) * 255),
        int((b_sum / total_weight) * 255)
    ]
    return average_color


if __name__ == '__main__':
    print(get_average_color([
        'VanillaDefault_1.20.4/assets/minecraft/textures/block/blue_candle_lit.png',
    ]))
