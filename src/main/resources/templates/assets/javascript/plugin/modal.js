Element.prototype.appendAfter = function(element){
    element.parentNode.insertBefore(this, element.nextSibling)
}

function _createModalFooter(buttons = []){
    if(buttons.length ===0){
        return document.createElement('div')
    }
    const footer = document.createElement('div')
    footer.classList.add('modal-footer')
    buttons.forEach(btn => {
        const button = document.createElement('button')
        button.classList.add('btn')
        button.classList.add(`btn-${btn.type}`)
        button.textContent = btn.text
        button.onclick = btn.handler
        footer.appendChild(button)
    })

    return footer
}

function _createModal(options) {
    const modal = document.createElement('div');
    modal.classList.add('kmodal');
    modal.insertAdjacentHTML('afterbegin', `
    <div class="modal-overlay" data-close="true">
        <div class="modal-window">
            <div class="modal-header">
                <span class="modal-title">${options.title || 'Modal window'}</span>
                ${options.closeable ? '<span class="modal-close" data-close="true">&times;</span>' : ''}
            </div>
            <div class="modal-body" data-content>
              ${options.content || ''}
            </div>
        </div>
    </div>
    `);
    const footer = _createModalFooter(options.footerButtons)
    footer.appendAfter(modal.querySelector('[data-content]'))
    document.body.appendChild(modal);
    return modal;
}


$.modal = function (options) {
    const $modal = _createModal(options);
    const ANIMATION_SPEED = 200;
    let closing = false;
    let destroyed = false;

    const modal = {
        open() {
            if (destroyed) return console.log('Modal is destroyed');
            !closing && $modal.classList.add('open');
        },
        close() {
            closing = true;
            $modal.classList.remove('open');
            $modal.classList.add('hide');
            setTimeout(() => {
                $modal.classList.remove('hide');
                closing = false;
            }, ANIMATION_SPEED);
        },
    };

    const listener = (event) => {
        if (event.target.dataset.close) modal.close();
    };

    $modal.addEventListener('click', listener);

    return Object.assign(modal, {
        destroy() {
            $modal.parentNode.removeChild($modal);
            $modal.removeEventListener('click', listener);
            destroyed = true;
        },
        setContent(html) {
            $modal.querySelector('[data-content]').innerHTML = html;
        },
    });
};
