# Written by: Claude AI
#
# This script was used to automate adding the graphic files names to the component JSON

import json

def update_graphics_paths(json_data):
    # Single counter for all components
    counter = 0
    
    for component in json_data:
        # Increment counter for each component
        counter += 1
        
        # Create the new graphics path with sequential number
        base_path = "src/main/resources/it/polimi/it/galaxytrucker/grafiche/tiles/GT-new_tiles_16_for web"
        if counter == 1:
            # First component uses no number
            component["graphic"] = f"{base_path}.jpg"
        else:
            # All subsequent components use sequential numbers
            component["graphic"] = f"{base_path}{counter}.jpg"
    
    return json_data

# Load the JSON data from file
with open('componenttiles.json', 'r') as file:
    data = json.load(file)

# Update the graphics paths
updated_data = update_graphics_paths(data)

# Save the updated JSON data
with open('componenttiles_python_output.json', 'w') as file:
    json.dump(updated_data, file, indent=2)

print("Graphics paths updated successfully!")