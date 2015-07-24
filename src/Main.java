import java.io.IOException;
import java.net.MalformedURLException;

import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import net.aksingh.owmjapis.OpenWeatherMap.Units;

import org.json.*;

import java.util.*;


public class Main {

	public static void main(String[] args) throws IOException, JSONException {
		//throws JSONException {
		
		System.out.println("Want to check out the weather?");
		System.out.println("Please enter the \"ZIP/NAME, ZIPCODE/CITY NAME, UNIT(F/C)\"");
		System.out.println("example: ZIP, 47906,C");
		
		// get user input
		Scanner input = new Scanner(System.in);
		String inputStr = input.nextLine();	
	//	System.out.println(inputStr);
		String[] strArr = inputStr.split(",");  // split on comma
		
		fetchWeather(strArr);
		
	}
	
	public static void fetchWeather(String[] strArr) throws IOException, JSONException{
		 
		 // my weather map object
		 MyWeatherMap mwm = new MyWeatherMap("");
		 
		 // set desired temperature unit
		 if(strArr[2].equalsIgnoreCase("F")){
			 mwm.setUnits(Units.IMPERIAL);
			 mwm.setOwmUnit(Units.IMPERIAL);
		 }else {
			 mwm.setUnits(Units.METRIC);
			 mwm.setOwmUnit(Units.METRIC);
		 }
		 
		 // search by city name or zipcode
		 if(strArr[0].equalsIgnoreCase("NAME")){
			 CurrentWeather mcwd = mwm.currentWeatherByCityName(strArr[1]);  //my current weather data
			 System.out.println("temperature at "+ mcwd.getCityName() + ": "+ mcwd.getMainInstance().getTemperature());
			 System.out.println("Max temp, Min temp : "+mcwd.getMainInstance().getMaxTemperature()+", "+mcwd.getMainInstance().getMinTemperature());
			 System.out.println("overall weather: " + mcwd.getWeatherInstance(0).getWeatherDescription());
			 
		 }else if(strArr[0].equalsIgnoreCase("ZIP")){
		 // test by zipcode
			 CurrentWeather mcwd = mwm.currentWeatherByZipCode(strArr[1]);
			 System.out.println("temperature at "+ mcwd.getCityName() + ": "+ mcwd.getMainInstance().getTemperature());
			 System.out.println("Max temp, Min temp : "+mcwd.getMainInstance().getMaxTemperature()+", "+mcwd.getMainInstance().getMinTemperature());
			 System.out.println("overall weather: " + mcwd.getWeatherInstance(0).getWeatherDescription());
			// System.out.println("temperature at "+ mcwd.getCityName() + ": "+ mcwd.getMainInstance().getTemperature());
		 }
	}

}
