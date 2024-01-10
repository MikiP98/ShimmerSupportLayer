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
                if len(row) == 4:
                    mod_id, block_id, radius, extra_states = row
                    data_list.append({
                        "mod_id": mod_id,
                        "block_id": block_id,
                        "radius": int(radius),
                        "extra_states": extra_states
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
        extra_states = entry["extra_states"]

        if mod_id not in reformatted_data:
            reformatted_data[mod_id] = {"blocks": []}

        reformatted_data[mod_id]["blocks"].append({"block_id": block_id, "radius": radius, "extra_states": extra_states})
        # reformatted_data[mod_id]["blocks"][block_id] = int(radius)

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

    if total_weight == 0:
        print(f"Total weight is 0 for texture paths: {texture_paths}")
        raise FileNotFoundError

    average_color = [
        int(round((r_sum / total_weight) * 255)),
        int(round((g_sum / total_weight) * 255)),
        int(round((b_sum / total_weight) * 255))
    ]
    return average_color


def get_color(mod_id, block_id):
    print(f"Starting to get color for block '{block_id}' in mod '{mod_id}'...")

    blockstate_path = f"assets/{mod_id}/blockstates/{block_id}.json"
    model_paths = set()

    # Read the blockstate JSON
    with open(blockstate_path, "r") as json_file:
        blockstate_data = json.load(json_file)

        if "variants" in blockstate_data:
            for variant in blockstate_data["variants"]:
                params = variant.split(",")
                lit_false = False
                for param in params:
                    if param == "lit=false":
                        lit_false = True
                if lit_false:
                    continue
                # print(blockstate_data["variants"].get(variant))
                # print(blockstate_data["variants"].get(variant)["model"])
                model_module = blockstate_data["variants"].get(variant)
                if type(model_module) is list:
                    for element in model_module:
                        model_paths.add(element["model"])
                else:
                    model_paths.add(model_module["model"])

        elif "multipart" in blockstate_data:
            for part in blockstate_data["multipart"]:
                if "apply" in part:
                    if type(part["apply"]) is list:
                        for element in part["apply"]:
                            model_paths.add(element["model"])
                    else:
                        model_paths.add(part["apply"]["model"])

    print(f"Model paths: {model_paths}")

    real_model_paths = []
    for model_path in model_paths:
        if model_path.startswith("block/"):
            real_model_paths.append(f"assets/minecraft/models/{model_path}.json")
        else:
            other_mod_id, model_id = model_path.split(":block/")
            real_model_paths.append(f"assets/{other_mod_id}/models/block/{model_id}.json")

    # model_path = f"assets/{mod_id}/models/block/{block_id}.json"

    # Read the block model JSON
    # with open(model_path, "r") as json_file:
    #     model_data = json.load(json_file)

    texture_image_paths = []
    # Iterate through model paths
    for model_path in real_model_paths:
        with open(model_path, "r") as json_file:
            model_data = json.load(json_file)

        # Iterate through textures in the model
        for texture_path in model_data.get("textures", {}).values():
            if texture_path.startswith("block/"):
                # Extract texture ID
                texture_id = texture_path[len("block/"):]

                # Form the path to the texture image
                texture_image_paths.append(f"assets/minecraft/textures/block/{texture_id}.png")
            else:
                # Extract other_mod_id and texture ID
                try:
                    other_mod_id, texture_id = texture_path.split(":block/")
                except ValueError:
                    print(f"Invalid texture path: {texture_path}")
                    continue

                # Form the path to the texture image
                texture_image_paths.append(f"assets/{other_mod_id}/textures/block/{texture_id}.png")

    print(f"Texture image paths: {texture_image_paths}")
    avg_r, avg_g, avg_b = get_average_color(texture_image_paths)

    print(f"End of block: {block_id}")
    print(f"Average color: {avg_r}, {avg_g}, {avg_b}")
    print()
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


def check_item(mod_id, block_id):
    item_path = f"assets/{mod_id}/models/item/{block_id}.json"

    try:
        with open(item_path, "r") as json_file:
            json.load(json_file)
    except FileNotFoundError:
        return False

    return True


if __name__ == '__main__':
    search_phrases = {
        "minecraft": "jar",
        "betternether": "better-nether",
        "betterend": "better-end"
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
        temp_java_code = f"// Mod ID: {mod_id}\n"

        temp_java_code += f"ArrayList<Color> {mod_id}Colors = new ArrayList<>();\n"
        temp_java_code += f"ArrayList<LightSource> {mod_id}LightSourceBlocks = new ArrayList<>();\n"
        temp_java_code += f"ArrayList<LightSource> {mod_id}LightSourceItems = new ArrayList<>();\n\n"

        for entry in mod_data["blocks"]:
            block_id, radius, extra_states = entry["block_id"], entry["radius"], entry["extra_states"]
            # print(f"Block ID: {block_id}, Radius: {radius}, Extra states: {extra_states}")

            # Call the function to get the color
            try:
                color = get_color(mod_id, block_id)
            except FileNotFoundError:
                print(
                    f"Blockstate or model not found for block '{block_id}' in mod '{mod_id}'. Or there were no valid pixels in the textures.")
                continue

            temp_java_code += f"{mod_id}Colors.add(new Color(\"{block_id}_weight_average_color\", {int(color.r)}, {int(color.g)}, {int(color.b)}, Config.auto_alpha));\n"

            if extra_states != "":
                temp_java_code += f"{mod_id}LightSourceBlocks.add(new LightSource(\"{block_id}\", \"{block_id}_weight_average_color\", {radius}, \"{extra_states}\"));\n"
            else:
                temp_java_code += f"{mod_id}LightSourceBlocks.add(new LightSource(\"{block_id}\", \"{block_id}_weight_average_color\", {radius}));\n"

            if check_item(mod_id, block_id):
                temp_java_code += f"{mod_id}LightSourceItems.add(new LightSource(\"{block_id}\", \"{block_id}_weight_average_color\", {radius}));\n"

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
