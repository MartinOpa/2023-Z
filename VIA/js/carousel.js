function initCarousel(carouselId, path, count) {
    const carouselInner = $(`#${carouselId} .carousel-inner`);
    for (let i = 0; i < count; i++) {
        const imageName = `image${i}.png`;

        const carouselItem = $('<div class="carousel-item"></div>');
        const img = $(`
            <img src="${path}${imageName}" class="d-block w-100" data-toggle="modal" data-target="#${imageName.replace(/\..*/, '')}Modal">
        `);

        carouselItem.append(img);
        carouselInner.append(carouselItem);

        $('body').append(`
            <div class="modal fade" id="${imageName.replace(/\..*/, '')}Modal" tabindex="-1" role="dialog" aria-labelledby="${imageName.replace(/\..*/, '')}ModalLabel" aria-hidden="true">
                <div class="modal-dialog d-flex justify-content-center align-items-center" role="document">
                    <div class="modal-body text-center">
                        <img src="${path}${imageName}" class="d-block max-width-100" alt="${imageName}">
                    </div>
                </div>
            </div>
        `);
    }

    $(`#${carouselId} .carousel-item:first-child`).addClass('active');
}

