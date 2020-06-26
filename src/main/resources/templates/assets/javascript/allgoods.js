function renderAllGoods(goods) {
    console.log('goods',goods)
    clearDom();

    if(!document.getElementsByClassName('goods_items-wrapper')[0]) {
        let goodsWrapper = document.createElement('div');
        goodsWrapper.classList.add('goods_items-wrapper');
        document.getElementsByClassName('goods_items')[0].appendChild(goodsWrapper);
    }

    let titleWrapper = document.createElement('div');
    titleWrapper.classList.add('goods_title-wrapper');
    document.getElementsByClassName('goods_items-wrapper')[0].appendChild(titleWrapper);

    let title = document.createElement('div');
    title.classList.add('goods_title');
    title.innerHTML = `All goods`;
    titleWrapper.appendChild(title);

    let addNew = document.createElement('div');
    addNew.classList.add('goods_add_item');
    addNew.innerHTML = 'Add new';
    addNew.onclick = function () {
        addNewItemModal2()
    }
    titleWrapper.appendChild(addNew)


    for(let i=0;i<goods.length;i++) {

        let bigWRapper = document.createElement('div');
        bigWRapper.classList.add('goods_item-wrapper');
        document.getElementsByClassName('goods_items-wrapper')[0].appendChild(bigWRapper);

            let goodWrapper = document.createElement('div');
            goodWrapper.classList.add('goods_item');
            bigWRapper.appendChild(goodWrapper);

            let goodTitle = document.createElement('h3');
            goodTitle.innerHTML = `${goods[i].name.capitalizeFirst()}`;
            goodTitle.classList.add('goods_item-title')
            goodWrapper.appendChild(goodTitle)

            let contentWrapper = document.createElement('div');
            contentWrapper.classList.add('goods_item_content')
            contentWrapper.innerHTML =
                `<div class="goods_item_content-price">Price : ${goods[i].price} uah</div>
                <div class="goods_item_content-quantity">Amount : ${goods[i].amount} item(s) </div>
                `
            goodWrapper.appendChild(contentWrapper)

            let funcWrapper = document.createElement('div');
            funcWrapper.classList.add('goods_item-func-wrapper')
            goodWrapper.appendChild(funcWrapper)

            /**ADD AMOUNT*/
            let addImg = document.createElement('div');
            addImg.innerHTML = 'Add'
            addImg.onclick = function () {
                addAddAmountItemModal2(goods[i])
            }
            addImg.classList.add('goods_item-func')
            funcWrapper.appendChild(addImg);

            /**REMOVE AMOUNT*/

            let removeImg = document.createElement('div');
            removeImg.innerHTML = 'Remove'
            removeImg.onclick = function () {
                addRemoveAmountItemModal2(goods[i])
            }
            removeImg.classList.add('goods_item-func-2')
            funcWrapper.appendChild(removeImg);

            /**EDIT GOOD*/

            let editImg = document.createElement('div');
            editImg.innerHTML = 'Edit'
            editImg.onclick = function () {
                addEditItemModal2(goods[i])
            }
            editImg.classList.add('goods_item-func')
            funcWrapper.appendChild(editImg)

            /**DELETE GOOD*/

            let deleteImg = document.createElement('div');
            deleteImg.innerHTML = 'Delete'
            deleteImg.onclick = function () {
                addDeleteItemModal2(goods[i])
            }
            deleteImg.classList.add('goods_item-func-2')
            funcWrapper.appendChild(deleteImg)
    }

    let search =  document.createElement('div');
    search.classList.add('goods_items-search')
    search.innerHTML = '<label>Search good by title</label>' +
        '<input type="text" id="goods_search-title" placeholder="Enter title">' +
        '<button class="btn-search">Search</button>'
    document.getElementsByClassName('goods_items-wrapper')[0].appendChild(search)

    document.getElementsByClassName('btn-search')[0].onclick = function () {
        getGoodByTitle(document.getElementById("goods_search-title").value)}

}

function getGoodByTitle(goodTitle) {
    console.log(goodTitle)
    fetch(`api/goodTitle/${goodTitle.toLowerCase()}`, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'token': tokenCookie
            }
        }
    ).then(function(response) {
        response.json().then(function (data) {
            console.log(data)
            let newData = {
                'amount':data.quantity,
                'name':data.title,
                'price':data.price,
                'id':data.id,
                'groupID':data.categoryId}
            let goodArray = []
            goodArray.push(newData)
            addFoundGoodsToDom(goodArray)
        })
    }).catch(function (error) {
        alert(error)
    })
}

function addFoundGoodsToDom(goods) {

    renderAllGoods(goods)
}



/**ADD AMOUNT ITEM*/
function addAddAmountItemModal2(good) {
    const modal = createAddItemModal2(good)
    modal.open()
}

function createAddItemModal2(good){
    const modal = $.modal({
        title: 'Add amount',
        closeable: true,
        content: `<form name="good_add-form">
                <div>
                  <p>How many new items to add?</p>
                  <input type="number" min="0" id="goods_add-quantity">
                </div>
                </form>
              `,
        footerButtons: [
            {text: 'Add', type: 'primary', handler() {
                    addAmountItem2(good,modal)}},
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close();
                    modal.destroy()
                }},
        ]})
    return modal
}

function addAmountItem2(good,modal) {
    if(document.getElementById('goods_add-quantity').value>0) {
        let oldAmount = parseFloat(good.amount);
        let newAmount = parseFloat(document.getElementById('goods_add-quantity').value);
        let amount = oldAmount + newAmount
        let item = {
            "quantity": `${amount}`,
            "id":`${good.id}`
        }

        fetch(`api/good`, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'token': tokenCookie
                },
                body: JSON.stringify(item)
            }
        ).then(function(response) {
            alert('The operation was successful')
            getCategoriesArray();
            getAllGoods()

            modal.close()
            modal.destroy()
        }).catch(function (error) {
            alert(error)
        })
    } else if(document.getElementById('goods_add-quantity').value<0) {alert('Invalid input. The amount of goods to be added should be positive.')}
    else alert('Invalid input. The amount of goods to be added shouldn`t be empty.')

}

/**REMOVE AMOUNT ITEM*/

function addRemoveAmountItemModal2(good) {
    const modal = createRemoveItemModal2(good)
    modal.open()
}

function createRemoveItemModal2(good){
    const modal = $.modal({
        title: 'Remove amount',
        closeable: true,
        content: `<form name="good_add-form">
                <div>
                  <p>How many new items to remove?</p>
                  <input type="number" min="0" id="goods_remove-quantity">
                </div>
                </form>
              `,
        footerButtons: [
            {text: 'Remove', type: 'primary', handler() {
                    removeAmountItem2(good,modal)}},
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close();
                    modal.destroy()
                }},
        ]})
    return modal
}

function removeAmountItem2(good,modal) {
    if(document.getElementById('goods_remove-quantity').value>0) {
        let oldAmount = parseFloat(good.amount);
        let newAmount = parseFloat(document.getElementById('goods_remove-quantity').value);
        let amount = oldAmount - newAmount
        if(amount<0) amount=0;
        let item = {
            "quantity": `${amount}`,
            "id":`${good.id}`
        }

        fetch(`api/good`, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'token': tokenCookie
                },
                body: JSON.stringify(item)
            }
        ).then(function(response) {
            alert('Removed')
            getCategoriesArray();
            getAllGoods()
            modal.close()
            modal.destroy()
        }).catch(function (error) {
            alert(error)
        })
    } else if(document.getElementById('goods_remove-quantity').value<0) {alert('Invalid input. The amount of goods to be removed should be positive.')}
    else alert('Invalid input. The amount of goods to be removed shouldn`t be empty.')

}

/**EDIT AMOUNT ITEM*/


function addEditItemModal2(good) {
    const modal = createEditItemModal2(good)
    modal.open()
}

function createEditItemModal2(good){
    const modal = $.modal({
        title: `Edit ${good.name}`,
        closeable: true,
        content: `
          <form id="good_edit-form">
            <div>
              <p>Change good title:</p>
              <input type="text" id="goods_change-name" placeholder=${good.name}>
            </div>
            <div>
              <p>Change good price:</p>
              <input type="number" min="0" id="goods_change-price" placeholder=${good.price}>
            </div>
            </form>

            `,
        footerButtons: [
            {text: 'Edit', type: 'primary', handler() {
                    editItem2(good,modal)}},
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close();
                    modal.destroy()
                }},
        ]})
    return modal
}

function editItem2(good,modal) {
    let item={"id":`${good.id}`};

    if(document.getElementById('goods_change-name').value)
        item.title = `${document.getElementById('goods_change-name').value.toLowerCase()}`

    if(document.getElementById('goods_change-price').value)
        item.price = `${document.getElementById('goods_change-price').value}`

    if(!document.getElementById('goods_change-price').value && !document.getElementById('goods_change-name').value) {
        alert('Invalid input. At least one value shouldn`t be empty.')
        return 0;
    }
    // console.log(item)
    // console.log(document.getElementById('goods_change-name').value)

    fetch(`api/good`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'token': tokenCookie
            },
            body: JSON.stringify(item)
        }
    ).then(function(response) {
        alert('The operation was successful')
        getCategoriesArray();
        getAllGoods()
        modal.close()
        modal.destroy()
    }).catch(function (error) {
        alert(error)
    })
}


/**DELETE GOOD*/

function addDeleteItemModal2(good) {
    const modal = createDeleteItemModal2(good)
    modal.open()
}

function createDeleteItemModal2(good) {
    const modal = $.modal({
        title: 'Delete good',
        closeable: true,
        content: `<form name="good_new-form">
                <div>
                  <p>Are you sure you want to delete ${good.name}?</p>
                </div>
                </form>
              `,
        footerButtons: [
            {text: 'Delete', type: 'primary', handler() {
                    deleteItem2(good,modal)}},
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close();
                    modal.destroy()
                }},
        ]})
    return modal
}

function deleteItem2(good,modal) {
    fetch(`api/good/${good.id}`, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json',
                'token': tokenCookie
            }
        }
    ).then(function(response) {
        alert('Deleted')
        getCategoriesArray();
        getAllGoods()
        modal.close()
        modal.destroy()
    }).catch(function (error) {
        alert(error)
    })
}

/**ADD NEW ITEM*/

function addNewItemModal2() {
    const modal = createNewItemModal2()
    modal.open()
}
function createNewItemModal2(){
    const modal = $.modal({
        title: 'Add new good',
        closeable: true,
        content: `<form name="good_new-form">
                <div>
                  <p>Title:</p>
                  <input type="text" id="goods_new-title">
                  <p>Price:</p>
                  <input type="text" id="goods_new-price">
                  <p>Category:</p>
                  <input type="text" id="goods_new-category">
                  <p>Amount:</p>
                  <input type="text" id="goods_new-quantity">
                </div>
                </form>
              `,
        footerButtons: [
            {text: 'Add', type: 'primary', handler() {
                    addNewItem2(modal)}},
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close();
                    modal.destroy()
                }},
        ]})
    return modal
}

function addNewItem2(modal) {

    if(!document.getElementById('goods_new-title').value || !document.getElementById('goods_new-quantity').value || !document.getElementById('goods_new-price').value ) {
        alert('Invalid input. All values shouldn`t be empty.')
        return 0;
    }

    let item = {
        "title": document.getElementById('goods_new-title').value.toLowerCase(),
        "category": document.getElementById('goods_new-category').value.toLowerCase(),
        "quantity": document.getElementById('goods_new-quantity').value.toLowerCase(),
        "price": document.getElementById('goods_new-price').value.toLowerCase()
    }

    fetch("api/good", {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'token': tokenCookie
            },
            body: JSON.stringify(item)
        }
    ).then(function(response) {
        alert('The operation was successful')
        getCategoriesArray();
        getAllGoods()
        modal.close()
        modal.destroy()
    }).catch(function (error) {
        alert(error)
    })
}

