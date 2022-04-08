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

function deleteAdvert() {
    $.get(location.href + '/delete').then(function (data) {
        document.location = data
    });
}


function viewSingle() {
    $.get(location.href + '/updateSingleAdvert').then(function (adView) {
        if (adView !== "") {
            $('#advertsList').empty();
            var element = document.getElementById('advertsList');
            var fragment = document.createDocumentFragment();
            var setUserName, setAdName, setAdDescription, setTags, setAdId, owner;
            var parsed = JSON.parse(adView);

            setUserEmail = parsed.userEmail;
            setUserPhoneNumber = parsed.userPhoneNumber;
            setUserName = parsed.userName;
            cost = parsed.adCost
            setAdName = parsed.adName;
            setAdDescription = parsed.adDescription;
            setAdId = parsed.adId;
            owner = parsed.owner;
            setTags = "";

            let block = document.createElement('div')
            block.className = 'blc'
            let picImg = document.createElement('img')
            // picImg.src = path;
            let naz = document.createElement('h2')
            naz.textContent = setAdName
            let price = document.createElement('p6')
            price.textContent = "Цена: " + cost
            // var button= document.createElement('button');
            // button.className = "btn";
            // button.type = "submit";
            // button.id = setAdId;
            // button.onclick = function () {
            //     clickRoom(this);
            // };
            // button.textContent = "Удалить";

            if(owner === "true") {
                var button = document.createElement('button');
                button.className = "btn";
                button.type = "submit";
                button.id = "del";
                button.onclick = function () {
                    deleteAdvert(this);
                    // alert("del" + this.id);
                };
                button.textContent = "Удалить";
                block.appendChild(button)
            }



            let opis = document.createElement('p')
            opis.textContent = "Описание: " + setAdDescription

            let tagP = document.createElement('p2')
            tagP.className = 'tags'
            for(let i=0;i< parsed.tags.length;++i){
                setTags = setTags + parsed.tags[i] + '; '
            }
            tagP.textContent =  setTags

            let usName = document.createElement('p3')
            usName.textContent = "Имя: " + setUserName;

            let phone = document.createElement('p4')
            phone.textContent = "Номер телефона: " + setUserPhoneNumber;

            let mail = document.createElement('p5')
            mail.textContent = "Почта: " + setUserEmail;
            var picArray = parsed.pictures;
            picImg.id = "picId";
            picImg.alt = 0;
            picImg.src = picArray[0];
            picImg.onclick = function (){
                changePic()
            }
            block.appendChild(picImg)
            block.appendChild(naz)
            block.appendChild(usName)
            block.appendChild(phone)
            block.appendChild(mail)
            block.appendChild(opis)
            block.appendChild(price)
            block.appendChild(tagP)
            element.appendChild(block)
            block.appendChild(button)
            }
    })
}

// function viewSinglePerSec() {
//     setInterval(viewSingle, 150000);
// }

$(document).ready(function () {
    viewSingle();
    // viewSinglePerSec();
})