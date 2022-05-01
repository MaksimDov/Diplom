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

function searchAdvert(data) {
    window.name = data
    location.href = location.href
}

function viewSearch() {
    $.get(location.href + "/" + window.name + '/viewSearch').then(function (adView) {
        var element = document.getElementById('advertsList');
        var searchField = document.getElementById('searchField');
        searchField.value = window.name
        element.innerHTML = ""
        if(adView.toString() == '[]'){
            alert("Объявлений подходящих вам по интересам нет, или вы не указали в профиле интересные вам темы!")
        }
        else {
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
                cost = elem.cost;

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

                let price = document.createElement('h3')
                price.textContent = cost
                price.className = "price"
                let tagUl = document.createElement('ul')
                for(let i=0;i<elem.tags.length;++i){
                    var tagLi = document.createElement('li')
                    tagLi.innerHTML = elem.tags[i];
                    tagUl.appendChild(tagLi)
                }
                block.appendChild(picImg)
                block.appendChild(naz)
                block.appendChild(price)
                block.appendChild(opis)
                block.appendChild(tagUl)
                element.appendChild(block)
                block.appendChild(button)
            })
        }
    });
}

function changeView(data) {
    if (data.id === "buttonSearch"){
        window.name = '3'
    }
    else{
        window.name = '0'
    }
    location.href = location.href
}


$(document).ready(function () {
    if (window.name === '0' || window.name === ''){
        viewAdverts();
    }
    else{
        viewSearch()
    }
})