// selectors
const app = document.querySelector('.app');
const form = document.forms.fetch;
const postForm = body => {
    console.log(body);
    const bodystr = JSON.stringify(body);
    return fetch(`/customer`, {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json'
            },
            bodystr
        });
};

const handleSubmit = async (e) => {
    e.preventDefault();
    const body = Object.fromEntries(new FormData(e.target));

    const res = await postForm(body);
    const data = await res.json();

    console.log(data.json);
};

form.addEventListener('submit', handleSubmit);