function fetchCats() {
    const url = 'https://cat-fact.herokuapp.com/facts';
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const listContainer = $("#catFacts");

            data.forEach(function (post, i) {
                const updatedAt = new Date(post.updatedAt).toLocaleString();
                const verified = post.status.verified ? '✅' : '❌';

                listContainer.append(`
                        <div class="list-item">
                            <img src="res/catPics/image${i%5}.png">
                            <div>
                                <h5>${post.text}</h5>
                                <p>Datum příspěvku: ${updatedAt}</p>
                                <p>Ověřeno: ${verified}</p>
                            </div>
                        </div>
                    `);
            });
        })
        .catch(err => {
            console.error('cat pics: ', err);
        });
}

