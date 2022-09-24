import {saveEntity,loadEntity,showEntity,form} from './global.js';
import { showTable } from './table.js'

const handleSubmit = async (e) => {
    e.preventDefault();
    const entity = Object.fromEntries(new FormData(e.target));
    const res = await saveEntity('/customer',entity);
    const data = await res;
    console.log("resetting")
    form.elements["message"].value = "Customer successfully updated";
};

const handleLoad = async (e) => {
    e.preventDefault();
    const id = form.elements['id'].value
    if (id) {
        const res = await loadEntity(`/customer/${id}`);
        form.elements["message"].value = "Customer loaded from database";
    }
    else {
      form.elements["message"].value = "Please key in the id to load";
    }
};

const handleCountryChange = async (e) => {
    console.log("Country code selected");
    const countryCode = form.elements['countryCode'].value
    if (countryCode) {
        const response = await fetch(`/products?countryCode=${countryCode}`);
        if (response.ok) {
            const json = await response.json();
            console.log(json);
            showTable("productsTable",json);
            form.elements["message"].value = "24 Sep 2022 4:22pm Insurance policies";
        }
        else {
            form.elements["message"].value = `Server returned error ${response.status}`;
        }
    }
};
form.elements['countryCode'].addEventListener('change',handleCountryChange)
