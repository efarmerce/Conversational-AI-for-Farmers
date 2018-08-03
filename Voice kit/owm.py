import requests

def request(city):
    api_address='http://api.openweathermap.org/data/2.5/weather?appid=<YOURAPIKEY>&q='
    url = api_address + city
    json_data = requests.get(url).json()
    format_add = json_data['main']['temp_min']
    return format_add
def cloudsToday(city):
    api_address='http://api.openweathermap.org/data/2.5/weather?appid=<YOURAPIKEY>&q='
    url = api_address + city
    json_data = requests.get(url).json()
    format_add = json_data['weather'][0]['description']
    return format_add
