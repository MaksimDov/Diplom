// function create() {
//     $.get('/playrooms/create').then(function (data) {
//         document.location = data
//     });
// }

function clickRoom(data) {
    $.get(location.href + "/" + data.id + '/watchAdvert').then(function (dataAdvert) {
        if (dataAdvert[0] === "T")
            alert(dataAdvert);
        else
            document.location = dataAdvert;
    });
}

function viewAdverts() {
    $.get('/viewAdverts/update').then(function (adView) {
        if (adView !== "") {
            $('#advertsList').empty();
            var element = document.getElementById('advertsList');
            var fragment = document.createDocumentFragment();
            var setUserName, setAdName, setAdDescription, setTags, setAdId, path;;
            var parsed = JSON.parse(adView);
            parsed.forEach((elem) => {
                setUserName = elem.userName;
                setAdName = elem.adName;
                setAdDescription = elem.adDescription;
                setAdId = elem.adId;
                path = elem.picPath;
                setTags = "";
                elem.tags.forEach((el) => {
                    setTags = setTags + el + " ";
                })
                var picFrag = document.createDocumentFragment('a');
                var picElement = document.createElement('img');
                picElement.id = path;
                picElement.src = path;
                picFrag.appendChild(picElement);
                element.appendChild(picFrag);
                var buttonElement = document.createElement('button');
                        buttonElement.className = "btn";
                        buttonElement.type = "submit";
                        buttonElement.id = setAdId;
                        buttonElement.onclick = function () {
                            clickRoom(this);
                        };
                        buttonElement.textContent = "id: " + setAdId + " userName:" + setUserName + " adName:" + setAdName + " des:" + setAdDescription + " tags:" + setTags;
                        // buttonElement.textContent = "text";
                        fragment.appendChild(buttonElement);

                        var brb = document.createElement('br');
                        fragment.appendChild(brb);
                        var brbr = document.createElement('br');
                        fragment.appendChild(brbr);
                        element.appendChild(fragment)
            })
        }
    });
}

function viewRoomsPerSec() {
    setInterval(viewAdverts(), 5000);
}

$(document).ready(function () {
    viewAdverts();
    viewRoomsPerSec();
})