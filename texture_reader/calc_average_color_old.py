import math

from PIL import Image
import colorsys

img = Image.open('VanillaDefault_1.20.4/assets/minecraft/textures/block/glowstone.png').convert('RGBA')

pixels = img.getdata()
weights = []

p_index = 0
r_sum = 0
g_sum = 0
b_sum = 0
total_weight = 0

for x in range(img.size[0]):
    for y in range(img.size[1]):
        pixel = pixels[p_index]

        r = pixel[0] / 255
        g = pixel[1] / 255
        b = pixel[2] / 255
        a = pixel[3] / 255

        h, s, v = colorsys.rgb_to_hsv(r, g, b)

        max_chan_val = max(r, max(g, b))

        weight = math.sqrt(r * r + g * g + b * b) * (max_chan_val * max_chan_val) * (0.5 + (s / 2)) * v * a

        r_sum += r * weight
        g_sum += g * weight
        b_sum += b * weight
        total_weight += weight

        print(pixel, end=' ')

        p_index += 1
    print()

average_color = [
    int((r_sum / total_weight) * 255),
    int((g_sum / total_weight) * 255),
    int((b_sum / total_weight) * 255)
]

print(average_color)