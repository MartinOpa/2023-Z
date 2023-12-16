// vlastní grafika
function convertWMO(code) {
    switch(code) {
        case 0: // clear
            return ['res/weather/sunny.png', 'slunečno', '☀️'];
        case 1: 
        case 2:
        case 3: // clear, cloudy, overcast
            return ['res/weather/cloudy.png', 'oblačno', '☁️'];
        case 45:
        case 48: // fog
            return ['res/weather/foggy.png', 'mlha', '☁️'];
        case 51:
        case 53:
        case 55:
        case 56:
        case 57: // drizzle / freezing drizzle
            return ['res/weather/drizzle.png', 'mrholení', '☔'];
        case 61:
        case 63:
        case 65:
        case 80:
        case 81:
        case 82: // rain slight/moderate/heavy / rain showers
            return ['res/weather/rain.png', 'déšť', '☔'];
        case 66:
        case 67: // freezing rain light/heavy
            return ['res/weather/freezingRain.png', 'déšť se sněhem', '☔'];
        case 71:
        case 73:
        case 75:
        case 77:
        case 85:
        case 86: // snow slight/moderate/heavy/grains / snow showers
            return ['res/weather/snow.png', 'sněžení', '❄️'];
        case 95:
        case 96:
        case 99: // thunder / hail
            return ['res/weather/thunder.png', 'bouřky', '⛈️'];
        default:
            return ['res/weather/cloudy.png', 'oblačno', '☁️'];
    }
}

function updateWeather() {
    const url = `https://api.open-meteo.com/v1/forecast?latitude=49.82&longitude=18.26&current=temperature_2m,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=Europe%2FBerlin&forecast_days=3`;
    
    fetch(url)
        .then(response => response.json())
        .then(data => {            
            document.getElementById('current-temperature').textContent = `${data.current.temperature_2m}°C`;
            document.getElementById('current-conditions').src = `${convertWMO(data.current.weather_code)[0]}`;
            document.getElementById('current-conditions-text').textContent = `${convertWMO(data.current.weather_code)[1]}`;
            document.getElementById('weatherHeader').textContent = `${convertWMO(data.current.weather_code)[2] + " Počasí"}`;
            
            document.getElementById('tomorrow-temperature').textContent = `${data.daily.temperature_2m_max[1]}°C`;
            document.getElementById('tomorrow-conditions').src = `${convertWMO(data.daily.weather_code[1])[0]}`;
            document.getElementById('tomorrow-conditions-text').textContent = `${convertWMO(data.daily.weather_code[1])[1]}`;
            
            document.getElementById('day-after-temperature').textContent = `${data.daily.temperature_2m_max[2]}°C`;
            document.getElementById('day-after-conditions').src = `${convertWMO(data.daily.weather_code[2])[0]}`;
            document.getElementById('day-after-conditions-text').textContent = `${convertWMO(data.daily.weather_code[2])[1]}`;
        })
        .catch(err => {
            console.error('weather: ', err);
        });
}

function fetchWeather() {
    updateWeather();
    setInterval(updateWeather, 30000);
}

