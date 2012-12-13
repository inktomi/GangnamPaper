package zh.wang.android.utils.YahooWeather4a;

public class ConditionDefinition {
	
	String[] conditionArray = new String[] {
			"tornado",
			"tropical storm",
			"hurricane",
			"severe thunderstorms",
			"thunderstorms",
			"mixed rain and snow",
			"mixed rain and sleet",
			"mixed snow and sleet",
			"freezing drizzle",
			"drizzle",
			"freezing rain",
			"showers",
			"showers",
			"snow flurries",
			"light snow showers",
			"blowing snow",
			"snow",
			"hail",
			"sleet",
			"dust",
			"foggy",
			"haze",
			"smoky",
			"blustery",
			"windy",
			"cold",
			"cloudy",
			"mostly cloudy (night)",
			"mostly cloudy (day)",
			"partly cloudy (night)",
			"partly cloudy (day)",
			"clear (night)",
			"sunny",
			"fair (night)",
			"fair (day)",
			"mixed rain and hail",
			"hot",
			"isolated thunderstorms",
			"scattered thunderstorms",
			"scattered thunderstorms",
			"scattered showers",
			"heavy snow",
			"scattered snow showers",
			"heavy snow",
			"partly cloudy",
			"thundershowers",
			"snow showers",
			"isolated thundershowers",
			"not available",
	};
	
	public String getConditionByCode(int code) {
		return conditionArray[code];
	}
}
