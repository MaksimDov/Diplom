function changePic() {
    $.get(location.href + '/changePic').then(function (json) {
        var parsed = JSON.parse(json);
        data = parsed.pictures;
        var element = document.getElementById('picId');
        if (element.alt < data.length - 1) {
            element.src = data[Number(element.alt) + Number(1)];
            element.alt = Number(element.alt) + Number(1);
        } else {
            element.src = data[0];
            element.alt = 0;
        }
    })
}


function viewSingle() {
    $.get(location.href + '/updateSingleAdvert').then(function (adView) {
        if (adView !== "") {
            $('#advertsList').empty();
            var element = document.getElementById('advertsList');
            var fragment = document.createDocumentFragment();
            var setUserName, setAdName, setAdDescription, setTags, setAdId;
            var parsed = JSON.parse(adView);

            setUserName = parsed.userName;
            setAdName = parsed.adName;
            setAdDescription = parsed.adDescription;
            setAdId = parsed.adId;
            setTags = "";
            parsed.tags.forEach((el) => {
                setTags = setTags + el + " ";
            })
            var buttonElement = document.createElement('button');
            buttonElement.className = "btn";
            buttonElement.type = "submit";
            buttonElement.id = setAdId;
            // buttonElement.onclick = function () {
            //     clickRoom(this);
            //     // alert("aaaaa" + this.id);
            // };
            buttonElement.textContent = "id: " + setAdId + " userName:" + setUserName + " adName:" + setAdName + " des:" + setAdDescription + " tags:" + setTags;
            fragment.appendChild(buttonElement);

            var brb = document.createElement('br');
            fragment.appendChild(brb);
            var brbr = document.createElement('br');
            fragment.appendChild(brbr);
            element.appendChild(fragment);
            var picFrag = document.createDocumentFragment('a');
            var picElement = document.createElement('img');

            var picArray = parsed.pictures;
            picElement.id = "picId";
            picElement.alt = 0;
            picElement.src = picArray[0];
            picElement.onclick = function (){
                changePic()
            }
            picFrag.appendChild(picElement);
            element.appendChild(picFrag);
        }
    });
}

// function viewSinglePerSec() {
//     setInterval(viewSingle, 150000);
// }

$(document).ready(function () {
    viewSingle();
    // viewSinglePerSec();
})