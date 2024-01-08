# coding=utf-8

import csv
import json
import math
import colorsys

from PIL import Image


class Color:
    def __init__(self, r, g, b):
        self.r = r
        self.g = g
        self.b = b


def read_light_blocks_csv(file_path):
    data_list = []
    try:
        with open(file_path, 'r') as file:
            # Create a CSV reader
            reader = csv.reader(file, delimiter=';')

            # Iterate through rows and add data to the list
            for row in reader:
                if len(row) == 3:
                    mod_id, block_id, radius = row
                    data_list.append({
                        "mod_id": mod_id,
                        "block_id": block_id,
                        "radius": int(radius)  # Assuming radius is a numeric value
                    })
                else:
                    print(f"Invalid row: {row}")

    except FileNotFoundError:
        print(f"File '{file_path}' not found.")
    except Exception as e:
        print(f"An error occurred: {e}")

    return data_list


def reformat(data_list):
    reformatted_data = {}

    for entry in data_list:
        mod_id = entry["mod_id"]
        block_id = entry["block_id"]
        radius = entry["radius"]

        if mod_id not in reformatted_data:
            reformatted_data[mod_id] = {"blocks": {}}

        reformatted_data[mod_id]["blocks"][block_id] = int(radius)

    return reformatted_data


def get_average_color(texture_paths: list[str]):
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

        weight = math.sqrt(r**2 + g**2 + b**2) * (max_chan_val**2) * (0.5 + (s * 0.5)) * v * a

        r_sum += r * weight
        g_sum += g * weight
        b_sum += b * weight
        total_weight += weight

    average_color = [
        int(round((r_sum / total_weight) * 255)),
        int(round((g_sum / total_weight) * 255)),
        int(round((b_sum / total_weight) * 255))
    ]
    return average_color


def get_color(mod_id, block_id):
    print(f"Starting to get color for block '{block_id}' in mod '{mod_id}'...")

    # Construct paths
    model_path = f"assets/{mod_id}/models/block/{block_id}.json"

    # Read the block model JSON
    with open(model_path, "r") as json_file:
        model_data = json.load(json_file)

    texture_image_paths = []
    # Iterate through textures in the model
    for texture_path in model_data.get("textures", {}).values():
        if texture_path.startswith("block/"):
            # Extract texture ID
            texture_id = texture_path[len("block/"):]

            # Form the path to the texture image
            texture_image_paths.append(f"assets/minecraft/textures/block/{texture_id}.png")
        else:
            # Extract other_mod_id and texture ID
            other_mod_id, texture_id = texture_path.split(":block/")

            # Form the path to the texture image
            texture_image_paths.append(f"assets/{other_mod_id}/textures/block/{texture_id}.png")

    print(f"Texture image paths: {texture_image_paths}")
    avg_r, avg_g, avg_b = get_average_color(texture_image_paths)

    print(f"End of block: {block_id}")
    print(f"Average color: {avg_r}, {avg_g}, {avg_b}")
    # saturated_color = saturate(Color(avg_r, avg_g, avg_b), 1.5)
    # print(f"Saturated color: {saturated_color.r}, {saturated_color.g}, {saturated_color.b}")
    return Color(avg_r, avg_g, avg_b)


def rgb_to_hsl(color):
    r, g, b = color.r / 255.0, color.g / 255.0, color.b / 255.0
    h, l, s = colorsys.rgb_to_hls(r, g, b)
    return h, l, s


def hsl_to_rgb(h, s, l):
    r, g, b = colorsys.hls_to_rgb(h, l, s)
    return int(r * 255), int(g * 255), int(b * 255)


def saturate(color, factor):
    """
    Increase the saturation of a color.

    Parameters:
    - color: A Color object with 'r', 'g', and 'b' components.
    - factor: Saturation increase factor. Should be greater than 1.

    Returns:
    A new Color object with increased saturation.
    """
    h, l, s = rgb_to_hsl(color)
    s *= factor
    s = min(1.0, s)  # Ensure saturation is within the valid range [0, 1]
    new_color = hsl_to_rgb(h, s, l)
    return Color(*new_color)


if __name__ == '__main__':
    search_phrases = {
        "minecraft": "jar",
        "betternether": "better-nether"
    }

    # Load the data from "light_blocks_plus_radius.csv"
    # File format: mod_id, block_id, radius
    # Example: minecraft;torch;15

    # Specify the file path
    file_path = "light_blocks_plus_radius.csv"

    # Call the function to read and get the data as a list
    data = read_light_blocks_csv(file_path)
    # print(data)

    # Reformat the data for easier use
    reformatted_data = reformat(data)

    # For each mod, print the mod id and the blocks with their radius
    # for mod_id, mod_data in reformatted_data.items():
    #     print(f"Mod ID: {mod_id}")
    #     for block_id, radius in mod_data["blocks"].items():
    #         print(f"Block ID: {block_id}, Radius: {radius}")
    #     print()

    print("Starting to generate Java code...")
    # TODO: Write this comment
    java_code = ""

    for mod_id, mod_data in reformatted_data.items():
        temp_java_code = ""

        temp_java_code += f"// Mod ID: {mod_id}\n"
        temp_java_code += f"ArrayList<Color> {mod_id}Colors = new ArrayList<>();\n"
        temp_java_code += f"ArrayList<LightSource> {mod_id}LightSourceBlocks = new ArrayList<>();\n"

        for block_id, radius in mod_data["blocks"].items():
            # Call the function to get the color
            try:
                color = get_color(mod_id, block_id)
            except FileNotFoundError:
                print(
                    f"Model or texture not found for block '{block_id}' in mod '{mod_id}'. Or there were no valid pixels in the texture.")
                continue

            temp_java_code += f"{mod_id}Colors.add(new Color(\"{block_id}_weight_average_color\", {int(color.r)}, {int(color.g)}, {int(color.b)}, Config.auto_alpha));\n"

            temp_java_code += f"{mod_id}LightSourceBlocks.add(new LightSource(\"{block_id}\", \"{block_id}_weight_average_color\", {radius}));\n"

        search_phrase = mod_id
        try:
            search_phrase = search_phrases[mod_id]
        except KeyError:
            pass

        temp_java_code += f"supportedMods.add(new SupportedMod(\"{mod_id}\", \"{mod_id}\", \"{search_phrase}\", {mod_id}Colors, {mod_id}LightSourceBlocks, {mod_id}LightSourceItems));\n\n"

        java_code += temp_java_code

    print()
    print("End of Java code generation.")
    print()
    print(java_code)
