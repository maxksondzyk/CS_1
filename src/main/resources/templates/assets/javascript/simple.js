console.log("success");
let tokenCookie = getCookie('token');
console.log(tokenCookie)

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

/*PUT*/


// let obj = {
//     "title": "tomatoes1",
//     "category": "vegetables1",
//     "quantity": "1021",
//     "price": "951"
// }
//
// fetch("/api/good", {
//         method: "PUT",
//         headers: {
//             'Content-Type': 'application/json',
//             'token': tokenCookie
//         },
//         body: JSON.stringify(obj)
//     }
// ).then(function(response) {
//     response.json().then(function (data) {
//         console.log(data)
//     })
// })

/*GET*/

fetch("/api/allGoods", {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
            'token': tokenCookie
        }
    }
).then(function(response) {
    response.json().then(function (data) {
        console.log(data)
    })
})

