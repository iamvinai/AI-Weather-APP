// src/main/resources/META-INF/resources/index.js
window.getLocation = function() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            // Handle location data
            alert("Latitude: " + position.coords.latitude + ", Longitude: " + position.coords.longitude);
            // Optionally send data to Vaadin
            window.Vaadin.Flow.clients[0].getUI().get().getPage().executeJavaScript(
                'handleLocationData($0, $1);',
                position.coords.latitude,
                position.coords.longitude
            );
        }, function(error) {
            console.error("Geolocation error: " + error.message);
        });
    } else {
        console.log("Geolocation is not supported by this browser.");
    }
};

function handleLocationData(latitude, longitude) {
    alert("Received location data: Latitude - " + latitude + ", Longitude - " + longitude);
    // Optionally handle the data or communicate with Vaadin
}