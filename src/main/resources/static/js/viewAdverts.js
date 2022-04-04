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
            // var fragment = document.createDocumentFragment();
            var setUserName, setAdName, setAdDescription, setTags, setAdId, path;;
            var parsed = JSON.parse(adView);
            parsed.forEach((elem) => {
                setAdName = elem.adName;
                setUserName = elem.userName;
                setAdDescription = elem.adDescription;
                setAdId = elem.adId;
                path = elem.picPath;
                setTags = "";

                if(setAdDescription.length > 149){
                    setAdDescription = setAdDescription.substring(0,149) + '...'
                }
                let block = document.createElement('div')
                block.className = 'blc'
                let picImg = document.createElement('img')
                picImg.src = path;
                let naz = document.createElement('h2')
                naz.textContent = setAdName
                var button= document.createElement('button');
                button.className = "btn";
                button.type = "submit";
                button.id = setAdId;
                button.onclick = function () {
                    clickRoom(this);
                };
                button.textContent = "Посмотреть";
                let opis = document.createElement('p')
                opis.textContent = "Описание: " + setAdDescription

                let tagUl = document.createElement('ul')
                for(let i=0;i<elem.tags.length;++i){
                    var tagLi = document.createElement('li')
                    tagLi.innerHTML = elem.tags[i];
                    tagUl.appendChild(tagLi)
                }
                block.appendChild(picImg)
                block.appendChild(naz)
                block.appendChild(opis)
                block.appendChild(tagUl)
                element.appendChild(block)
                block.appendChild(button)
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