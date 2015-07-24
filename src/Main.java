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
		String[] strArr = inputStr.split(",");  // split on comma
		
		// check general input format
		boolean inputValid = inputVerify(strArr);
		while(inputValid == false){
			System.out.println("wrong input format, enter again:");
			inputStr=input.nextLine();
			strArr=inputStr.split(",");
			inputValid = inputVerify(strArr);
		}
		
		try{
			fetchWeather(strArr);
		}catch(NullPointerException|IndexOutOfBoundsException e){
			System.out.println("check your input format!");
		}
	}
	
	
	
	public static boolean inputVerify(String[] strArr){
		if(strArr.length != 3){
			return false;
		}else if(!(strArr[2].equalsIgnoreCase("c") | strArr[2].equalsIgnoreCase("f"))){
			return false;
		}else if(strArr[0].equalsIgnoreCase("zip") && strArr[1].matches("[-+]?\\d*\\.?\\d+")){
			return true;
		}else if(strArr[0].equalsIgnoreCase("name") && !strArr[1].matches("[-+]?\\d*\\.?\\d+")){
			return true;
		}
		return false;
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
