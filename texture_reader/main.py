from PIL import Image
import numpy as np

from calc_average_color import get_average_color

def print_pixels(pixels):
    for pixel in pixels:
        if pixel[3] == 0:
            continue
        print(pixel)


def channel_avg(pixels):
    total_weight = 0
    total_r, total_g, total_b = 0, 0, 0
    for pixel in pixels:
        r, g, b, a = pixel
        normalized_alpha = a/255

        total_r += r * normalized_alpha
        total_g += g * normalized_alpha
        total_b += b * normalized_alpha

        total_weight += normalized_alpha

    # print(total, total_weight)
    return {round(total_r/total_weight), round(total_g/total_weight), round(total_b/total_weight)}


def channel_weight_avg(pixels):
    total_weight_r, total_weight_g, total_weight_b = 0, 0, 0
    total_r, total_g, total_b = 0, 0, 0
    for pixel in pixels:
        r, g, b, a = pixel
        normalized_alpha = a/255

        weight_r, weight_g, weight_b = r * normalized_alpha, g * normalized_alpha, b * normalized_alpha

        total_r += r * weight_r
        total_g += g * weight_g
        total_b += b * weight_b

        total_weight_r += weight_r
        total_weight_g += weight_g
        total_weight_b += weight_b

    # print(total_r, total_weight_r)
    return {round(total_r/total_weight_r), round(total_g/total_weight_g), round(total_b/total_weight_b)}

def test_run(img_paths):
    pixels = []
    for img_path in img_paths:
        img = Image.open(img_path).convert('RGBA')
        pixels += img.getdata()

    # print(pixels)
    # print_pixels(pixels)
    print()
    print("Block: " + img_paths[0])
    print("weight, s-v corrected color avg (new): " + str(get_average_color(img_paths)))
    print("weight channel avg (old): " + str(channel_weight_avg(pixels)))
    print("channel avg: " + str(channel_avg(pixels)))


if __name__ == '__main__':
    img_path = 'VanillaDefault_1.20.4/assets/minecraft/textures/block/torch.png'
    test_run([img_path])

    img_path = 'VanillaDefault_1.20.4/assets/minecraft/textures/block/glowstone.png'
    test_run([img_path])

    img_path = 'blue_crying_obsidian.png'
    test_run([img_path])

    img_paths = [
        'VanillaDefault_1.20.4/assets/minecraft/textures/block/fire_0.png',
        'VanillaDefault_1.20.4/assets/minecraft/textures/block/fire_1.png'
    ]
    test_run(img_paths)
