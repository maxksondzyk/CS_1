

function showAllGoodsByCategory(category) {
    if(document.getElementsByClassName('goods_items-wrapper')[0]) {
        document.getElementsByClassName('goods_items')[0].removeChild(document.getElementsByClassName('goods_items-wrapper')[0]);
    }

    let goodsWrapper = document.createElement('div');
    goodsWrapper.classList.add('goods_items-wrapper');
    document.getElementsByClassName('goods_items')[0].appendChild(goodsWrapper);

    getGoodsArray(category)
}


function getGoodsArray(category) {
    fetch(`api/categoryProducts/${category.id}`, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'token': tokenCookie
            }
        }
    ).then(function(response) {
        response.json().then(function (data) {
            console.log(data)
            const goods = []
            for(let i=0;i<data.products.length;i++) {
                goods.push(data.products[i])
            }
            // console.log(categories)
            renderGoods(goods,category.name, category.id)
        })
    }).catch(function (error) {
        alert(error)
    })
}

function renderGoods(goods,categoryName,categoryId) {

    clearDom();

    let title = document.createElement('div');
    title.classList.add('goods_title');
    title.innerHTML = `${categoryName.capitalizeFirst()} goods`;
    document.getElementsByClassName('goods_items-wrapper')[0].appendChild(title);

    for(let i=0;i<goods.length+1;i++) {

        let bigWRapper = document.createElement('div');
        bigWRapper.classList.add('goods_item-wrapper');
        document.getElementsByClassName('goods_items-wrapper')[0].appendChild(bigWRapper);

        if(i===goods.length) {
            /**ADD NEW GOOD*/

            let addNew = document.createElement('div');
            addNew.classList.add('goods_add_item');
            addNew.innerHTML = 'Add new';
            addNew.onclick = function () {
                addNewItemModal(categoryName,categoryId)
            }
            bigWRapper.appendChild(addNew)
        }

        else {

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
                addAddAmountItemModal(goods[i],categoryName,categoryId)
            }
            addImg.classList.add('goods_item-func')
            funcWrapper.appendChild(addImg);

            /**REMOVE AMOUNT*/

            let removeImg = document.createElement('div');
            removeImg.innerHTML = 'Remove'
            removeImg.onclick = function () {
                addRemoveAmountItemModal(goods[i],categoryName,categoryId)
            }
            removeImg.classList.add('goods_item-func-2')
            funcWrapper.appendChild(removeImg);

            /**EDIT GOOD*/

            let editImg = document.createElement('div');
            editImg.innerHTML = 'Edit'
            editImg.onclick = function () {
                addEditItemModal(goods[i],categoryName,categoryId)
            }
            editImg.classList.add('goods_item-func')
            funcWrapper.appendChild(editImg)

            /**DELETE GOOD*/

            let deleteImg = document.createElement('div');
            deleteImg.innerHTML = 'Delete'
            deleteImg.onclick = function () {
                addDeleteItemModal(goods[i],categoryName,categoryId)
            }
            deleteImg.classList.add('goods_item-func-2')
            funcWrapper.appendChild(deleteImg)

        }
    }
}

function clearDom() {
    if(document.getElementsByClassName('goods_items-wrapper')[0])
        document.getElementsByClassName('goods_items-wrapper')[0].innerHTML = ''
}

/**ADD AMOUNT TO GOOD*/

function addAddAmountItemModal(good,categoryName,categoryId) {
    const modal = createAddItemModal(good,categoryName,categoryId)
    modal.open()
}

function createAddItemModal(good,categoryName,categoryId){
    const modal = $.modal({
        title: 'Add amount',
        closeable: true,
        content: `<form name="good_add-form">
                <div>
                  <p>How many new items to add?</p>
                  <input type="number" id="good_add-quantity">
                </div>
                </form>
              `,
        footerButtons: [
            {text: 'Add', type: 'primary', handler() {
                    addAmountItem(good,categoryName,categoryId,modal)}},
            {text: 'Cancel', type: 'danger', handler() {
                modal.close();
                modal.destroy()
                }},
        ]})
    return modal
}

function addAmountItem(good,categoryName,categoryId,modal) {
    let oldAmount = parseFloat(good.amount);
    let newAmount = parseFloat(document.getElementById('good_add-quantity').value);
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
        alert('Added')
        let category = {'name':`${categoryName}`,id:categoryId}
        getGoodsArray(category);
        modal.close()
        modal.destroy()
    }).catch(function (error) {
        alert(error)
    })
}

/**REMOVE AMOUNT TO GOOD*/

function addRemoveAmountItemModal(good,categoryName,categoryId) {
    const modal = createRemoveItemModal(good,categoryName,categoryId)
    modal.open()
}

function createRemoveItemModal(good,categoryName,categoryId){
    const modal = $.modal({
        title: 'Remove amount',
        closeable: true,
        content: `<form name="good_add-form">
                <div>
                  <p>How many new items to remove?</p>
                  <input type="number" id="good_remove-quantity">
                </div>
                </form>
              `,
        footerButtons: [
            {text: 'Remove', type: 'primary', handler() {
                    removeAmountItem(good,categoryName,categoryId,modal)}},
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close();
                    modal.destroy()
                }},
        ]})
    return modal
}

function removeAmountItem(good,categoryName,categoryId,modal) {
    let oldAmount = parseFloat(good.amount);
    let newAmount = parseFloat(document.getElementById('good_remove-quantity').value);
    let amount = oldAmount - newAmount
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
        let category = {'name':`${categoryName}`,id:categoryId}
        getGoodsArray(category);
        modal.close()
        modal.destroy()
    }).catch(function (error) {
        alert(error)
    })
}

/**EDIT GOOD*/

function addEditItemModal(good,categoryName,categoryId) {
    const modal = createEditItemModal(good,categoryName,categoryId)
    modal.open()
}

function createEditItemModal(good,categoryName,categoryId){
    const modal = $.modal({
        title: `Edit ${good.name}`,
        closeable: true,
        content: `
          <form id="good_edit-form">
            <div>
              <p>Change good title:</p>
              <input type="text" id="good_change-name" placeholder=${good.name}>
            </div>
            <div>
              <p>Change good price:</p>
              <input type="number" id="good_change-price" placeholder=${good.price}>
            </div>
            </form>

            `,
        footerButtons: [
            {text: 'Edit', type: 'primary', handler() {
                    editItem(good,modal,categoryName,categoryId)}},
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close();
                    modal.destroy()
                }},
        ]})
    return modal
}

function editItem(good,modal,categoryName,categoryId) {
    let item={"id":`${good.id}`};

    if(document.getElementById('good_change-name').value)
        item.title = `${document.getElementById('good_change-name').value}`

    if(document.getElementById('good_change-price').value)
        item.price = `${document.getElementById('good_change-price').value}`

    console.log(item)
    console.log(document.getElementById('good_change-name').value)

    fetch(`api/good`, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'token': tokenCookie
            },
            body: JSON.stringify(item)
        }
    ).then(function(response) {
        alert('Changed')
        let category = {'name':`${categoryName}`,id:categoryId}
        getGoodsArray(category);
        modal.close()
        modal.destroy()
    }).catch(function (error) {
        alert(error)
    })
}



/**DELETE GOOD*/

function addDeleteItemModal(good,categoryName,categoryId) {
    const modal = createDeleteItemModal(good,categoryName,categoryId)
    modal.open()
}

function createDeleteItemModal(good,categoryName,categoryId) {
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
                    deleteItem(good,modal,categoryName,categoryId)}},
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close();
                    modal.destroy()
                }},
        ]})
    return modal
}

function deleteItem(good,modal,categoryName,categoryId) {
    fetch(`api/good/${good.id}`, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json',
                'token': tokenCookie
            }
        }
    ).then(function(response) {
        alert('Deleted')
        let category = {'name':`${categoryName}`,id:categoryId}
        getGoodsArray(category)
        modal.close()
        modal.destroy()
    }).catch(function (error) {
        alert(error)
    })
}

/**ADD NEW ITEM*/

function addNewItemModal(categoryName,categoryId) {
    const modal = createNewItemModal(categoryName,categoryId)
    modal.open()
}
function createNewItemModal(categoryName,categoryId){
    const modal = $.modal({
        title: 'Add new good',
        closeable: true,
        content: `<form name="good_new-form">
                <div>
                  <p>Title:</p>
                  <input type="text" id="good_new-title">
                  <p>Price:</p>
                  <input type="text" id="good_new-price">
                  <p>Amount:</p>
                  <input type="text" id="good_new-quantity">
                </div>
                </form>
              `,
        footerButtons: [
            {text: 'Add', type: 'primary', handler() {
                    addNewItem(categoryName,categoryId,modal)}},
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close();
                    modal.destroy()
                }},
        ]})
    return modal
}

function addNewItem(categoryName,categoryId,modal) {
    let item = {
        "title": document.getElementById('good_new-title').value,
        "category": categoryName,
        "quantity": document.getElementById('good_new-quantity').value,
        "price": document.getElementById('good_new-price').value
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
        alert('Added')
        let category = {'name':`${categoryName}`,id:categoryId}
        getGoodsArray(category);
        modal.close()
        modal.destroy()
    }).catch(function (error) {
        alert(error)
    })
}



