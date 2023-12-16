function handleWindowResizeMap() {
    var map = L.map('map');
    map.invalidateSize();
}

function initMap() {
    var options = {
        center: [49.83191, 18.16099],
        zoom: 18
    }
    
    var map = new L.map('map-leaflet', options);
    var layer = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
    map.addLayer(layer);

    window.addEventListener('resize', handleWindowResizeMap);
    handleWindowResizeMap();
}

