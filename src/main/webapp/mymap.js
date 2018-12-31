var map = L.map('map').setView([59.93863, 30.31413], 10);

L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a rel="nofollow" href="http://osm.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);
var mass = [];
var markers = L.featureGroup(mass).addTo(map);

function addStreet(array, num) {
    console.log("array "+ array);
    var list = [];
    var count = 0;
    for(var i = 0; !isNaN(array[i][0]); i++){
        list[i] = array[i];
        count++;
    }
    console.log("list "+list);

    var list2 = [];

    for(var i = 0; i < count; i++){
        list2[i] = [list[i][0], list[i][1]];
    }

    console.log(list2);
    var color;
    if(num < 76){
        color = 'green';
    }else if(num >= 76 && num < 152){
        color = 'yellow';
    }else{
        color = 'red';
    }
    var polyline = L.polyline(list2, {color: color, weight: 5,
        fillOpacity: 0.5,
        smoothFactor: 1}).addTo(map);
    mass.push(polyline);
}
