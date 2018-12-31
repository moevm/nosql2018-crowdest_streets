$("form[name='uploader']").submit(function(e) {
    var formData = new FormData($(this)[0]);

    $.ajax({
        url: 'Servlet?method=import',
        type: "POST",
        data: formData,
        async: false,
        success: function (msg) {},
        error: function(msg) {
            alert('Ошибка!');
        },
        cache: false,
        contentType: false,
        processData: false
    });
    e.preventDefault();
});


var mass = [];

window.onload = function() {
    var body = 'method=getTable';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", '/Servlet', false);

    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(body);
    var res = xhr.responseText;

    mass = res.split("\"");
    mass.splice(0, 1);

    for (var i = 0; i < mass.length/2; i++) {
        var tbody = document.getElementById('myTable').getElementsByTagName("TBODY")[0];
        var row = document.createElement("TR");
        var td1 = document.createElement("TD");

        var e = document.createElement('a');
        e.href = 'mapservlet?method=find&street=' + mass[i * 2];
        e.appendChild(document.createTextNode(mass[i * 2]));
        td1.appendChild(e);
        var td2 = document.createElement("TD");
        td2.appendChild(document.createTextNode(mass[i * 2 + 1]));
        row.appendChild(td1);
        row.appendChild(td2);
        tbody.appendChild(row);
    }
}

function sort(row_ind) {

    var rowsMass = [];

    for(var i = 0; i < mass.length/2; i++){
        rowsMass[i] = [mass[i*2], mass[i*2 + 1]]
    }

    rowsMass.sort(sortFunction);

    function sortFunction(a, b) {
        if(row_ind === 0){
            if (a[row_ind] === b[row_ind]) {
                return 0;
            }
            else {
                return (a[row_ind] < b[row_ind]) ? -1 : 1;
            }
        }else{
            return (b[row_ind] - a[row_ind]);
        }
    }

    var elmtTable = document.getElementById('myTable');
    var tbody = document.getElementById('myTable').getElementsByTagName("TBODY")[0];
    var tableRows = tbody.getElementsByTagName("TR");
    var rowCount = tableRows.length;
    for (var x = rowCount - 1; x > 0; x--) {
        tbody.removeChild(tableRows[x]);
    }

    for (i = 0; i < rowsMass.length; i++) {
        var tbody = document.getElementById('myTable').getElementsByTagName("TBODY")[0];

        var row = document.createElement("TR");
        var td1 = document.createElement("TD");
        var e = document.createElement('a');
        e.href = 'mapservlet?method=find&street=' + rowsMass[i].toString().substring(0, rowsMass[i].toString().indexOf(','));
        e.appendChild(document.createTextNode(rowsMass[i].toString().substring(0, rowsMass[i].toString().indexOf(','))));
        td1.appendChild(e);
        var td2 = document.createElement("TD");
        td2.appendChild(document.createTextNode(rowsMass[i].toString().substring(rowsMass[i].toString().indexOf(',') + 1)));
        row.appendChild(td1);
        row.appendChild(td2);
        tbody.appendChild(row);
    }
}

function db_export() {
    var body = 'method=loaddb';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", '/Servlet', false);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(body);
    var res = xhr.response;

    download(res, "pgsdb.graphml", "text/plain");
}

function download(data, filename, type) {
    var file = new Blob([data], {type: type});
    if (window.navigator.msSaveOrOpenBlob)
        window.navigator.msSaveOrOpenBlob(file, filename);
    else {
        var a = document.createElement("a"),
            url = URL.createObjectURL(file);
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        setTimeout(function() {
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
        }, 0);
    }
}

function show1() {
    var current_street = document.getElementById('strInput').value;
    var body = 'street=' + current_street;
    document.location.href = "mapservlet?street=" + current_street;
}

function btnAll() {
    document.location.href = "mapservlet?street=all";
}
