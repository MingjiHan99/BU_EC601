import googlemaps
from datetime import datetime

gmaps = googlemaps.Client(key='You API Key')

location = gmaps.geolocate()
print(f"Your current location: {location['location']}")
return_values = gmaps.places(location=location['location'], type='restaurant', radius=3000)

print("Here are restaurents and addresses in your nearby areas:")
for idx, res in enumerate(return_values['results']):
    print(f"{idx}. {res['name']} {res['formatted_address']}")
print('Select one using number:')
num = input()
num = int(num)

destination = return_values['results'][num]["geometry"]['location']


route = gmaps.directions(location['location'], destination)
print("Here is the route from your current location to restaurant:")
for leg in route[0]['legs']:
    for step in leg['steps']:
        print(step['html_instructions']
              .replace("<b>", "")
              .replace('</b>', '')
              .replace("</div>", '')
              .replace('<div style="font-size:0.9em">', ' ')
              .replace('/<wbr/>', ' '))
