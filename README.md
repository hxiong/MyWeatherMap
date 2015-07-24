# MyWeatherMap
Class MyWeatherMap inherits OpenWeatherMap;
it can be instantiated to check weather by feed in city name or zip code;

# featured methods
# get current weather json object
MyWeatherMap mwm = new MyWeatherMap(""); # create map object
CurrentWeather cwd = mwm.currentWeatherByCityName(cityName);  # search by city name
CurrentWeather cwd = mwm.currentWeatherByZipCode(zipCode);  # search by zip code

# get temperature 
cwd.getMainInstance.getTemperature();

# get cityname
mwm.getCityName();

# get weather description
cwd.getWeatherInstance(0).getWeatherDescription();

# more methods, check the source files

