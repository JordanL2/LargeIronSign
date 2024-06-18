# pip install Pillow
from PIL import Image, ImageFont, ImageDraw
import math

# use a truetype font (.ttf)
# font file from: https://www.dafont.com/pixellari.font
font_path = ""
font_name = "Pixellari.ttf"
out_path = font_path + "Output/"

font_size = 16 # px
font_color = "#000000" # HEX Black

# Create Font using PIL
font = ImageFont.truetype(font_path+font_name, font_size)

# Copy Desired Characters from Google Fonts Page and Paste into variable
desired_characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

# Loop through the characters needed and save to desired location
for character in desired_characters:

    # Get text size of character
    (left, top, right, bottom) = font.getbbox(character)
    width = right - left
    height = bottom - top
    offset = math.floor((16 - width)/2)
    print(left, top, right, bottom, width, height, offset)

    # Create PNG Image with that size
    img = Image.new(mode="RGBA", size=(16, 16), color="#FFFFFF")
    draw = ImageDraw.Draw(img)

    # Draw the character
    draw.text((offset, 1), str(character), font=font, fill=font_color)

    # Save the character as png
    try:
        img.save(out_path + "large_sign_" + str(character).lower() + ".png")
    except:
        print(f"[-] Couldn't Save:\t{character}")
