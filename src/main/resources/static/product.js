import {saveEntity,loadEntity,showEntity,form} from './global.js';

const handleSubmit = async (e) => {
    e.preventDefault();
    const entity = Object.fromEntries(new FormData(e.target));
    const res = await saveEntity('/product',entity);
    const data = await res;
    form.elements["message"].value = "Product successfully updated";
};

const handleLoad = async (e) => {
    e.preventDefault();
    const id = form.elements['id'].value
    if (id) {
        const res = await loadEntity(`/product/${id}`);
        form.elements["message"].value = "Product loaded from database";
    }
    else {
      form.elements["message"].value = "Please key in the id to load";
    }
};
form.elements['load'].addEventListener('click',handleLoad)
form.addEventListener('submit', handleSubmit);
