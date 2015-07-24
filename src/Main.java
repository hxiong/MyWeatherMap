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
		System.out.println("Please enter the \"CITY NAME, UNIT(F/C)\" -> Chicago,F: ");
		
		// get user input
		Scanner input = new Scanner(System.in);
		String inputStr = input.nextLine();	
	//	System.out.println(inputStr);
		String[] strArr = inputStr.split(",");  // split on comma
		
		// create weather map object
		 OpenWeatherMap owm = new OpenWeatherMap("");
		 CurrentWeather cwd = owm.currentWeatherByCityName("London","UK");
		 System.out.println("temperature at "+ cwd.getCityName() + ": "+ cwd.getMainInstance().getTemperature());
		 
		 
		 // my weather map object
		 MyWeatherMap mwm = new MyWeatherMap("");
		 
		 // set desired temperature unit
		 if(strArr[1].equals("F")){
			 mwm.setUnits(Units.IMPERIAL);
			 mwm.setOwmUnit(Units.IMPERIAL);
		 }else {
			 mwm.setUnits(Units.METRIC);
			 mwm.setOwmUnit(Units.METRIC);
		 }
		 
		 CurrentWeather mcwd = mwm.currentWeatherByCityName(strArr[0]);
		 System.out.println("temperature at "+ mcwd.getCityName() + ": "+ mcwd.getMainInstance().getTemperature());
		 
		 // test by zipcode
		 CurrentWeather zipw = mwm.currentWeatherByZipCode("47906");
		 System.out.println("temperature at "+ zipw.getCityName() + ": "+ zipw.getMainInstance().getTemperature());
		 
	        // getting current weather data for the "London" city
	  //   CurrentWeather cwd = owm.currentWeatherByCityName("London");
		

/*	        // printing the max./min. temperature
	     System.out.println("Temperature: " + cwd.getMainInstance().getMaxTemperature()
	                            + "/" + cwd.getMainInstance().getMinTemperature() + "\'F");
 */  
	    
	}

}
