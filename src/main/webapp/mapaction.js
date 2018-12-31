var params = new URLSearchParams(window.location.search);

var street = params.get('street');

if (street === 'all') {
    mapAll();
} else {
    mapStreet(street);
}

function mapAll() {
    var body = 'method=streetList';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", '/Servlet', false);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(body);
    var res = xhr.responseText;
    var strList = res.split("\"");
    for (var i = 0; i < strList.length; i++) {
        if (strList[i].length > 2) {
            mapStreet(strList[i]);
        }
    }
}

function mapStreet(street) {

    var body = 'method=find&street=' + street;
    var xhr = new XMLHttpRequest();
    xhr.open("POST", '/Servlet', false);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(body);
    var res = xhr.responseText;

    var list = res.split("\"");

    var list3 = [];
    var l = 0;
    for (var i = 0; i < list.length; i++) {
        if(list[i] !== " "){
            list3[l] = list[i];
            l++;
        }
    }

    var list2 = [];
    var count = 0;
    for (var i = 0; i < list3.length / 2; i++) {
        var v1 = parseFloat(list3[2 * i + 1]);
        var v2 = parseFloat(list3[2 * i + 2]);
        list2[i] = [v1, v2];
        count++;
    }

    addStreet(list2, count - 1);
 }