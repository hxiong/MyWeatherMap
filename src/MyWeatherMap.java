import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;
import java.io.*;
import net.aksingh.owmjapis.CurrentWeather;
import net.aksingh.owmjapis.OpenWeatherMap;
import org.json.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import org.apache.*;


public class MyWeatherMap extends OpenWeatherMap {
	 /*
    URLs and parameters for OWM.org
     */
    private static final String URL_API = "http://api.openweathermap.org/data/2.5/";
    private static final String URL_CURRENT = "weather?";
    private static final String URL_HOURLY_FORECAST = "forecast?";
    private static final String URL_DAILY_FORECAST = "forecast/daily?";

    private static final String PARAM_COUNT = "cnt=";
    private static final String PARAM_CITY_NAME = "q=";
    private static final String PARAM_CITY_ID = "id=";
    private static final String PARAM_LATITUDE = "lat=";
    private static final String PARAM_LONGITUDE = "lon=";
    private static final String PARAM_MODE = "mode=";
    private static final String PARAM_UNITS = "units=";
    private static final String PARAM_APPID = "appId=";
    private static final String PARAM_LANG = "lang=";
	
	 /*
    Instance Variables
     */
    private final MyOWMAddress owmAddress;
    private final MyOWMResponse owmResponse;
    private final MyOWMProxy owmProxy;
    
    public MyWeatherMap(String apiKey) {
		super(apiKey);
		this.owmAddress = new MyOWMAddress(Units.IMPERIAL, Language.ENGLISH, apiKey);
	     this.owmProxy = new MyOWMProxy(null, Integer.MIN_VALUE, null, null);
	     this.owmResponse = new MyOWMResponse(owmAddress, owmProxy);
		// TODO Auto-generated constructor stub
	}
	public MyWeatherMap(Units units, String apiKey) {
		 super(units, apiKey);
		 this.owmAddress = new MyOWMAddress(units, Language.ENGLISH, apiKey);
	     this.owmProxy = new MyOWMProxy(null, Integer.MIN_VALUE, null, null);
	     this.owmResponse = new MyOWMResponse(owmAddress, owmProxy);
		// TODO Auto-generated constructor stub
	}
	
	public MyWeatherMap(Units units, Language lang, String apiKey) {
			super(units, lang, apiKey);
	        this.owmAddress = new MyOWMAddress(units, lang, apiKey);
	        this.owmProxy = new MyOWMProxy(null, Integer.MIN_VALUE, null, null);
	        this.owmResponse = new MyOWMResponse(this.owmAddress, this.owmProxy);
	}
	
	// get weather by zip code
	 public CurrentWeather currentWeatherByZipCode(String zipCode)
	            throws IOException, JSONException {
	        String response = owmResponse.currentWeatherByZipCode(zipCode);
	        return this.currentWeatherFromRawResponse(response);
	 }
	 
	 private static class MyOWMResponse {
	        private final MyOWMAddress owmAddress;
	        private final MyOWMProxy owmProxy;

	        public MyOWMResponse(MyOWMAddress owmAddress, MyOWMProxy owmProxy) {
	            this.owmAddress = owmAddress;
	            this.owmProxy = owmProxy;
	        }   
	        
	       public String currentWeatherByZipCode(String zipCode) {
	            String address = owmAddress.currentWeatherByZipCode(zipCode);
	            return httpGET(address);
	       }
	       
	       private String httpGET(String requestAddress) {
	            URL request;
	            HttpURLConnection connection = null;
	            BufferedReader reader = null;

	            String tmpStr;
	            String response = null;

	            try {
	                request = new URL(requestAddress);

	                if (owmProxy.getProxy() != null) {
	                    connection = (HttpURLConnection) request.openConnection(owmProxy.getProxy());
	                } else {
	                    connection = (HttpURLConnection) request.openConnection();
	                }

	                connection.setRequestMethod("GET");
	                connection.setUseCaches(false);
	                connection.setDoInput(true);
	                connection.setDoOutput(false);
	                connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
	                connection.connect();

	                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	                    String encoding = connection.getContentEncoding();

	                    try {
	                        if (encoding != null && "gzip".equalsIgnoreCase(encoding)) {
	                            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
	                        } else if (encoding != null && "deflate".equalsIgnoreCase(encoding)) {
	                            reader = new BufferedReader(new InputStreamReader(new InflaterInputStream(connection.getInputStream(), new Inflater(true))));
	                        } else {
	                            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                        }

	                        while ((tmpStr = reader.readLine()) != null) {
	                            response = tmpStr;
	                        }
	                    } catch (IOException e) {
	                        System.err.println("Error: " + e.getMessage());
	                    } finally {
	                        if (reader != null) {
	                            try {
	                                reader.close();
	                            } catch (IOException e) {
	                                System.err.println("Error: " + e.getMessage());
	                            }
	                        }
	                    }
	                } else { // if HttpURLConnection is not okay
	                    try {
	                        reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
	                        while ((tmpStr = reader.readLine()) != null) {
	                            response = tmpStr;
	                        }
	                    } catch (IOException e) {
	                        System.err.println("Error: " + e.getMessage());
	                    } finally {
	                        if (reader != null) {
	                            try {
	                                reader.close();
	                            } catch (IOException e) {
	                                System.err.println("Error: " + e.getMessage());
	                            }
	                        }
	                    }

	                    // if response is bad
	                    System.err.println("Bad Response: " + response + "\n");
	                    return null;
	                }
	            } catch (IOException e) {
	                System.err.println("Error: " + e.getMessage());
	                response = null;
	            } finally {
	                if (connection != null) {
	                    connection.disconnect();
	                }
	            }

	            return response;
	        }
	 }
	 
	 public void setOwmUnit(Units unit){
		 owmAddress.setOwmUnits(unit);
	 }
	 
	 public static class MyOWMAddress{
		 
			 private static final String MODE = "json";
		        private static final String ENCODING = "UTF-8";
		        
		        private String mode;
		        private Units units;
		        private String appId;
		        private Language lang;
		        
		  public MyOWMAddress(String appId) {
			  	this.appId = appId;
			  	this.units = Units.IMPERIAL;
			  	this.lang = Language.ENGLISH;
	       }

	      public MyOWMAddress(Units units, String appId) {
	    	    this.appId = appId;
			  	this.units = units;
			  	this.lang = Language.ENGLISH;
	       }

	      
	      public MyOWMAddress(Units units, Language lang, String appId){
	            this.mode = MODE;
	            this.units = units;
	            this.lang = lang;
	            this.appId = appId;
	      }
	      
	      private void setOwmUnits(Units units) {
	            this.units = units;
	      }
	      
	      public String currentWeatherByZipCode(String zipCode) {
	            return new StringBuilder()
	                    .append(URL_API).append(URL_CURRENT)
	                 //   .append("zip=").append(zipCode).append("&")
	                    .append("zip=").append(zipCode).append(",us").append("&")
	                    .append(PARAM_MODE).append(this.mode).append("&")
	                    .append(PARAM_UNITS).append(this.units).append("&")
	                    .append(PARAM_LANG).append(this.lang).append("&")
	                    .append(PARAM_APPID).append(this.appId)
	                    .toString();
	      }
	      
	 }

	 
	   /**
	     * Proxifies the default HTTP requests
	     *
	     * @since 2.5.0.5
	     */
	 private static class MyOWMProxy {
	        private String ip;
	        private int port;
	        private String user;
	        private String pass;

	        private MyOWMProxy(String ip, int port, String user, String pass) {
	            this.ip = ip;
	            this.port = port;
	            this.user = user;
	            this.pass = pass;
	        }

	        public Proxy getProxy() {
	            Proxy proxy = null;

	            if (ip != null && (! "".equals(ip)) && port != Integer.MIN_VALUE) {
	                proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
	            }

	            if (user != null && (! "".equals(user)) && pass != null && (! "".equals(pass))) {
	                Authenticator.setDefault(getAuthenticatorInstance(user, pass));
	            }

	            return proxy;
	        }

	        private Authenticator getAuthenticatorInstance(final String user, final String pass) {
	            Authenticator authenticator = new Authenticator() {

	                public PasswordAuthentication getPasswordAuthentication() {
	                    return (new PasswordAuthentication(user, pass.toCharArray()));
	                }
	            };

	            return authenticator;
	        }

	        public void setIp(String ip) {
	            this.ip = ip;
	        }

	        public void setPort(int port) {
	            this.port = port;
	        }

	        public void setUser(String user) {
	            this.user = user;
	        }

	        public void setPass(String pass) {
	            this.pass = pass;
	        }
	    }
} 
