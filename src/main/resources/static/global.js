// selectors
export const form = document.forms.main;

export const saveEntity = (url,entity) => {
    const body = JSON.stringify(entity);
    console.log(body);
    return fetch(url, {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json'
            },
            body: body
        })
      .then(function (response) {
        if (response.ok) {
            const json = response.json();
            form.reset();
            return json;
        }
        throw new Error("Server returned an error")
      })
      .catch(function (err) {
        form.elements["message"].value = err;
      });
    };

export const loadEntity = url => {
    fetch(url)
      .then(function (response) {
        if (response.ok) {
            return response.json();
        }
        throw new Error("Server returned an error")
      })
      .then(function (json) {
          console.log(json);
          showEntity(json);
          return json;
      })
      .catch(function (err) {
        console.log(err);
        form.elements["message"].value = err;
      });
};

export function showEntity(json) {
    for(var key in json)
    {
      if(json.hasOwnProperty(key)) {
        const elem = form.elements[key];
        if (elem) elem.value = json[key];
      }
    }
}

