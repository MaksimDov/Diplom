function viewProfileData() {
    $.get(location.href + '/update').then(function (profileData) {
        if (profileData !== "") {
            var parsed = JSON.parse(profileData);
            var nameElement = document.getElementById('name');
            nameElement.value = parsed.userName;
            var loginElement = document.getElementById('login');
            loginElement.value = parsed.userLogin;
            var phoneNumberElement = document.getElementById('phoneNumber');
            phoneNumberElement.value = parsed.userPhoneNumber;
            var emailElement = document.getElementById('email');
            emailElement.value = parsed.userEmail;

            // var tagStr = "";
            // for(let i = 0; i < parsed.tags.length; i++){
            //     tagStr = tagStr + parsed.tags[i] + "; ";
            // }
            var interesElement = document.getElementById('interes');
            // interesElement.innerHTML = tagStr;
            for(let i=0;i<parsed.tags.length;++i){
                var tagLi = document.createElement('li')
                tagLi.innerHTML = parsed.tags[i];
                interesElement.appendChild(tagLi)
            }
            // var interesElement = document.getElementById('interes');
            // interesElement.innerHTML = tagStr;
        }
    });
}

// function viewSinglePerSec() {
//     setInterval(viewSingle, 150000);
// }

$(document).ready(function () {
    viewProfileData();
    // viewSinglePerSec();
})