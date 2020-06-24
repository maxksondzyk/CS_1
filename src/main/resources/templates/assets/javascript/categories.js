const categories = [{id:1, name:'Fruits'}, {id:2, name:'Books'},
    {id:3, name:'Phones'}, {id:4, name:'Films'}];

render();

function render() {
    clearDom();

    for(let i=0;i<categories.length;i++) {
        let categoryWrapper = document.createElement('div');
        categoryWrapper.classList.add('category_item');

        let title = document.createElement('h2');
        title.innerHTML = `${categories[i].name}`
        title.onclick = function () { console.log('ff')}
        categoryWrapper.appendChild(title)

        let editImg = document.createElement('div');
        editImg.innerHTML = '<img src="../images/edit.svg" style="max-width: 20px">'
        editImg.onclick = function () {addEditItemModal(categories[i])}
        categoryWrapper.appendChild(editImg)

        let deleteImg = document.createElement('div');
        deleteImg.innerHTML = '<img src="../images/trash.svg" style="max-width: 20px">'
        deleteImg.onclick = function () {deleteCategory(categories[i])}
        categoryWrapper.appendChild(deleteImg)


        document.getElementsByClassName('category_items')[0].appendChild(categoryWrapper);
    }

    let addImg = document.createElement('div');
    addImg.innerHTML = '<img src="../images/plus.svg" style="max-width: 20px">'
    addImg.onclick = function () {addNewCategory()}
    document.getElementsByClassName('category_items')[0].appendChild(addImg);
}

function clearDom() {
    document.getElementsByClassName('category_items')[0].innerHTML = ''
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





