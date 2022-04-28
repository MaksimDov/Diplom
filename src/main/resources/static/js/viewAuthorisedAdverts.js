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


function viewMyAd() {
    $.get('/viewAuthorisedAdverts/viewMyAd').then(function (adView) {
        var element = document.getElementById('advertsList');
        element.innerHTML = ""
        if(adView.toString() == '[]'){
            alert("У вас нет объявлений!")
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

function viewRecomend() {
    $.get('/viewAuthorisedAdverts/viewRecomend').then(function (adView) {
        var element = document.getElementById('advertsList');
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

function viewSearch() {
    $.get('/viewAuthorisedAdverts/viewSearch').then(function (adView) {
        var element = document.getElementById('advertsList');
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

function viewAuthorisedAdverts() {
    $.get('/viewAuthorisedAdverts/update').then(function (adView) {
        if(adView.toString() == '[]'){
            alert("Объявлений покаа нет!")
        }
        else {
            $('#advertsList').empty();
            var element = document.getElementById('advertsList');
            // var fragment = document.createDocumentFragment();
            var setUserName, setAdName, setAdDescription, setTags, setAdId, path, cost;
            var parsed = JSON.parse(adView);
            parsed.forEach((elem) => {
                setAdName = elem.adName;
                setUserName = elem.userName;
                setAdDescription = elem.adDescription;
                setAdId = elem.adId;
                path = elem.picPath;
                cost = elem.adCost;
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
                let price = document.createElement('h3')
                price.textContent = cost
                price.className = "price"
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
                block.appendChild(price)
                block.appendChild(opis)
                block.appendChild(tagUl)
                element.appendChild(block)
                block.appendChild(button)
            })
        }
    });
}

// function viewRoomsPerSec() {
//     setInterval(viewAuthorisedAdverts(), 5000);
// }


function changeView(data) {
    if(data.textContent === "Мои объявления"){
        window.name = '1'
    }
    else if(data.textContent === "Рекомендации"){
        window.name = '2'
    }
    else if (data.id === "buttonSearch"){
        window.name = '3'
    }
    else{
        window.name = '0'
    }
    location.href = location.href
}


$(document).ready(function () {
    var myAd = document.getElementById('MyAdvert');
    var rec = document.getElementById("recomend")
    if (window.name === '0' || window.name === ''){
        rec.textContent = "Рекомендации"
        myAd.textContent = "Мои объявления"
        viewAuthorisedAdverts();
    }
    if (window.name === '1'){
        myAd.textContent = "Все объявления"
        rec.textContent = "Рекомендации"
        viewMyAd()
    }
    if (window.name === '2'){
        rec.textContent = "Все объявления"
        myAd.textContent = "Мои объявления"
        viewRecomend()
    }
    if (window.name === '3'){
        viewSearch()
    }
})