const goods = [
    {id:1, name:'Banana', quantity:10, price:16},
    {id:1, name:'Apple', quantity:12, price:26},
    {id:1, name:'Straw', quantity:7, price:10},
    {id:1, name:'Apple', quantity:12, price:26},
    {id:1, name:'Straw', quantity:7, price:10},
    {id:1, name:'Orange', quantity:2, price:40}];

function showAllGoodsByCategory(categoryName) {

    if(document.getElementsByClassName('goods_items-wrapper')[0]) {
        document.getElementsByClassName('goods_items')[0].removeChild(document.getElementsByClassName('goods_items-wrapper')[0]);
    }

    let goodsWrapper = document.createElement('div');
    goodsWrapper.classList.add('goods_items-wrapper');
    document.getElementsByClassName('goods_items')[0].appendChild(goodsWrapper);

    renderGoods(categoryName);
}

function renderGoods(categoryName) {

    clearDom();

    let title = document.createElement('div');
    title.classList.add('goods_title');
    title.innerHTML = `${categoryName} goods`;
    document.getElementsByClassName('goods_items-wrapper')[0].appendChild(title);

    for(let i=0;i<goods.length;i++) {
        let goodWrapper = document.createElement('div');
        goodWrapper.classList.add('goods_item');

        let title = document.createElement('h3');
        title.innerHTML = `${goods[i].name}`
        goodWrapper.appendChild(title)

        let contentWrapper = document.createElement('div');
        contentWrapper.innerHTML =
            `<div>Price : ${goods[i].price}</div>
       <div>Quantity : ${goods[i].quantity} </div>
      `
        goodWrapper.appendChild(contentWrapper)

        let funcWrapper = document.createElement('div');
        goodWrapper.appendChild(funcWrapper)

        let editImg = document.createElement('div');
        editImg.innerHTML = 'Edit'
        editImg.onclick = function () { addEditItemModal(goods[i]) }
        funcWrapper.appendChild(editImg)

        let deleteImg = document.createElement('div');
        deleteImg.innerHTML = 'Delete'
        deleteImg.onclick = function () {deleteCategory(goods[i])}
        funcWrapper.appendChild(deleteImg)

        let addImg = document.createElement('div');
        addImg.innerHTML = 'Add'
        addImg.onclick = function () {addAddItemModal(goods[i])}
        funcWrapper.appendChild(addImg);

        document.getElementsByClassName('goods_items-wrapper')[0].appendChild(goodWrapper);
    }
}

function clearDom() {
    if(document.getElementsByClassName('goods_items-wrapper')[0])
        document.getElementsByClassName('goods_items-wrapper')[0].innerHTML = ''
}

function addEditItemModal(good) {
    const modal = createEditItemModal(good)
    modal.open()
}

function createEditItemModal(good){
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

`,
        footerButtons: [
            {
                text: 'Add', type: 'primary', handler() {
                    editItem(good)
                }
            },
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close()
                    modal.destroy()
                    render()
                }},
        ]
    })
    return modal
}

function editItem(good) {
    if(!document.getElementById('good_change-name').value) console.log('k');
    else good.name = document.getElementById('good_change-name').value;

    if(!document.getElementById('good_change-price').value) console.log('k');
    else good.price = document.getElementById('good_change-price').value;


    if(!document.getElementById('good_change-quantity').value) console.log('k');
    else good.quantity = document.getElementById('good_change-quantity').value;

    document.getElementById("good_edit-form").reset();
    render()
}

function deleteCategory(category) {
    console.log(category.name)
}

function addAddItemModal(good) {
    const modal = createAddItemModal(good)
    modal.open()
}

function createAddItemModal(good){
    const modal = $.modal({
        title: 'Add new category',
        closeable: true,
        content: `<form name="good_add-form">
                <div>
                  <p>Change good quantity:</p>
                  <input type="number" id="good_change-quantity" placeholder=${good.quantity}>
                </div>
              `,
        footerButtons: [
            {
                text: 'Add', type: 'primary', handler() {
                    addItem(good)
                }
            },
            {text: 'Cancel', type: 'danger', handler() {
                    modal.close()
                    modal.destroy()
                    render()
                }},
        ]
    })
    return modal
}

