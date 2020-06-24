const categories = [{id:1, name:'Fruits'}, {id:2, name:'Books'},
    {id:3, name:'Phones'}, {id:4, name:'Films'}];

render();

function render() {
    clearDom();

    for(let i=0;i<categories.length;i++) {
        let categoryWrapper = document.createElement('div');
        categoryWrapper.classList.add('category_item');

        let title = document.createElement('h3');
        title.innerHTML = `${categories[i].name}`
        categoryWrapper.appendChild(title)

        let imgWrapper = document.createElement('div');
        imgWrapper.classList.add('category_item_img-wrapper')
        categoryWrapper.appendChild(imgWrapper)

        let editImg = document.createElement('div');
        editImg.innerHTML = 'Edit'
        editImg.onclick = function () {addEditItemModal(categories[i])}
        editImg.classList.add('category_item_img')
        imgWrapper.appendChild(editImg)

        let deleteImg = document.createElement('div');
        deleteImg.innerHTML = 'Delete'
        deleteImg.onclick = function () {}
        deleteImg.classList.add('category_item_img')
        imgWrapper.appendChild(deleteImg)

        categoryWrapper.addEventListener('click', function(e) {
            if (!e.target.classList.contains('category_item_img') && !e.target.classList.contains('category_add_item')) {
                showAllGoodsByCategory(categories[i].name);
            }
        });
        document.getElementsByClassName('category_items-wrapper')[0].appendChild(categoryWrapper);
    }

    let addImg = document.createElement('div');
    addImg.innerHTML = 'Add new category'
    addImg.onclick = function () {addNewCategory()}
    addImg.classList.add('category_add_item');
    document.getElementsByClassName('category_items')[0].appendChild(addImg);
}

function clearDom() {
    document.getElementsByClassName('category_items-wrapper')[0].innerHTML = ''
    if(document.getElementsByClassName('goods_items-wrapper')[0])
        document.getElementsByClassName('goods_items-wrapper')[0].innerHTML = ''
}

function addEditItemModal(category) {
    const modal = createEditItemModal(category)
    modal.open()
}

function createEditItemModal(category){
    const modal = $.modal({
        title: `Edit ${category.name}`,
        closeable: true,
        content: `<form  id="category_edit-form">
        <p><p>Change category title:</p>
         <input type="text" id="category_change-name" placeholder=${category.name}></p>
`,
        footerButtons: [
            {
                text: 'Add', type: 'primary', handler() {
                    editItem(category)
                }
            },
            {
                text: 'Cancel', type: 'danger', handler() {
                    modal.close()
                    modal.destroy()
                    render()
                }
            },
        ]
    })
    return modal
}

function editItem(category) {
    if(!document.getElementById('category_change-name').value) console.log('k');
    else category.name = document.getElementById('category_change-name').value;

    document.getElementById("category_edit-form").reset();
    render()
}


function addNewCategory() {
    const modal = createAddItemModal()
    modal.open()
}

function createAddItemModal(){
    const modal = $.modal({
        title: 'Add new category',
        closeable: true,
        content: `<form name="form_add_item">
        <p><p>Enter category title:</p> <input type="text"></p>
`,
        footerButtons: [
            {text: 'Add', type: 'primary'},
            {text: 'Cancel', type: 'danger', handler() {modal.close()}},
        ]
    })
    return modal
}
